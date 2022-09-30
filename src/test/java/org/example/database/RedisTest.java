package org.example.database;

import org.example.config.RedissonConfiguration;
import org.example.util.ThreadUtil;
import org.junit.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedisTest {
    /**
     * 测试LockWatchdogTimeout机制对redis锁时间进行自动延长，redis键的格式为node id:thread id
     */
    @Test
    public void testRedission() throws InterruptedException {
        RedissonClient redissonClient = RedissonConfiguration.getRedissonClient();
        RLock watchDogLock = redissonClient.getLock("watchDogLock");
        if (watchDogLock.tryLock(3, TimeUnit.SECONDS)) {
            // 阻塞期间redis锁的ttl每下降1/3，便会恢复原状
            ThreadUtil.sleep(24000);
        }
    }
}
