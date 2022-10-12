package org.example.service;

import org.example.dao.StudentDao;
import org.example.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataBaseService {
    @Autowired
    private StudentDao studentDao;

    @Cacheable(value = {"cache_l1", "cache_l2"}, key = "#id")
    public String getById(String id) {
        return studentDao.getById(id);
    }

    @CacheEvict(value = {"cache_l1", "cache_l2"}, key = "#id")
    public void removeById(String id) {
    }

    @Transactional(rollbackFor = Throwable.class)
    public void save(Student student) {
        studentDao.save(student);
    }
}
