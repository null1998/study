package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataBaseService {
    @Autowired
    private StudentDao studentDao;
    @Cacheable(value = {"cache_l1","cache_l2"}, key = "#id")
    public String getById(String id) {
        return studentDao.getById(id);
    }

    @CacheEvict(value = {"cache_l1","cache_l2"}, key = "#id")
    public void removeById(String id) {
    }
}
