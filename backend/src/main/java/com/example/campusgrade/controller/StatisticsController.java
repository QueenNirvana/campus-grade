package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.dto.CourseStats;
import com.example.campusgrade.dto.OverviewStats;
import com.example.campusgrade.dto.StudentSummary;
import com.example.campusgrade.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

/**
 * 成绩统计接口。
 *
 * <p>三个接口分别面向管理员概览、课程统计和学生个人汇总，
 * 角色与数据范围由 StatisticsService 统一校验。</p>
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    /** 统计查询服务。 */
    private final StatisticsService statisticsService;

    /** 注入统计服务。 */
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /** 查询全系统数量和平均分概览，仅管理员可得到结果。 */
    @GetMapping("/overview")
    public ApiResponse<OverviewStats> overview() {
        return ApiResponse.ok(statisticsService.overview());
    }

    /** 查询某门课程的平均分、最高分、最低分、及格率和分数分布。 */
    @GetMapping("/courses/{courseId}")
    public ApiResponse<CourseStats> courseStats(@PathVariable Long courseId) {
        return ApiResponse.ok(statisticsService.courseStats(courseId));
    }

    /** 查询当前学生自己的课程数、通过数、学分和平均成绩。 */
    @GetMapping("/student-summary")
    public ApiResponse<StudentSummary> studentSummary() {
        return ApiResponse.ok(statisticsService.studentSummary());
    }
}
