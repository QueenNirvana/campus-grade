package com.example.campusgrade.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    public String username;
    @NotBlank(message = "密码不能为空")
    public String password;
}
