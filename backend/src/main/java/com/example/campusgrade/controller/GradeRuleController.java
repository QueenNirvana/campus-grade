package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.dto.CourseGradeRuleConfig;
import com.example.campusgrade.service.GradeRuleManageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理某门课程专属的成绩权重和绩点换算规则。
 *
 * <p>路径中的 courseId 指明课程。管理员可管理全部课程，
 * 教师只能管理自己负责的课程，具体归属校验在服务层完成。</p>
 */
@RestController
@RequestMapping("/api/courses/{courseId}/grade-rules")
public class GradeRuleController {
    /** 负责规则读取、校验和事务保存。 */
    private final GradeRuleManageService manageService;

    /** 注入课程规则管理服务。 */
    public GradeRuleController(GradeRuleManageService manageService) {
        this.manageService = manageService;
    }

    /** 查询课程当前的平时/期末权重及全部绩点区间。 */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<CourseGradeRuleConfig> config(@PathVariable Long courseId) {
        return ApiResponse.ok(manageService.getConfig(courseId));
    }

    /** 整体保存课程权重和绩点规则，任一规则无效时整个事务都会回滚。 */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<CourseGradeRuleConfig> save(@PathVariable Long courseId, @RequestBody CourseGradeRuleConfig config) {
        return ApiResponse.ok(manageService.saveConfig(courseId, config));
    }
}
