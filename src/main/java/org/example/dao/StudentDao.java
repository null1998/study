package org.example.dao;

import org.example.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
public class StudentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getById(String id) {
        return jdbcTemplate.queryForObject("select name from student where id = ?", new Object[]{id}, new int[]{Types.VARCHAR}, String.class);
    }

    public void save(Student student) {
        jdbcTemplate.update("insert into student(id,name )values(?,?)", student.getId(), student.getName());
    }
}
