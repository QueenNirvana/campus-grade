package com.example.campusgrade.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成绩实体，对应 grades 表并包含列表查询所需的关联展示字段。
 *
 * <p>studentName、courseName 等字段由 GradeMapper 的多表 JOIN 查询得到，
 * 不会重复写入 grades 表。</p>
 */
public class Grade {
    /** 成绩记录主键。 */
    public Long id;
    /** 学生主键。 */
    public Long studentId;
    /** 课程主键。 */
    public Long courseId;
    /** 教师录入的平时成绩。 */
    public BigDecimal usualScore;
    /** 教师录入的期末成绩。 */
    public BigDecimal finalScore;
    /** 后端按课程权重计算出的总评成绩。 */
    public BigDecimal totalScore;
    /** 后端按课程专属规则换算出的绩点。 */
    public BigDecimal gradePoint;
    /** 成绩备注。 */
    public String remark;
    /** 联表得到的学号。 */
    public String studentNo;
    /** 联表得到的学生姓名。 */
    public String studentName;
    /** 联表得到的班级名称。 */
    public String className;
    /** 联表得到的课程编号。 */
    public String courseCode;
    /** 联表得到的课程名称。 */
    public String courseName;
    /** 联表得到的课程学分。 */
    public BigDecimal credit;
    /** 联表得到的授课教师主键。 */
    public Long teacherId;
    /** 联表得到的授课教师姓名。 */
    public String teacherName;
    /** 成绩创建时间。 */
    public LocalDateTime createdAt;
    /** 成绩最后更新时间。 */
    public LocalDateTime updatedAt;
}
