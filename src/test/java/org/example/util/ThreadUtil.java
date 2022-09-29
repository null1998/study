package org.example.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    public static ThreadPoolExecutor getThreadPoolExecutor(String threadNamePrefix) {
        return getThreadPoolExecutor(4, 8, 16, TimeUnit.SECONDS, new LinkedBlockingDeque<>(32), threadNamePrefix);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(int threadNum, BlockingQueue<Runnable> workQueue, String threadNamePrefix) {
        return getThreadPoolExecutor(threadNum, threadNum, 0, TimeUnit.SECONDS, workQueue, threadNamePrefix);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> workQueue, String threadNamePrefix) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workQueue,
                (Runnable r) -> {
                    Thread t = new Thread(r);
                    t.setName(threadNamePrefix + t.getId());
                    return t;
                },
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
