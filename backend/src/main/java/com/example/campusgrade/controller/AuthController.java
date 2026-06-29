package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.dto.LoginRequest;
import com.example.campusgrade.dto.LoginResponse;
import com.example.campusgrade.security.CurrentUser;
import com.example.campusgrade.security.SecurityUtils;
import com.example.campusgrade.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 登录认证相关接口。
 *
 * <p>类上的 {@code @RequestMapping} 定义统一前缀 /api/auth，
 * 方法上的 GetMapping/PostMapping 再补充具体路径。</p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /** 登录业务由服务层处理，控制器只负责接收和返回 HTTP 数据。 */
    private final AuthService authService;

    /** 通过构造器注入认证服务。 */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** 校验用户名和密码，成功后返回 JWT 与用户信息。 */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    /** 返回 JWT 所代表的当前登录用户，常用于前端刷新登录状态。 */
    @GetMapping("/me")
    public ApiResponse<CurrentUser> me() {
        return ApiResponse.ok(SecurityUtils.currentUser());
    }
}
