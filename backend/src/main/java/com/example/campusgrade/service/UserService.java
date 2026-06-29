package com.example.campusgrade.service;

import com.example.campusgrade.entity.User;
import com.example.campusgrade.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统账号业务服务，主要负责密码加密和响应脱敏。
 */
@Service
public class UserService {
    /** 用户表数据访问对象。 */
    private final UserMapper userMapper;
    /** BCrypt 密码编码器。 */
    private final PasswordEncoder passwordEncoder;

    /** 注入用户 Mapper 和密码编码器。 */
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /** 查询全部用户，并在返回控制器前清除每个用户的密码。 */
    public List<User> findAll() {
        List<User> users = userMapper.findAll();
        // 即使是哈希密码也属于敏感数据，不应通过接口返回。
        users.forEach(user -> user.password = null);
        return users;
    }

    /**
     * 新增或修改用户。
     *
     * <p>id 为空表示新增，此时空密码使用默认值 123456；
     * id 非空表示修改，仅在前端提交新密码时重新加密。</p>
     */
    public User save(User user) {
        if (user.status == null) {
            // 前端未指定状态时，默认创建为启用账号。
            user.status = 1;
        }
        if (user.id == null) {
            // 数据库只保存 BCrypt 哈希，不直接保存用户输入的原始密码。
            user.password = passwordEncoder.encode(user.password == null || user.password.isBlank() ? "123456" : user.password);
            userMapper.insert(user);
        } else {
            // 已经以 $2 开头的 BCrypt 值不重复加密，空值则由 XML 保留旧密码。
            if (user.password != null && !user.password.isBlank() && !user.password.startsWith("$2")) {
                user.password = passwordEncoder.encode(user.password);
            }
            userMapper.update(user);
        }
        // 保存后的响应同样不能包含密码。
        user.password = null;
        return user;
    }
}
