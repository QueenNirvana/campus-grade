package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.dto.LoginRequest;
import com.example.campusgrade.dto.LoginResponse;
import com.example.campusgrade.entity.User;
import com.example.campusgrade.mapper.UserMapper;
import com.example.campusgrade.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.username);
        if (user == null || user.status == null || user.status != 1) {
            throw new BusinessException("账号不存在或已停用");
        }
        if (!passwordMatches(request.password, user.password)) {
            throw new BusinessException("用户名或密码错误");
        }
        user.password = null;
        return new LoginResponse(jwtTokenProvider.createToken(user), user);
    }

    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (storedPassword != null && storedPassword.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }
        return rawPassword != null && rawPassword.equals(storedPassword);
    }
}
