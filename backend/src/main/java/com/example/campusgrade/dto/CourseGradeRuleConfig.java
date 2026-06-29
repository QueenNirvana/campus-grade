package com.example.campusgrade.dto;

import com.example.campusgrade.entity.CourseGradeRule;

import java.math.BigDecimal;
import java.util.List;

/**
 * 一门课程的完整成绩计算配置 DTO。
 *
 * <p>DTO（Data Transfer Object）专门用于接口传输数据。本对象把 courses 表中的
 * 两个权重和 course_grade_rules 表中的多条绩点规则组合成一次请求/响应。</p>
 */
public class CourseGradeRuleConfig {
    /** 被配置的课程主键。 */
    public Long courseId;
    /** 平时成绩占总评成绩的百分比。 */
    public BigDecimal usualWeight;
    /** 期末成绩占总评成绩的百分比。 */
    public BigDecimal finalWeight;
    /** 该课程从 0 到 100 分的绩点换算区间。 */
    public List<CourseGradeRule> rules;
}
