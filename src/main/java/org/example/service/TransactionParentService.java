package org.example.service;

import org.example.dao.StudentMapper;
import org.example.entity.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TransactionParentService {
    @Resource
    private StudentMapper studentMapper;

    @Resource
    private TransactionChildService transactionChildService;

    /**
     * 父类无事务，子类事务为独立新事务，子类事务不受父类异常影响，数据提交
     *
     * @param student 实体
     */
    public void noTransactionThrowExceptionChildPropagationRequired(Student student) {
        transactionChildService.testPropagationRequired(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 父类有事务，子类加入父类的事务，子类事务受父类异常影响，数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionThrowExceptionChildPropagationRequired(Student student) {
        transactionChildService.testPropagationRequired(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 父类无事务，子类也不开启事务，数据提交
     *
     * @param student 实体
     */
    public void noTransactionChildPropagationSupportsThrowException(Student student) {
        transactionChildService.testPropagationSupports(student);
    }

    /**
     * 父类有事务，子类加入父类的事务，数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionChildPropagationSupportsThrowException(Student student) {
        transactionChildService.testPropagationSupports(student);
    }

    /**
     * 父类无事务，子类抛出异常IllegalTransactionStateException
     */
    public void noTransactionChildPropagationMandatory() {
        transactionChildService.testPropagationMandatory(null);
    }

    /**
     * 父类有事务，子类加入父类的事务，子类事务受父类error影响，数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionThrowErrorChildPropagationMandatory(Student student) {
        transactionChildService.testPropagationMandatory(student);
        throw new Error("测试异常");
    }

    /**
     * 父类无事务，子类事务为独立新事务，子类事务不受父类异常影响，数据提交
     *
     * @param student 实体
     */
    public void noTransactionThrowExceptionChildPropagationRequiresNew(Student student) {
        transactionChildService.testPropagationRequiresNew(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 父类有事务，子类创建独立新事务并挂起父类事务，子类事务不受父类异常影响，数据提交
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionThrowExceptionChildPropagationRequiresNew(Student student) {
        transactionChildService.testPropagationRequiresNew(student);
        throw new RuntimeException("测试异常");
    }

}
