package org.example;

import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;

import java.util.Map;

/**
 * 默认自动过期CacheManager
 */
public class DefaultAutoExpireCacheManager extends RedissonSpringCacheManager {
    public DefaultAutoExpireCacheManager(RedissonClient redisson) {
        super(redisson);
    }

    public DefaultAutoExpireCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config) {
        super(redisson, config);
    }

    public DefaultAutoExpireCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config, Codec codec) {
        super(redisson, config, codec);
    }

    public DefaultAutoExpireCacheManager(RedissonClient redisson, String configLocation) {
        super(redisson, configLocation);
    }

    public DefaultAutoExpireCacheManager(RedissonClient redisson, String configLocation, Codec codec) {
        super(redisson, configLocation, codec);
    }

    @Override
    protected CacheConfig createDefaultConfig() {
        // 为每个缓存设置默认60分钟生命周期，30分钟空闲时间
        return new CacheConfig(60 * 60 * 1000, 30 * 60 * 1000);
    }
}
