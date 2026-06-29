package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.Teacher;
import com.example.campusgrade.mapper.TeacherMapper;
import com.example.campusgrade.security.SecurityUtils;
import com.example.campusgrade.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师档案管理接口。
 *
 * <p>教师只能看到自己的档案；管理员可查看全部教师并执行增删改。</p>
 */
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    /** 教师档案数据访问对象。 */
    private final TeacherMapper teacherMapper;
    /** 用于把登录用户映射为教师档案 id。 */
    private final CourseService courseService;

    /** 注入教师 Mapper 和课程服务。 */
    public TeacherController(TeacherMapper teacherMapper, CourseService courseService) {
        this.teacherMapper = teacherMapper;
        this.courseService = courseService;
    }

    /** 查询教师列表；教师角色只能获得本人档案。 */
    @GetMapping
    public ApiResponse<List<Teacher>> list() {
        if (SecurityUtils.isTeacher()) {
            return ApiResponse.ok(List.of(teacherMapper.findById(courseService.currentTeacherId())));
        }
        return ApiResponse.ok(teacherMapper.findAll());
    }

    /** 查询当前登录账号关联的教师档案。 */
    @GetMapping("/me")
    public ApiResponse<Teacher> me() {
        return ApiResponse.ok(teacherMapper.findById(courseService.currentTeacherId()));
    }

    /** 管理员新增教师档案。 */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Teacher> save(@RequestBody Teacher item) {
        teacherMapper.insert(item);
        return ApiResponse.ok(item);
    }

    /** 管理员修改指定教师档案。 */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Teacher> update(@PathVariable Long id, @RequestBody Teacher item) {
        // 防止请求体 id 与 URL id 不一致。
        item.id = id;
        teacherMapper.update(item);
        return ApiResponse.ok(item);
    }

    /** 管理员删除指定教师档案。 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        teacherMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
