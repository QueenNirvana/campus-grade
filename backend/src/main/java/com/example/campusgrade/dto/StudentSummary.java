package com.example.campusgrade.dto;

import java.math.BigDecimal;

/** 当前学生个人成绩汇总 DTO。 */
public class StudentSummary {
    /** 已有成绩记录的课程数。 */
    public Long courseCount;
    /** 总评成绩达到 60 分的课程数。 */
    public Long passedCount;
    /** 已统计课程的学分总和。 */
    public BigDecimal totalCredit;
    /** 各门课程总评成绩的平均值。 */
    public BigDecimal averageScore;
    /** 各门课程绩点的平均值。 */
    public BigDecimal averageGradePoint;
}
