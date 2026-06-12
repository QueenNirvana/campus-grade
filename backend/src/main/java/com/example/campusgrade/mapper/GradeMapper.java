package com.example.campusgrade.mapper;

import com.example.campusgrade.dto.CourseStats;
import com.example.campusgrade.dto.OverviewStats;
import com.example.campusgrade.dto.StudentSummary;
import com.example.campusgrade.entity.Grade;
import java.util.List;
import java.util.Map;

public interface GradeMapper {
    List<Grade> findAll();
    List<Grade> findByTeacherId(Long teacherId);
    List<Grade> findByStudentId(Long studentId);
    Grade findById(Long id);
    int insert(Grade item);
    int update(Grade item);
    int delete(Long id);
    long countAll();
    OverviewStats overview();
    CourseStats courseStats(Long courseId);
    List<Map<String, Object>> courseDistribution(Long courseId);
    StudentSummary studentSummary(Long studentId);
}
