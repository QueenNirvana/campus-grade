package com.example.campusgrade.dto;

import com.example.campusgrade.entity.User;

/** 登录成功后返回给前端的数据。 */
public class LoginResponse {
    /** 后续请求需要放入 Authorization 请求头的 JWT。 */
    public String token;
    /** 当前用户基本信息，密码字段已在服务层清空。 */
    public User user;

    /** 同时封装令牌和用户信息。 */
    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
