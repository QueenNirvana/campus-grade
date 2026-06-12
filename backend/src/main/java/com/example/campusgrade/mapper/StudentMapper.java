package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.Student;
import java.util.List;

public interface StudentMapper {
    List<Student> findAll();
    Student findById(Long id);
    Student findByUserId(Long userId);
    int insert(Student item);
    int update(Student item);
    int delete(Long id);
    long countAll();
}
