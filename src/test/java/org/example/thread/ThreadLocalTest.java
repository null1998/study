package org.example.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import org.example.common.CommonConst;
import org.example.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.*;

public class ThreadLocalTest {
    private static final ThreadLocal<Integer> threadLocalCounter = new ThreadLocal<>();

    private static final ThreadLocal<byte[]> THREAD_LOCAL_BYTE_ARRAY = new ThreadLocal<>();

    private static final InheritableThreadLocal<String> INHERITABLE_THREAD_LOCAL_STRING = new InheritableThreadLocal<>();

    private static final TransmittableThreadLocal<String> TRANSMITTABLE_THREAD_LOCAL_STRING = new TransmittableThreadLocal<>();

    @Test
    public void testThreadLocal() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    if (threadLocalCounter.get() == null) {
                        threadLocalCounter.set(1);
                    } else {
                        threadLocalCounter.set(threadLocalCounter.get() + 1);
                    }
                }
                assert 10 == threadLocalCounter.get();
                threadLocalCounter.remove();
            }).start();
        }
    }

    /**
     * 测试线程池保留线程导致threadLocal内存泄漏问题，最终泄漏内存大小=corePoolSize * 每个线程的ThreadLocalMap大小
     * 因为有强引用链thread->threadLocalMap->entry->value的存在，导致在thread存活期间，value无法被回收
     * -XX:+PrintGCDetails
     */
    @Test
    public void testThreadPoolCauseMemoryLeakProblem() throws InterruptedException {
        int threadNum = 8;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        // 线程池阻塞队列长度设置为0，超出核心线程数的请求直接新建线程进行处理
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor(4, 8, 500, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), "test-threadLocal-");
        for (int i = 0; i < threadNum; i++) {
            threadPoolExecutor.execute(() -> {
                THREAD_LOCAL_BYTE_ARRAY.set(new byte[CommonConst._100MB]);
                ThreadUtil.sleep(100);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        // 此时线程池存活8个线程，泄漏约8*100=800MB
        System.gc();
        // 等待线程池回收线程
        ThreadUtil.sleep(1000);
        // 空闲线程已回收，剩余4个核心线程无法回收，最终泄漏约4*100=400MB
        System.gc();
    }

    /**
     * 测试线程池通过threadLocal的remove方法，避免保留线程导致的threadLocal内存泄漏问题
     * -XX:+PrintGCDetails
     */
    @Test
    public void testThreadPoolAvoidMemoryLeakProblem() throws InterruptedException {
        int threadNum = 8;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        // 线程池阻塞队列长度设置为0，超出核心线程数的请求直接新建线程进行处理
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor(4, 8, 500, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), "test-threadLocal-");
        for (int i = 0; i < threadNum; i++) {
            threadPoolExecutor.execute(() -> {
                THREAD_LOCAL_BYTE_ARRAY.set(new byte[CommonConst._100MB]);
                ThreadUtil.sleep(100);
                // 移除threadLocal
                THREAD_LOCAL_BYTE_ARRAY.remove();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        // 此时线程池有存活线程，但threadLocal内存已被回收
        System.gc();
    }

    /**
     * 测试线程自动销毁避免threadLocal内存泄漏问题，但不建议直接new的方式创建线程
     * -XX:+PrintGCDetails
     */
    @Test
    public void testThreadAutoDestroyAvoidMemoryLeakProblem() throws InterruptedException {
        int threadNum = 4;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                THREAD_LOCAL_BYTE_ARRAY.set(new byte[CommonConst._100MB]);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.gc();
    }

    /**
     * 测试如何把父线程的threadLocal传递给子线程的threadLocal，使用inheritableThreadLocals，是父子线程中独立存在的两个副本
     * 但因为复制仅在创建线程时才进行，所以不适用于线程池复用线程的情况，不适合生产
     */
    @Test
    public void testCopyInheritableThreadLocalByNewThread() throws InterruptedException {
        INHERITABLE_THREAD_LOCAL_STRING.set("父线程的值");
        // 创建子线程时，把父线程的inheritableThreadLocals复制到子线程的inheritableThreadLocals中
        Thread thread = new Thread(() -> {
            assert "父线程的值".equals(INHERITABLE_THREAD_LOCAL_STRING.get());
            // 仅清除子线程的inheritableThreadLocals
            INHERITABLE_THREAD_LOCAL_STRING.remove();
            assert INHERITABLE_THREAD_LOCAL_STRING.get() == null;
        });
        thread.start();
        thread.join();
        // 父线程的inheritableThreadLocals的值保持原样，不受子线程remove影响
        assert "父线程的值".equals(INHERITABLE_THREAD_LOCAL_STRING.get());
    }

    /**
     * 测试在线程池中使用inheritableThreadLocals导致的问题
     */
    @Test
    public void testThreadPoolCauseInheritableThreadLocalProblem() {
        INHERITABLE_THREAD_LOCAL_STRING.set("父线程的值");
        // 创建只有一个线程的线程池，测试复用线程情况下inheritableThreadLocals存在的问题
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor(1, new LinkedBlockingDeque<>(1), "test-threadLocal-");
        for (int i = 0; i < 2; i++) {
            int fi = i;
            threadPoolExecutor.execute(() -> {
                if (fi == 0) {
                    // 该线程第一次创建时，复制父线程的inheritableThreadLocals
                    assert "父线程的值".equals(INHERITABLE_THREAD_LOCAL_STRING.get());
                    // 线程结束后需要remove避免内存泄漏
                    INHERITABLE_THREAD_LOCAL_STRING.remove();
                }
                // 复用线程执行任务时，不会再复制一次父线程的inheritableThreadLocals
                assert fi != 1 || INHERITABLE_THREAD_LOCAL_STRING.get() == null;
            });
        }
        // 更新父线程inheritableThreadLocals的值
        INHERITABLE_THREAD_LOCAL_STRING.set("父线程的值更新");
        threadPoolExecutor.execute(() -> {
            // 复用线程执行任务时，不会再复制一次父线程的inheritableThreadLocals，也就是不能接收父线程inheritableThreadLocals的更新
            assert INHERITABLE_THREAD_LOCAL_STRING.get() == null;
            INHERITABLE_THREAD_LOCAL_STRING.remove();
        });
    }

    /**
     * 测试TransmittableThreadLocal在线程池环境下在父子线程中传递threadLocal，不受线程池影响，可以在生产中使用
     */
    @Test
    public void testTransmittableThreadLocal() throws InterruptedException {
        TRANSMITTABLE_THREAD_LOCAL_STRING.set("父线程的值");
        // 创建只有一个线程的线程池，测试复用线程情况下的TransmittableThreadLocal
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor(1, new LinkedBlockingDeque<>(4), "test-threadLocal-");
        for (int i = 0; i < 2; i++) {
            threadPoolExecutor.execute(TtlRunnable.get(() -> {
                // 每次执行任务，子线程都会复制threadLocal
                assert "父线程的值".equals(TRANSMITTABLE_THREAD_LOCAL_STRING.get());
                TRANSMITTABLE_THREAD_LOCAL_STRING.remove();
            }));
        }
        // 父线程更新threadLocal，会影响后续子线程执行任务时复制的threadLocal值
        TRANSMITTABLE_THREAD_LOCAL_STRING.set("父线程的值更新");
        for (int i = 0; i < 2; i++) {
            int fi = i;
            threadPoolExecutor.execute(TtlRunnable.get(() -> {
                assert "父线程的值更新".equals(TRANSMITTABLE_THREAD_LOCAL_STRING.get());
                if (fi == 0) {
                    // 在执行的任务中修改threadLocal，作用域仅限本次任务，不会影响此线程后续执行任务的threadLocal，也不会影响父线程的threadLocal
                    TRANSMITTABLE_THREAD_LOCAL_STRING.set("子线程的值");
                }
                assert fi != 1 || "父线程的值更新".equals(TRANSMITTABLE_THREAD_LOCAL_STRING.get());
            }));
        }
        assert "父线程的值更新".equals(TRANSMITTABLE_THREAD_LOCAL_STRING.get());
        threadPoolExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
    }
}
