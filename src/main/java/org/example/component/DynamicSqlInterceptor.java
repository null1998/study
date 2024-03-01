package org.example.component;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author huang
 */
//@Component
@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare", args = {Connection.class, Integer.class})
})
public class DynamicSqlInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(DynamicSqlInterceptor.class);
    private final static Map<String,String> TABLE_MAP = new LinkedHashMap<>();
    static {
        TABLE_MAP.put("UNE_PAYBOOK_REPORT_SUPERVISE","UNE_PAYBOOK_REPORT_SUPERVISE_123456");
        TABLE_MAP.put("REGION","region_123456");
        TABLE_MAP.put("FAB_BILL","FAB_BILL_123456");
        TABLE_MAP.put("FAB_AGEN_BILL","FAB_AGEN_BILL_123456");
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1. 获取 StatementHandler 对象也就是执行语句
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 2. MetaObject 是 MyBatis 提供的一个反射帮助类，可以优雅访问对象的属性，这里是对 statementHandler 对象进行反射处理，
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory());
        // 3. 通过 metaObject 反射获取 statementHandler 对象的成员变量 mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // mappedStatement 对象的 id 方法返回执行的 mapper 方法的全路径名，如ltd.newbee.mall.core.dao.UserMapper.insertUser
        String id = mappedStatement.getId();
        // 5. 获取包含原始 sql 语句的 BoundSql 对象
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        log.info("替换前---sql：{}", sql);
        // 拦截方法
        String mSql = null;
        if (isReplaceTableName(sql)) {
            TableNameVisitor visitor = new TableNameVisitor(TABLE_MAP);
            List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.HIVE);
            for (SQLStatement stmt : stmtList) {
                stmt.accept(visitor);
            }
            mSql = SQLUtils.toSQLString(stmtList, JdbcConstants.HIVE);
            log.info("替换后---mSql：{}", mSql);
            // 8. 对 BoundSql 对象通过反射修改 SQL 语句。
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, mSql);
        }
        // 9. 执行修改后的 SQL 语句。
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 使用 Plugin.wrap 方法生成代理对象
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 获取配置文件中的属性值
    }

    /**
     * 判断是否需要替换表名
     *
     * @param sql 语句
     * @return boolean
     */
    private boolean isReplaceTableName(String sql){
        for(String tableName : TABLE_MAP.keySet()){
            if(sql.toUpperCase().contains(tableName)){
                return true;
            }
        }
        return false;
    }
}

