package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.Teacher;
import com.example.campusgrade.mapper.TeacherMapper;
import com.example.campusgrade.security.SecurityUtils;
import com.example.campusgrade.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherMapper teacherMapper;
    private final CourseService courseService;

    public TeacherController(TeacherMapper teacherMapper, CourseService courseService) {
        this.teacherMapper = teacherMapper;
        this.courseService = courseService;
    }

    @GetMapping
    public ApiResponse<List<Teacher>> list() {
        if (SecurityUtils.isTeacher()) {
            return ApiResponse.ok(List.of(teacherMapper.findById(courseService.currentTeacherId())));
        }
        return ApiResponse.ok(teacherMapper.findAll());
    }

    @GetMapping("/me")
    public ApiResponse<Teacher> me() {
        return ApiResponse.ok(teacherMapper.findById(courseService.currentTeacherId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Teacher> save(@RequestBody Teacher item) {
        teacherMapper.insert(item);
        return ApiResponse.ok(item);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Teacher> update(@PathVariable Long id, @RequestBody Teacher item) {
        item.id = id;
        teacherMapper.update(item);
        return ApiResponse.ok(item);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        teacherMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
