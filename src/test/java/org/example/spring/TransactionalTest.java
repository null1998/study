package org.example.spring;

import org.example.dao.StudentMapper;
import org.example.entity.Student;
import org.example.service.TransactionCallerService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.IllegalTransactionStateException;

import javax.annotation.Resource;

@SpringBootTest
public class TransactionalTest {

    @Resource
    private TransactionCallerService transactionCallerService;

    @Resource
    private StudentMapper studentMapper;

    /**
     * 测试场景
     * 调用者无事务->调用被调用者事务传播为默认REQUIRED->被调用者创建独立新事务->调用者抛出异常->被调用者数据提交
     */
    @Test
    public void testCallerNoTransactionThrowExceptionCalleePropagationRequired() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.noTransactionThrowExceptionCalleePropagationRequired(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为默认REQUIRED->被调用者加入调用者事务->调用者抛出异常->被调用者数据回滚
     */
    @Test
    public void testCallerHasTransactionThrowExceptionCalleePropagationRequired() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.hasTransactionThrowExceptionCalleePropagationRequired(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者无事务->调用被调用者事务传播为SUPPORTS->被调用者无事务->被调用者抛出异常->数据提交
     */
    @Test
    public void testCallerNoTransactionCalleePropagationSupportsThrowException() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.noTransactionCalleePropagationSupportsThrowException(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为SUPPORTS->被调用者加入调用者事务->被调用者抛出异常->数据回滚
     */
    @Test
    public void testCallerHasTransactionCalleePropagationSupportsThrowException() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.hasTransactionCalleePropagationSupportsThrowException(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者无事务->调用被调用者事务传播为MANDATORY->被调用者抛出异常IllegalTransactionStateException
     */
    @Test
    public void testCallerNoTransactionCalleePropagationMandatory() {
        Assert.assertThrows(IllegalTransactionStateException.class, () -> {
            transactionCallerService.noTransactionCalleePropagationMandatory();
        });
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为MANDATORY->被调用者加入调用者事务->调用者抛出error->被调用者数据回滚
     */
    @Test
    public void testCallerHasTransactionThrowErrorCalleePropagationMandatory() {
        Student student = init();
        Assert.assertThrows(Error.class, () -> {
            transactionCallerService.hasTransactionThrowErrorCalleePropagationMandatory(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者无事务->调用被调用者事务传播为REQUIRES_NEW->被调用者创建独立新事务->调用者抛出异常->被调用者数据提交
     */
    @Test
    public void testCallerNoTransactionThrowExceptionCalleePropagationRequiresNew() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.noTransactionThrowExceptionCalleePropagationRequiresNew(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为REQUIRES_NEW->被调用者创建独立新事务并挂起调用者事务->调用者抛出异常->被调用者数据提交
     */
    @Test
    public void testCallerHasTransactionThrowExceptionCalleePropagationRequiresNew() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.hasTransactionThrowExceptionCalleePropagationRequiresNew(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为REQUIRES_NEW->被调用者创建独立新事务并挂起调用者事务->被调用者抛出异常->调用者数据回滚
     */
    @Test
    public void testCallerHasTransactionCalleePropagationRequiresNewThrowException() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.hasTransactionInsertCalleePropagationRequiresNewThrowException(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为NOT_SUPPORTED->被调用者不支持事务->被调用者插入数据后抛出异常->被调用者数据已提交
     */
    @Test
    public void testCallerHasTransactionCalleePropagationNotSupported() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.hasTransactionCalleePropagationNotSupported(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者无事务->调用被调用者事务传播为NEVER->被调用者不支持事务->被调用者插入数据后抛出异常->被调用者数据已提交
     */
    @Test
    public void testCallerNoTransactionCalleePropagationNever() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.noTransactionCalleePropagationNever(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为NEVER->被调用者不支持事务->被调用者抛出异常IllegalTransactionStateException
     */
    @Test
    public void testCallerHasTransactionCalleePropagationNever() {
        Student student = init();
        Assert.assertThrows(IllegalTransactionStateException.class, () -> {
            transactionCallerService.hasTransactionCalleePropagationNever(student);
        });
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为NESTED->被调用者抛出异常回滚，异常在调用者处被捕获不影响调用者->调用者数据提交
     * 只对DataSourceTransactionManager事务管理器起效
     */
    @Test
    public void testCallerHasTransactionTryCatchCalleePropagationNested() {
        Student student = init();
        transactionCallerService.hasTransactionTryCatchCalleePropagationNested(student);
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 调用者有事务->调用被调用者事务传播为NESTED->调用者抛出异常回滚->被调用者受外层事务影响，被调用者数据回滚
     * 只对DataSourceTransactionManager事务管理器起效
     */
    @Test
    public void testCallerHasTransactionThrowExceptionCalleePropagationNested() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionCallerService.hasTransactionThrowExceptionCalleePropagationNested(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 删除数据库数据并初始化一个实体
     *
     * @return 实体
     */
    private Student init() {
        studentMapper.deleteByPrimaryKey("1");
        Student student = new Student();
        student.setId("1");
        student.setName("张三");
        return student;
    }
}
