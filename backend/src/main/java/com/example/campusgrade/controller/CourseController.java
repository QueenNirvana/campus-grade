package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.Course;
import com.example.campusgrade.mapper.CourseMapper;
import com.example.campusgrade.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程管理接口。
 *
 * <p>查询时由服务层按照当前角色过滤课程；课程基础信息的增删改只允许管理员执行。
 * 课程成绩权重和绩点规则由 GradeRuleController 单独管理。</p>
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    /** 负责课程表的基础增删改。 */
    private final CourseMapper courseMapper;
    /** 负责角色可见范围和课程业务校验。 */
    private final CourseService courseService;

    /** 注入课程 Mapper 和服务。 */
    public CourseController(CourseMapper courseMapper, CourseService courseService) {
        this.courseMapper = courseMapper;
        this.courseService = courseService;
    }

    /** 查询当前用户可见的课程：教师只看到本人课程，其他角色看到全部。 */
    @GetMapping
    public ApiResponse<List<Course>> list() {
        return ApiResponse.ok(courseService.visibleCourses());
    }

    /** 管理员新增课程。 */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Course> save(@RequestBody Course item) {
        courseMapper.insert(item);
        return ApiResponse.ok(item);
    }

    /** 管理员根据课程 id 修改课程。 */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Course> update(@PathVariable Long id, @RequestBody Course item) {
        // 主键以 URL 路径参数为准。
        item.id = id;
        courseMapper.update(item);
        return ApiResponse.ok(item);
    }

    /** 管理员删除指定课程。 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
