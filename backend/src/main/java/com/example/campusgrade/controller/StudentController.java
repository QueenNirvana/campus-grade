package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.Student;
import com.example.campusgrade.mapper.StudentMapper;
import com.example.campusgrade.security.SecurityUtils;
import com.example.campusgrade.service.GradeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生档案管理接口。
 *
 * <p>学生只能查看自己的档案；教师和管理员可以查询学生列表；
 * 档案的新增、修改和删除仅管理员可执行。</p>
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {
    /** 学生档案数据访问对象。 */
    private final StudentMapper studentMapper;
    /** 用于把当前登录用户映射为学生档案 id。 */
    private final GradeService gradeService;

    /** 注入学生 Mapper 和成绩服务。 */
    public StudentController(StudentMapper studentMapper, GradeService gradeService) {
        this.studentMapper = studentMapper;
        this.gradeService = gradeService;
    }

    /** 查询学生列表；学生角色的结果会被限制为本人一条记录。 */
    @GetMapping
    public ApiResponse<List<Student>> list() {
        if (SecurityUtils.isStudent()) {
            return ApiResponse.ok(List.of(studentMapper.findById(gradeService.currentStudentId())));
        }
        return ApiResponse.ok(studentMapper.findAll());
    }

    /** 查询当前登录账号关联的学生档案。 */
    @GetMapping("/me")
    public ApiResponse<Student> me() {
        return ApiResponse.ok(studentMapper.findById(gradeService.currentStudentId()));
    }

    /** 管理员新增学生档案。 */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Student> save(@RequestBody Student item) {
        studentMapper.insert(item);
        return ApiResponse.ok(item);
    }

    /** 管理员修改指定学生档案。 */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Student> update(@PathVariable Long id, @RequestBody Student item) {
        // 始终采用路径中的主键。
        item.id = id;
        studentMapper.update(item);
        return ApiResponse.ok(item);
    }

    /** 管理员删除指定学生档案。 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        studentMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
