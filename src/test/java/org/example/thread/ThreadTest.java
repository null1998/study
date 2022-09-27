package org.example.thread;

import org.example.common.Hardware;
import org.example.util.NIOUtil;
import org.example.util.OSHIUtil;
import org.example.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadTest {
    private int sum = 0;
    private char[] letters = new char[]{'a', 'b', 'c'};
    private char[] nums = new char[]{'1', '2', '3'};
    private int i = 0;
    private int j = 0;
    StringBuilder result = new StringBuilder();
    /**
     * 测试原子性问题
     */
    @Test
    public void testAtomicityProblem() {
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor("thread-test-");
        for (int i = 0; i < 1000; i++) {
            threadPoolExecutor.execute(() -> {
                sum++;
            });
        }
        assert sum != 1000;
    }

    /**
     * 测试线程的六种状态
     */
    @Test
    public void testThreadState() {
        testThreadStateNew();
        testThreadStateRunnable();
        testThreadStateBlocked();
        testThreadStateWaiting();
        testThreadStateTimedWaiting();
        testThreadStateTerminated();
    }

    @Test
    public void testAlternateOutputByWaitNotify() throws InterruptedException {
        CountDownLatch controlThreadPriority = new CountDownLatch(1);
        Object lock = new Object();
        Thread threadA = new Thread(() -> {
            try {
                controlThreadPriority.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                while (i < letters.length) {
                    result.append(letters[i++]);
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });
        threadA.start();
        Thread threadB = new Thread(() -> {
            synchronized (lock) {
                controlThreadPriority.countDown();
                while (j < nums.length) {
                    result.append(nums[j++]);
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });
        threadB.start();
        threadA.join();
        threadB.join();
        assert result.toString().equals("1a2b3c");
        assert threadA.getState() == Thread.State.TERMINATED;
        assert threadB.getState() == Thread.State.TERMINATED;
    }

    private static void testThreadStateTerminated() {
        Thread threadA = new Thread(() -> {

        });
        threadA.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert threadA.getState() == Thread.State.TERMINATED;

        // 故意抛出异常测试
        Thread threadB = new Thread(() -> {
            throw new RuntimeException("测试线程因异常结束时，状态为TERMINATED");
        });
        threadB.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert threadB.getState() == Thread.State.TERMINATED;
    }

    private static void testThreadStateTimedWaiting() {
        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        threadB.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert threadB.getState() == Thread.State.TIMED_WAITING;
    }

    private static void testThreadStateWaiting() {
        Object lock = new Object();
        Thread threadA = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threadA.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert threadA.getState() == Thread.State.WAITING;
        synchronized (lock) {
            lock.notify();
        }

        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        threadB.start();
        Thread threadC = new Thread(() -> {
            try {
                threadB.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        threadC.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert threadC.getState() == Thread.State.WAITING;
    }

    private static void testThreadStateBlocked() {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert thread.getState() == Thread.State.BLOCKED;
    }

    private static void testThreadStateRunnable() {
        Thread thread = new Thread(() -> {

        });
        thread.start();
        assert thread.getState() == Thread.State.RUNNABLE;
    }

    private static void testThreadStateNew() {
        Thread thread = new Thread(() -> {

        });
        assert thread.getState() == Thread.State.NEW;
    }

    /**
     * 测试使用多线程的优势，在io期间cpu是空闲的，io工作由direct memory access完成，
     * 所以多线程可以充分利用cpu在io期间的空闲时间
     */
    @Test
    public void testMultithreadingAdvantage() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(16);
        System.out.println(OSHIUtil.getHardwareInfo(Hardware.CPU));
        for (int i = 0; i < 16; i++) {
            int finalI = i;
            new Thread(() -> {
                NIOUtil.copyByFileChannel("C:\\from\\ideaIC-2021.1.3.exe", "C:\\to\\ideaIC-2021.1.3" + finalI + ".exe");
                countDownLatch.countDown();
            }).start();
        }
        while (countDownLatch.getCount() > 0) {
            System.out.println(OSHIUtil.getHardwareInfo(Hardware.CPU));
            Thread.sleep(1000);
        }
    }

    /**
     * 测试使用多线程的劣势，子线程中的任务是死循环，cpu占用一直是100%，所以多线程不能改善计算密集型的场景
     */
    @Test
    public void testMultithreadingDisadvantage() {
        System.out.println(OSHIUtil.getHardwareInfo(Hardware.CPU));
        for (int i = 0; i < 16; i++) {
            new Thread(() -> {
                for (; ; ) {

                }
            }).start();
        }
        while (true) {
            System.out.println(OSHIUtil.getHardwareInfo(Hardware.CPU));
        }
    }
}
