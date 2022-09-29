package org.example.thread;

import org.example.common.CommonConst;
import org.example.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {
    private static final ThreadLocal<Integer> threadLocalCounter = new ThreadLocal<>();

    private static final ThreadLocal<byte[]> THREAD_LOCAL_BYTE_ARRAY = new ThreadLocal<>();

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
     * -XX:+PrintGCDetails
     */
    @Test
    public void testThreadPoolCauseMemoryLeakProblem() throws InterruptedException {
        int threadNum = 8;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        // 线程池阻塞队列长度设置为0，超出核心线程数的请求直接新建线程进行处理
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor(4,8,500, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),"test-threadLocal-");
        for (int i = 0; i < threadNum; i++) {
            threadPoolExecutor.submit(()->{
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
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor(4,8,500, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),"test-threadLocal-");
        for (int i = 0; i < threadNum; i++) {
            threadPoolExecutor.submit(()->{
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
            new Thread(()->{
                THREAD_LOCAL_BYTE_ARRAY.set(new byte[CommonConst._100MB]);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.gc();
    }
}
