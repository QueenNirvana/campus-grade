package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.User;
import com.example.campusgrade.mapper.UserMapper;
import com.example.campusgrade.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统登录账号管理接口。
 *
 * <p>类级别的 @PreAuthorize 表示本控制器所有接口都只允许管理员访问。
 * 用户密码的默认值、加密和响应脱敏由 UserService 处理。</p>
 */
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    /** 负责账号保存、密码加密和敏感字段清理。 */
    private final UserService userService;
    /** 删除账号时直接使用的用户 Mapper。 */
    private final UserMapper userMapper;

    /** 注入用户服务和 Mapper。 */
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /** 查询全部账号，返回前密码字段会被清空。 */
    @GetMapping
    public ApiResponse<List<User>> list() {
        return ApiResponse.ok(userService.findAll());
    }

    /** 新增系统账号。 */
    @PostMapping
    public ApiResponse<User> save(@RequestBody User user) {
        return ApiResponse.ok(userService.save(user));
    }

    /** 修改指定系统账号。 */
    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @RequestBody User user) {
        // URL 中的 id 决定要更新哪条记录。
        user.id = id;
        return ApiResponse.ok(userService.save(user));
    }

    /** 根据主键删除系统账号。 */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
