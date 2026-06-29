package com.example.campusgrade.entity;

import java.time.LocalDateTime;

/** 教师档案实体，对应 teachers 表并包含关联账号的用户名。 */
public class Teacher {
    /** 教师档案主键。 */
    public Long id;
    /** 关联的 users 账号主键。 */
    public Long userId;
    /** 教工号。 */
    public String teacherNo;
    /** 教师姓名。 */
    public String name;
    /** 职称。 */
    public String title;
    /** 联系电话。 */
    public String phone;
    /** 电子邮箱。 */
    public String email;
    /** 联表查询得到的登录用户名。 */
    public String username;
    /** 档案创建时间。 */
    public LocalDateTime createdAt;
}
