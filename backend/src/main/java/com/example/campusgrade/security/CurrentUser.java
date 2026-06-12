package com.example.campusgrade.security;

public class CurrentUser {
    public Long id;
    public String username;
    public String realName;
    public String role;

    public CurrentUser(Long id, String username, String realName, String role) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.role = role;
    }
}
