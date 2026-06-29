package com.example.campusgrade.dto;

import jakarta.validation.constraints.NotBlank;

/** 登录接口接收的用户名和密码。 */
public class LoginRequest {
    /** 登录用户名；@NotBlank 同时拒绝 null、空串和纯空格。 */
    @NotBlank(message = "用户名不能为空")
    public String username;
    /** 登录密码。 */
    @NotBlank(message = "密码不能为空")
    public String password;
}
