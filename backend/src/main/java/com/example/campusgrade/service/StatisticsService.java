package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.dto.CourseStats;
import com.example.campusgrade.dto.OverviewStats;
import com.example.campusgrade.dto.StudentSummary;
import com.example.campusgrade.mapper.GradeMapper;
import com.example.campusgrade.security.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    private final GradeMapper gradeMapper;
    private final GradeService gradeService;
    private final CourseService courseService;

    public StatisticsService(GradeMapper gradeMapper, GradeService gradeService, CourseService courseService) {
        this.gradeMapper = gradeMapper;
        this.gradeService = gradeService;
        this.courseService = courseService;
    }

    public OverviewStats overview() {
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException("只有管理员可以查看系统概览");
        }
        return gradeMapper.overview();
    }

    public CourseStats courseStats(Long courseId) {
        courseService.assertTeacherOwnsCourse(courseId);
        CourseStats stats = gradeMapper.courseStats(courseId);
        stats.distribution = gradeMapper.courseDistribution(courseId);
        return stats;
    }

    public StudentSummary studentSummary() {
        return gradeMapper.studentSummary(gradeService.currentStudentId());
    }
}
