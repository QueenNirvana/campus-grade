package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.Course;
import java.util.List;

public interface CourseMapper {
    List<Course> findAll();
    List<Course> findByTeacherId(Long teacherId);
    Course findById(Long id);
    int insert(Course item);
    int update(Course item);
    int delete(Long id);
    long countAll();
}
