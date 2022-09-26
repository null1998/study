package org.example.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    public static ThreadPoolExecutor getThreadPoolExecutor(String threadNamePrefix) {
        return getThreadPoolExecutor(4, 8, 16, TimeUnit.SECONDS, 32, threadNamePrefix);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit timeUnit, int queueCapacity, String threadNamePrefix) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                new LinkedBlockingDeque<>(queueCapacity),
                (Runnable r) -> {
                    Thread t = new Thread(r);
                    t.setName(threadNamePrefix + t.getId());
                    return t;
                },
                new ThreadPoolExecutor.AbortPolicy());
    }
}
