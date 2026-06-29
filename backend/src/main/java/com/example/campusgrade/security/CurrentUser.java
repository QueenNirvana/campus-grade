package com.example.campusgrade.security;

/**
 * 当前登录用户的简化信息。
 *
 * <p>认证成功后，该对象会作为 principal 放入 Spring Security 上下文。
 * 它不包含密码等敏感字段，业务层可安全地读取当前用户身份和角色。</p>
 */
public class CurrentUser {
    /** users 表中的用户主键。 */
    public Long id;
    /** 登录用户名。 */
    public String username;
    /** 用户真实姓名。 */
    public String realName;
    /** 角色代码：ADMIN、TEACHER 或 STUDENT。 */
    public String role;

    /** 创建一份当前登录用户信息。 */
    public CurrentUser(Long id, String username, String realName, String role) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.role = role;
    }
}
