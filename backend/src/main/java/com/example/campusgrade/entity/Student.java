package com.example.campusgrade.entity;

import java.time.LocalDateTime;

/** 学生档案实体，对应 students 表并包含关联账号和班级的展示字段。 */
public class Student {
    /** 学生档案主键。 */
    public Long id;
    /** 关联的 users 账号主键。 */
    public Long userId;
    /** 所属 classes 班级主键。 */
    public Long classId;
    /** 学号。 */
    public String studentNo;
    /** 学生姓名。 */
    public String name;
    /** 性别。 */
    public String gender;
    /** 联系电话。 */
    public String phone;
    /** 电子邮箱。 */
    public String email;
    /** 联表查询得到的班级名称。 */
    public String className;
    /** 联表查询得到的登录用户名。 */
    public String username;
    /** 档案创建时间。 */
    public LocalDateTime createdAt;
}
