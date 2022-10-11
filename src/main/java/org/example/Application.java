package org.example;

import org.example.config.DefaultAutoExpireCacheManager;
import org.example.dao.StudentDao;
import org.example.service.DataBaseService;
import org.example.util.TimeUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

@SpringBootApplication
@EnableCaching
@RestController
public class Application {
    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private StudentDao studentDao;

    private final Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
        Random random = new Random();
        // 设定缓存在凌晨3-5点范围内的随机时间点过期
        long ttl = ChronoUnit.MILLIS.between(LocalDateTime.now(), TimeUtil.getNextAssignTime(3 + random.nextInt(2), random.nextInt(60), random.nextInt(60)));
        // TODO 设置分级缓存
        config.put("cache_l1", new CacheConfig(ttl, ttl));
        config.put("cache_l2", new CacheConfig(ttl, ttl));
        return new DefaultAutoExpireCacheManager(redissonClient, config);
    }

    @GetMapping("/limit/{id}")
    public String getById(@PathVariable String id) throws InterruptedException {
        semaphore.acquire();
        String name = studentDao.getById(id);
        semaphore.release();
        return name;
    }

    @GetMapping("/cache/{id}")
    public String getCacheById(@PathVariable String id) {
        return dataBaseService.getById(id);
    }
}
