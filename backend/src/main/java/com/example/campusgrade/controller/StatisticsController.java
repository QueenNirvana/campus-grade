package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.dto.CourseStats;
import com.example.campusgrade.dto.OverviewStats;
import com.example.campusgrade.dto.StudentSummary;
import com.example.campusgrade.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/overview")
    public ApiResponse<OverviewStats> overview() {
        return ApiResponse.ok(statisticsService.overview());
    }

    @GetMapping("/courses/{courseId}")
    public ApiResponse<CourseStats> courseStats(@PathVariable Long courseId) {
        return ApiResponse.ok(statisticsService.courseStats(courseId));
    }

    @GetMapping("/student-summary")
    public ApiResponse<StudentSummary> studentSummary() {
        return ApiResponse.ok(statisticsService.studentSummary());
    }
}
