package com.example.campusgrade.controller;

import com.example.campusgrade.common.ApiResponse;
import com.example.campusgrade.entity.User;
import com.example.campusgrade.mapper.UserMapper;
import com.example.campusgrade.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<List<User>> list() {
        return ApiResponse.ok(userService.findAll());
    }

    @PostMapping
    public ApiResponse<User> save(@RequestBody User user) {
        return ApiResponse.ok(userService.save(user));
    }

    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @RequestBody User user) {
        user.id = id;
        return ApiResponse.ok(userService.save(user));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userMapper.delete(id);
        return ApiResponse.ok(null);
    }
}
