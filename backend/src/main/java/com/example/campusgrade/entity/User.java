package com.example.campusgrade.entity;

import java.time.LocalDateTime;

public class User {
    public Long id;
    public String username;
    public String password;
    public String realName;
    public String role;
    public Integer status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
