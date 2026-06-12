package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.CourseGradeRule;
import java.util.List;

public interface CourseGradeRuleMapper {
    List<CourseGradeRule> findByCourseId(Long courseId);
    int deleteByCourseId(Long courseId);
    int insert(CourseGradeRule item);
}
