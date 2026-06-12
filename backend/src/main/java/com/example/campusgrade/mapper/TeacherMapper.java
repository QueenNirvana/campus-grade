package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.Teacher;
import java.util.List;

public interface TeacherMapper {
    List<Teacher> findAll();
    Teacher findById(Long id);
    Teacher findByUserId(Long userId);
    int insert(Teacher item);
    int update(Teacher item);
    int delete(Long id);
    long countAll();
}
