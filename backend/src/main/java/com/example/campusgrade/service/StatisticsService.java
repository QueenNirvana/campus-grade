package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.dto.CourseStats;
import com.example.campusgrade.dto.OverviewStats;
import com.example.campusgrade.dto.StudentSummary;
import com.example.campusgrade.mapper.GradeMapper;
import com.example.campusgrade.security.SecurityUtils;
import org.springframework.stereotype.Service;

/** 成绩统计查询和统计权限控制服务。 */
@Service
public class StatisticsService {
    /** 执行聚合统计 SQL。 */
    private final GradeMapper gradeMapper;
    /** 获取当前学生档案 id。 */
    private final GradeService gradeService;
    /** 校验教师是否拥有待统计课程。 */
    private final CourseService courseService;

    /** 注入统计所需组件。 */
    public StatisticsService(GradeMapper gradeMapper, GradeService gradeService, CourseService courseService) {
        this.gradeMapper = gradeMapper;
        this.gradeService = gradeService;
        this.courseService = courseService;
    }

    /** 返回全系统概览；只有管理员具有该统计权限。 */
    public OverviewStats overview() {
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException("只有管理员可以查看系统概览");
        }
        return gradeMapper.overview();
    }

    /** 返回课程总体数值和分数段分布；教师只能统计自己的课程。 */
    public CourseStats courseStats(Long courseId) {
        courseService.assertTeacherOwnsCourse(courseId);
        CourseStats stats = gradeMapper.courseStats(courseId);
        // 总体统计和分数段分布来自两条 SQL，在服务层合并成同一个 DTO。
        stats.distribution = gradeMapper.courseDistribution(courseId);
        return stats;
    }

    /** 返回当前登录学生自己的成绩汇总。 */
    public StudentSummary studentSummary() {
        return gradeMapper.studentSummary(gradeService.currentStudentId());
    }
}
