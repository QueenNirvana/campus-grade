package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.Course;
import com.example.campusgrade.mapper.CourseMapper;
import com.example.campusgrade.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseMapper courseMapper;
    private final CourseService courseService;

    public CourseController(CourseMapper courseMapper, CourseService courseService) {
        this.courseMapper = courseMapper;
        this.courseService = courseService;
    }

    @GetMapping
    public ApiResponse<List<Course>> list() {
        return ApiResponse.ok(courseService.visibleCourses());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Course> save(@RequestBody Course item) {
        courseMapper.insert(item);
        return ApiResponse.ok(item);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Course> update(@PathVariable Long id, @RequestBody Course item) {
        item.id = id;
        courseMapper.update(item);
        return ApiResponse.ok(item);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
