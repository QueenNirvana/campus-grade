package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.dto.LoginRequest;
import com.example.campusgrade.dto.LoginResponse;
import com.example.campusgrade.security.CurrentUser;
import com.example.campusgrade.security.SecurityUtils;
import com.example.campusgrade.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<CurrentUser> me() {
        return ApiResponse.ok(SecurityUtils.currentUser());
    }
}
