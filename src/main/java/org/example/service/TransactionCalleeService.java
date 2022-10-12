package org.example.service;

import org.example.dao.StudentMapper;
import org.example.entity.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TransactionCalleeService {
    @Resource
    private StudentMapper studentMapper;

    /**
     * 默认的事务传播方式REQUIRED，方法运行时若已处在一个事务中，则加入该事务，否则自己创建一个新事务
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void testPropagationRequired(Student student) {
        studentMapper.insert(student);
    }

    /**
     * SUPPORT，方法运行时若已处在一个事务中，则加入该事务，否则自己不创建新事务
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.SUPPORTS)
    public void testPropagationSupports(Student student) {
        studentMapper.insert(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * MANDATORY，方法运行时若已处在一个事务中，则加入该事务，否则抛出异常IllegalTransactionStateException
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.MANDATORY)
    public void testPropagationMandatory(Student student) {
        studentMapper.insert(student);
    }

    /**
     * REQUIRES_NEW，总是为自己创建一个新事务，方法运行时若已处在一个事务中，则挂起该事务
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
    public void testPropagationRequiresNew(Student student) {
        studentMapper.insert(student);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
    public void testPropagationRequiresNewThrowException() {
        throw new RuntimeException("测试异常");
    }


    /**
     * NOT_SUPPORTED，不支持事务，方法运行时若已处在一个事务中，则挂起该事务，方法调用完毕后恢复该事务
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.NOT_SUPPORTED)
    public void testPropagationNotSupported(Student student) {
        studentMapper.insert(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * NEVER，不支持事务，方法运行时若已处在一个事务中，则抛出异常
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.NEVER)
    public void testPropagationNever(Student student) {
        studentMapper.insert(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * NESTED，内部事务的回滚不影响外部事务，只对DataSourceTransactionManager事务管理器起效
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.NESTED)
    public void testPropagationNested(Student student) {
        studentMapper.insert(student);
    }

    /**
     * NESTED，内部事务的回滚不影响外部事务，只对DataSourceTransactionManager事务管理器起效
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.NESTED)
    public void testPropagationNestedThrowException() {
        throw new RuntimeException("测试异常");
    }
}
