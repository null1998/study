package org.example.service;

import org.example.dao.StudentMapper;
import org.example.entity.Student;
import org.example.util.ThreadUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class TransactionCallerService {
    @Resource
    private StudentMapper studentMapper;

    @Resource
    private TransactionCalleeService transactionCalleeService;

    /**
     * 调用者无事务，被调用者事务为独立新事务，被调用者事务不受调用者异常影响，数据提交
     *
     * @param student 实体
     */
    public void noTransactionThrowExceptionCalleePropagationRequired(Student student) {
        transactionCalleeService.testPropagationRequired(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 调用者有事务，被调用者加入调用者的事务，被调用者事务受调用者异常影响，数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionThrowExceptionCalleePropagationRequired(Student student) {
        transactionCalleeService.testPropagationRequired(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 调用者无事务，被调用者也不开启事务，数据提交
     *
     * @param student 实体
     */
    public void noTransactionCalleePropagationSupportsThrowException(Student student) {
        transactionCalleeService.testPropagationSupports(student);
    }

    /**
     * 调用者有事务，被调用者加入调用者的事务，数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionCalleePropagationSupportsThrowException(Student student) {
        transactionCalleeService.testPropagationSupports(student);
    }

    /**
     * 调用者无事务，被调用者抛出异常IllegalTransactionStateException
     */
    public void noTransactionCalleePropagationMandatory() {
        transactionCalleeService.testPropagationMandatory(null);
    }

    /**
     * 调用者有事务，被调用者加入调用者的事务，被调用者事务受调用者error影响，数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionThrowErrorCalleePropagationMandatory(Student student) {
        transactionCalleeService.testPropagationMandatory(student);
        throw new Error("测试异常");
    }

    /**
     * 调用者无事务，被调用者事务为独立新事务，被调用者事务不受调用者异常影响，数据提交
     *
     * @param student 实体
     */
    public void noTransactionThrowExceptionCalleePropagationRequiresNew(Student student) {
        transactionCalleeService.testPropagationRequiresNew(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 调用者有事务，被调用者创建独立新事务并挂起调用者事务，被调用者事务不受调用者异常影响，数据提交
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionThrowExceptionCalleePropagationRequiresNew(Student student) {
        transactionCalleeService.testPropagationRequiresNew(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 调用者有事务，被调用者创建独立新事务并挂起调用者事务，被调用者抛出异常事务回滚后向外抛出异常，调用者数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionInsertCalleePropagationRequiresNewThrowException(Student student) {
        studentMapper.insert(student);
        transactionCalleeService.testPropagationRequiresNewThrowException();
    }

    /**
     * 调用者有事务，被调用者不支持事务，被调用者插入数据后抛出异常，被调用者数据已提交
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionCalleePropagationNotSupported(Student student) {
        transactionCalleeService.testPropagationNotSupported(student);
    }

    /**
     * 调用者有无事务，被调用者不支持事务，被调用者插入数据后抛出异常，被调用者数据已提交
     *
     * @param student 实体
     */
    public void noTransactionCalleePropagationNever(Student student) {
        transactionCalleeService.testPropagationNever(student);
    }

    /**
     * 调用者有事务，被调用者不支持事务，抛出异常
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionCalleePropagationNever(Student student) {
        transactionCalleeService.testPropagationNever(student);
    }

    /**
     * 调用者有事务，被调用者支持事务，被调用者抛出异常回滚，异常在调用者处被捕获不影响调用者，调用者数据提交
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionTryCatchCalleePropagationNested(Student student) {
        try {
            studentMapper.insert(student);
            transactionCalleeService.testPropagationNestedThrowException();
        } catch (Exception e) {
            System.out.println("捕获被调用者异常，避免调用者回滚事务");
        }
    }

    /**
     * 调用者有事务，被调用者支持事务，调用者抛出异常回滚，被调用者受外层事务影响，被调用者数据回滚
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hasTransactionThrowExceptionCalleePropagationNested(Student student) {
        transactionCalleeService.testPropagationNested(student);
        throw new RuntimeException("测试异常");
    }

    /**
     * 调用者有事务，被调用者耗时较长需要异步执行，为保证数据一致性，希望能在调用者事务完成后根据事务结果决定是否执行异步任务
     *
     * @param student 实体
     */
    @Transactional(rollbackFor = Throwable.class)
    public void hashTransactionCallAfterCompletion(Student student) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (STATUS_COMMITTED == status) {
                    System.out.println("事务已提交，开始执行异步任务");
                    ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor("test-call-after-committed-");
                    threadPoolExecutor.execute(()-> studentMapper.deleteByPrimaryKey(student.getId()));
                }
                if (STATUS_ROLLED_BACK == status) {
                    System.out.println("事务已回滚，取消执行异步任务");
                }
            }
        });
        studentMapper.insert(student);
    }
}
