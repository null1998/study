package org.example.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Objects;

@SpringBootTest
public class MySQLTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试可重复读事务隔离级别下MVCC的效果
     * set global transaction isolation level repeatable read
     */
    @Test
    public void testRepeatableReadMVCC() throws InterruptedException {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        DataSourceTransactionManager manager = new DataSourceTransactionManager(Objects.requireNonNull(jdbcTemplate.getDataSource()));
        // 开启事务a，当执行第一个查询时创建一致性视图，此后事务a的查询共用此一致性视图
        TransactionStatus status = manager.getTransaction(definition);
        String queryNameSql = "select name from teacher where id = 1";
        String countSql = "select count(*) from teacher";
        try {
            // 第一个查询，创建事务a的一致性视图
            assert "张三".equals(jdbcTemplate.queryForObject(queryNameSql, String.class));
            // 事务b执行更新并提交
            Thread updateThread = new Thread(this::updateRow);
            updateThread.start();
            updateThread.join();
            // 事务b提交的版本晚于事务a的一致性视图，不可见
            assert "张三".equals(jdbcTemplate.queryForObject(queryNameSql, String.class));

            assert Integer.valueOf(3).equals(jdbcTemplate.queryForObject(countSql, Integer.class));
            // 事务c执行插入并提交
            Thread insertThread = new Thread(this::insertRow);
            insertThread.start();
            insertThread.join();
            // 事务c提交的版本晚于事务a的一致性视图，不可见
            assert Integer.valueOf(3).equals(jdbcTemplate.queryForObject(countSql, Integer.class));

            // 提交事务a
            manager.commit(status);
        } catch (Exception e) {
            manager.rollback(status);
            throw e;
        }
        // 开启新事务，查询并创建一致性视图，事务b提交的版本早于一致性视图，可见
        assert "张三-update".equals(jdbcTemplate.queryForObject(queryNameSql, String.class));
        // 开启新事务，查询并创建一致性视图，事务c提交的版本早于一致性视图，可见
        assert Integer.valueOf(4).equals(jdbcTemplate.queryForObject(countSql, Integer.class));
    }

    /**
     * 测试读已提交事务隔离级别下MVCC的效果
     * set global transaction isolation level read committed
     */
    @Test
    public void testReadCommittedMVCC() throws InterruptedException {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        DataSourceTransactionManager manager = new DataSourceTransactionManager(Objects.requireNonNull(jdbcTemplate.getDataSource()));
        // 开启事务a
        TransactionStatus status = manager.getTransaction(definition);
        String queryNameSql = "select name from teacher where id = 1";
        String countSql = "select count(*) from teacher";
        try {
            assert "张三".equals(jdbcTemplate.queryForObject(queryNameSql, String.class));
            // 事务b执行更新并提交
            Thread updateThread = new Thread(this::updateRow);
            updateThread.start();
            updateThread.join();
            // 查询时创建事务a的一致性视图，事务b提交的版本早于事务a的一致性视图，可见
            assert "张三-update".equals(jdbcTemplate.queryForObject(queryNameSql, String.class));

            assert Integer.valueOf(3).equals(jdbcTemplate.queryForObject(countSql, Integer.class));
            // 事务c执行插入并提交
            Thread insertThread = new Thread(this::insertRow);
            insertThread.start();
            insertThread.join();
            // 查询时创建事务a的一致性视图，事务c提交的版本早于事务a的一致性视图，可见
            assert Integer.valueOf(4).equals(jdbcTemplate.queryForObject(countSql, Integer.class));

            // 提交事务a
            manager.commit(status);
        } catch (Exception e) {
            manager.rollback(status);
            throw e;
        }
    }

    private void insertRow() {
        jdbcTemplate.update("insert into teacher values(4, '小刘')");
    }

    private void updateRow() {
        jdbcTemplate.update("update teacher set name = '张三-update' where id = 1");
    }

}
