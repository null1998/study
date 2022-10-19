package org.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonConfiguration {
    private static volatile RedissonClient redissonClient;

    public static RedissonClient getRedissonClient() {
        if (redissonClient == null) {
            synchronized (RedissonConfiguration.class) {
                if (redissonClient == null) {
                    Config config = new Config();
                    config.useSingleServer()
                            .setAddress("redis://127.0.0.1:6379");
                    // 设置锁存活时间，当锁存活时间减少1/3时，重置为12s
                    config.setLockWatchdogTimeout(12000);
                    redissonClient = Redisson.create(config);
                    return redissonClient;
                }
            }
        }
        return redissonClient;
    }
}
