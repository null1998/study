package org.example.spring;

import org.example.entity.Student;
import org.example.service.DataBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class TransactionalTest {

    @Resource
    private DataBaseService dataBaseService;

    @Test
    public void testTransactional() {
        Student student = new Student();
        student.setId("1");
        student.setName("张三");
        dataBaseService.save(student);
    }
}
