package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.dto.LoginRequest;
import com.example.campusgrade.dto.LoginResponse;
import com.example.campusgrade.entity.User;
import com.example.campusgrade.mapper.UserMapper;
import com.example.campusgrade.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 登录认证服务。
 *
 * <p>服务层负责账号状态校验、密码比对和 JWT 签发，
 * 控制器无需了解密码格式或令牌生成细节。</p>
 */
@Service
public class AuthService {
    /** 查询登录账号。 */
    private final UserMapper userMapper;
    /** 登录成功后签发 JWT。 */
    private final JwtTokenProvider jwtTokenProvider;
    /** 比对 BCrypt 加密密码。 */
    private final PasswordEncoder passwordEncoder;

    /** 使用构造器注入登录所需组件。 */
    public AuthService(UserMapper userMapper, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 执行登录。
     *
     * @param request 前端提交的用户名和原始密码
     * @return JWT 和已隐藏密码的用户信息
     */
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.username);
        // 账号不存在和账号停用使用同一条提示，减少无意义的账号信息泄露。
        if (user == null || user.status == null || user.status != 1) {
            throw new BusinessException("账号不存在或已停用");
        }
        if (!passwordMatches(request.password, user.password)) {
            throw new BusinessException("用户名或密码错误");
        }
        // 密码无论是明文还是哈希都不应返回给前端。
        user.password = null;
        return new LoginResponse(jwtTokenProvider.createToken(user), user);
    }

    /**
     * 兼容 BCrypt 哈希密码和演示数据中的旧明文密码。
     *
     * <p>正式生产系统应只保存哈希密码；这里保留明文分支是为了兼容课程演示数据。</p>
     */
    private boolean passwordMatches(String rawPassword, String storedPassword) {
        // BCrypt 哈希通常以 $2 开头。
        if (storedPassword != null && storedPassword.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }
        return rawPassword != null && rawPassword.equals(storedPassword);
    }
}
