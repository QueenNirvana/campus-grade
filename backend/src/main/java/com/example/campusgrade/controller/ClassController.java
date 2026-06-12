package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.ClassInfo;
import com.example.campusgrade.mapper.ClassMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    private final ClassMapper classMapper;

    public ClassController(ClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    @GetMapping
    public ApiResponse<List<ClassInfo>> list() {
        return ApiResponse.ok(classMapper.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassInfo> save(@RequestBody ClassInfo item) {
        classMapper.insert(item);
        return ApiResponse.ok(item);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassInfo> update(@PathVariable Long id, @RequestBody ClassInfo item) {
        item.id = id;
        classMapper.update(item);
        return ApiResponse.ok(item);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        classMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
