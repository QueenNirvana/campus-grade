package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.ClassInfo;
import com.example.campusgrade.mapper.ClassMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班级信息的 REST 控制器。
 *
 * <p>所有已登录角色都能查询班级；新增、修改和删除只允许管理员操作。</p>
 */
@RestController
@RequestMapping("/api/classes")
public class ClassController {
    /** 该模块逻辑简单，控制器直接调用班级 Mapper 完成数据库操作。 */
    private final ClassMapper classMapper;

    /** 注入班级数据访问对象。 */
    public ClassController(ClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    /** 查询全部班级。 */
    @GetMapping
    public ApiResponse<List<ClassInfo>> list() {
        return ApiResponse.ok(classMapper.findAll());
    }

    /** 新增班级；@PreAuthorize 在进入方法前检查管理员角色。 */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassInfo> save(@RequestBody ClassInfo item) {
        classMapper.insert(item);
        return ApiResponse.ok(item);
    }

    /** 根据 URL 中的 id 修改班级，并以路径 id 为最终主键。 */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassInfo> update(@PathVariable Long id, @RequestBody ClassInfo item) {
        // 不信任请求体中的 id，统一使用路径参数，避免修改错记录。
        item.id = id;
        classMapper.update(item);
        return ApiResponse.ok(item);
    }

    /** 根据主键删除班级。 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        classMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
