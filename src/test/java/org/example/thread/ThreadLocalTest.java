package org.example.thread;

import org.junit.Test;

public class ThreadLocalTest {
    private static final ThreadLocal<Integer> threadLocalCounter = new ThreadLocal<>();

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
                System.out.println(Thread.currentThread().getName() + " " + threadLocalCounter.get());
                threadLocalCounter.remove();
            }).start();
        }
    }

}
