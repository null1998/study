package org.example;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataBaseService {
    @Cacheable(value = {"cache_l1","cache_l2"}, key = "#id")
    public String getById(String id) {
        return "hello-"+id;
    }

    @CacheEvict(value = {"cache_l1","cache_l2"}, key = "#id")
    public void removeById(String id) {
    }
}
