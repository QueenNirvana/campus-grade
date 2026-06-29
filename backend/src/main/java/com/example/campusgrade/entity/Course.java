package com.example.campusgrade.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 课程实体，对应 courses 表，并附带联表查询得到的教师姓名。 */
public class Course {
    /** 课程主键。 */
    public Long id;
    /** 课程编号，例如 CS101。 */
    public String courseCode;
    /** 课程名称。 */
    public String courseName;
    /** 课程学分。 */
    public BigDecimal credit;
    /** 授课教师在 teachers 表中的主键。 */
    public Long teacherId;
    /** 联表查询得到的教师姓名，不直接存储在 courses 表。 */
    public String teacherName;
    /** 开课学期。 */
    public String semester;
    /** 本课程平时成绩权重，单位为百分比。 */
    public BigDecimal usualWeight;
    /** 本课程期末成绩权重，单位为百分比。 */
    public BigDecimal finalWeight;
    /** 课程创建时间。 */
    public LocalDateTime createdAt;
}
