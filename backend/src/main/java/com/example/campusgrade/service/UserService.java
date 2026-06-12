package com.example.campusgrade.service;

import com.example.campusgrade.entity.User;
import com.example.campusgrade.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        List<User> users = userMapper.findAll();
        users.forEach(user -> user.password = null);
        return users;
    }

    public User save(User user) {
        if (user.status == null) {
            user.status = 1;
        }
        if (user.id == null) {
            user.password = passwordEncoder.encode(user.password == null || user.password.isBlank() ? "123456" : user.password);
            userMapper.insert(user);
        } else {
            if (user.password != null && !user.password.isBlank() && !user.password.startsWith("$2")) {
                user.password = passwordEncoder.encode(user.password);
            }
            userMapper.update(user);
        }
        user.password = null;
        return user;
    }
}
