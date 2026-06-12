package com.example.campusgrade.dto;

import com.example.campusgrade.entity.User;

public class LoginResponse {
    public String token;
    public User user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
