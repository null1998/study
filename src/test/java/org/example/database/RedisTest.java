package org.example.database;

import org.example.service.DataBaseService;
import org.example.dao.StudentDao;
import org.example.config.RedissonConfiguration;
import org.example.util.ThreadUtil;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
public class RedisTest {
    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private StudentDao studentDao;

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

    @Test
    public void testGet() {
        assert "10000".equals(dataBaseService.getById("10000"));
    }

    @Test
    public void testRemove() {
        dataBaseService.removeById("1");
    }
}
