package org.example.spring;

import org.example.dao.StudentMapper;
import org.example.entity.Student;
import org.example.service.DataBaseService;
import org.example.service.TransactionParentService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.IllegalTransactionStateException;

import javax.annotation.Resource;

@SpringBootTest
public class TransactionalTest {

    @Resource
    private DataBaseService dataBaseService;

    @Resource
    private TransactionParentService transactionParentService;

    @Resource
    private StudentMapper studentMapper;

    @Test
    public void testTransactional() {
        Student student = new Student();
        student.setId("1");
        student.setName("张三");
        dataBaseService.save(student);
    }

    /**
     * 测试场景
     * 父类无事务->调用子类事务传播为默认REQUIRED->子类创建独立新事务->父类抛出异常->子类数据提交
     */
    @Test
    public void testParentNoTransactionThrowExceptionChildPropagationRequired() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionParentService.noTransactionThrowExceptionChildPropagationRequired(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 父类有事务->调用子类事务传播为默认REQUIRED->子类加入父类事务->父类抛出异常->子类数据回滚
     */
    @Test
    public void testParentHasTransactionThrowExceptionChildPropagationRequired() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionParentService.hasTransactionThrowExceptionChildPropagationRequired(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 父类无事务->调用子类事务传播为SUPPORTS->子类无事务->子类抛出异常->数据提交
     */
    @Test
    public void testParentNoTransactionChildPropagationSupportsThrowException() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionParentService.noTransactionChildPropagationSupportsThrowException(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 父类有事务->调用子类事务传播为SUPPORTS->子类加入父类事务->子类抛出异常->数据回滚
     */
    @Test
    public void testParentHasTransactionChildPropagationSupportsThrowException() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionParentService.hasTransactionChildPropagationSupportsThrowException(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 父类无事务->调用子类事务传播为MANDATORY->子类抛出异常IllegalTransactionStateException
     */
    @Test
    public void testParentNoTransactionChildPropagationMandatory() {
        Assert.assertThrows(IllegalTransactionStateException.class, () -> {
            transactionParentService.noTransactionChildPropagationMandatory();
        });
    }

    /**
     * 测试场景
     * 父类有事务->调用子类事务传播为MANDATORY->子类加入父类事务->父类抛出error->子类数据回滚
     */
    @Test
    public void testParentHasTransactionThrowErrorChildPropagationMandatory() {
        Student student = init();
        Assert.assertThrows(Error.class, () -> {
            transactionParentService.hasTransactionThrowErrorChildPropagationMandatory(student);
        });
        assert !studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 父类无事务->调用子类事务传播为REQUIRES_NEW->子类创建独立新事务->父类抛出异常->子类数据提交
     */
    @Test
    public void testParentNoTransactionThrowExceptionChildPropagationRequiresNew() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionParentService.noTransactionThrowExceptionChildPropagationRequiresNew(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
    }

    /**
     * 测试场景
     * 父类无事务->调用子类事务传播为REQUIRES_NEW->子类创建独立新事务并挂起父类事务->父类抛出异常->子类数据提交
     */
    @Test
    public void testParentHasTransactionThrowExceptionChildPropagationRequiresNew() {
        Student student = init();
        Assert.assertThrows(RuntimeException.class, () -> {
            transactionParentService.hasTransactionThrowExceptionChildPropagationRequiresNew(student);
        });
        assert studentMapper.selectByPrimaryKey("1").isPresent();
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
