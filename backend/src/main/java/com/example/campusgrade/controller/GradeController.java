package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.dto.GradeSaveRequest;
import com.example.campusgrade.entity.Grade;
import com.example.campusgrade.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public ApiResponse<List<Grade>> list() {
        return ApiResponse.ok(gradeService.visibleGrades());
    }

    @PostMapping
    public ApiResponse<Grade> save(@Valid @RequestBody GradeSaveRequest request) {
        return ApiResponse.ok(gradeService.save(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Grade> update(@PathVariable Long id, @Valid @RequestBody GradeSaveRequest request) {
        request.id = id;
        return ApiResponse.ok(gradeService.save(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ApiResponse.ok(null);
    }
}
