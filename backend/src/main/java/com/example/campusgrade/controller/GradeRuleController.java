package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.CourseGradeRule;
import com.example.campusgrade.mapper.CourseGradeRuleMapper;
import com.example.campusgrade.service.GradeRuleManageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/grade-rules")
public class GradeRuleController {
    private final CourseGradeRuleMapper ruleMapper;
    private final GradeRuleManageService manageService;

    public GradeRuleController(CourseGradeRuleMapper ruleMapper, GradeRuleManageService manageService) {
        this.ruleMapper = ruleMapper;
        this.manageService = manageService;
    }

    @GetMapping
    public ApiResponse<List<CourseGradeRule>> list(@PathVariable Long courseId) {
        return ApiResponse.ok(ruleMapper.findByCourseId(courseId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<CourseGradeRule>> save(@PathVariable Long courseId, @RequestBody List<CourseGradeRule> rules) {
        return ApiResponse.ok(manageService.saveRules(courseId, rules));
    }
}
