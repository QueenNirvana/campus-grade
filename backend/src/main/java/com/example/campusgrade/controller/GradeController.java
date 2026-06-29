package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.dto.GradeSaveRequest;
import com.example.campusgrade.entity.Grade;
import com.example.campusgrade.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成绩录入与查询接口。
 *
 * <p>总评成绩和绩点不由控制器接收后直接保存，而是在 GradeService 中根据
 * 课程自己的权重与绩点区间计算，并同时执行教师课程权限校验。</p>
 */
@RestController
@RequestMapping("/api/grades")
public class GradeController {
    /** 成绩相关的计算、权限和持久化逻辑。 */
    private final GradeService gradeService;

    /** 注入成绩服务。 */
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    /** 按当前角色查询可见成绩。 */
    @GetMapping
    public ApiResponse<List<Grade>> list() {
        return ApiResponse.ok(gradeService.visibleGrades());
    }

    /** 新增成绩；@Valid 会校验学生、课程、平时成绩和期末成绩不能为空。 */
    @PostMapping
    public ApiResponse<Grade> save(@Valid @RequestBody GradeSaveRequest request) {
        return ApiResponse.ok(gradeService.save(request));
    }

    /** 修改指定成绩，仍由后端重新计算总评成绩和绩点。 */
    @PutMapping("/{id}")
    public ApiResponse<Grade> update(@PathVariable Long id, @Valid @RequestBody GradeSaveRequest request) {
        // 将路径中的记录 id 写入请求对象，使服务层走更新分支。
        request.id = id;
        return ApiResponse.ok(gradeService.save(request));
    }

    /** 删除成绩；学生会在服务层被拒绝，教师只能删除本人课程的成绩。 */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ApiResponse.ok(null);
    }
}
