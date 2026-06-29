package com.example.campusgrade.entity;

import java.time.LocalDateTime;

/**
 * 班级实体，对应数据库 classes 表。
 *
 * <p>字段使用 public 是本课程项目的简化写法，MyBatis 可直接进行属性映射。</p>
 */
public class ClassInfo {
    /** 班级主键。 */
    public Long id;
    /** 班级名称。 */
    public String className;
    /** 入学或年级年份。 */
    public Integer gradeYear;
    /** 专业名称。 */
    public String major;
    /** 数据创建时间。 */
    public LocalDateTime createdAt;
}
