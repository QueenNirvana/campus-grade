package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.Student;
import com.example.campusgrade.mapper.StudentMapper;
import com.example.campusgrade.security.SecurityUtils;
import com.example.campusgrade.service.GradeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentMapper studentMapper;
    private final GradeService gradeService;

    public StudentController(StudentMapper studentMapper, GradeService gradeService) {
        this.studentMapper = studentMapper;
        this.gradeService = gradeService;
    }

    @GetMapping
    public ApiResponse<List<Student>> list() {
        if (SecurityUtils.isStudent()) {
            return ApiResponse.ok(List.of(studentMapper.findById(gradeService.currentStudentId())));
        }
        return ApiResponse.ok(studentMapper.findAll());
    }

    @GetMapping("/me")
    public ApiResponse<Student> me() {
        return ApiResponse.ok(studentMapper.findById(gradeService.currentStudentId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Student> save(@RequestBody Student item) {
        studentMapper.insert(item);
        return ApiResponse.ok(item);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Student> update(@PathVariable Long id, @RequestBody Student item) {
        item.id = id;
        studentMapper.update(item);
        return ApiResponse.ok(item);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        studentMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
