package com.example.campusgrade.entity;

import java.time.LocalDateTime;

/** 系统登录账号实体，对应 users 表。 */
public class User {
    /** 用户主键。 */
    public Long id;
    /** 唯一登录用户名。 */
    public String username;
    /** 密码；数据库中通常保存 BCrypt 哈希，接口响应前会被清空。 */
    public String password;
    /** 用户真实姓名。 */
    public String realName;
    /** 角色代码：ADMIN、TEACHER 或 STUDENT。 */
    public String role;
    /** 账号状态：1 表示启用，其他值表示不可登录。 */
    public Integer status;
    /** 账号创建时间。 */
    public LocalDateTime createdAt;
    /** 账号最后更新时间。 */
    public LocalDateTime updatedAt;
}
