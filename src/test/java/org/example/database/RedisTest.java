package org.example.database;

import org.example.config.RedissonConfiguration;
import org.example.dao.StudentDao;
import org.example.service.DataBaseService;
import org.example.util.ThreadUtil;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    /**
     * 测试redisson锁tryLock是否会阻塞等待
     * 结论tryLock是不会阻塞等待，直接放弃获取锁，执行后续步骤，指定等待时间的tryLock才可以阻塞一段时间
     */
    @Test
    public void testRedissonBlock() throws InterruptedException {
        RedissonClient redissonClient = RedissonConfiguration.getRedissonClient();
        RLock watchDogLock = redissonClient.getLock("watchDogLock");
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor("redisson-block-");
        for (int i = 0; i < 2; i++) {
            threadPoolExecutor.submit(() -> {
                if (watchDogLock.tryLock()) {
                    System.out.println(Thread.currentThread().getName() + " 获取到锁");
                    ThreadUtil.sleep(1000);
                    watchDogLock.unlock();
                } else {
                    System.out.println(Thread.currentThread().getName() + " 没获得锁");
                }
                System.out.println(Thread.currentThread().getName() + " 结束");
            });
        }
        boolean awaitTermination = threadPoolExecutor.awaitTermination(2000, TimeUnit.MILLISECONDS);
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
