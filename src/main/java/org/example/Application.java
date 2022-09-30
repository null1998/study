package org.example;

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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@EnableCaching
public class Application {
    @Autowired
    private DataBaseService dataBaseService;

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
}
