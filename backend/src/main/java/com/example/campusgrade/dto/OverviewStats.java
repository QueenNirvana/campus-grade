package com.example.campusgrade.dto;

import java.math.BigDecimal;

/** 管理员首页使用的全系统概览统计 DTO。 */
public class OverviewStats {
    /** 系统账号总数。 */
    public long userCount;
    /** 学生档案总数。 */
    public long studentCount;
    /** 教师档案总数。 */
    public long teacherCount;
    /** 课程总数。 */
    public long courseCount;
    /** 成绩记录总数。 */
    public long gradeCount;
    /** 全部成绩记录的总评平均分。 */
    public BigDecimal averageScore;
}
