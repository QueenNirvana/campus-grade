package com.example.campusgrade.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 某门课程的统计结果 DTO。 */
public class CourseStats {
    /** 课程主键。 */
    public Long courseId;
    /** 课程名称。 */
    public String courseName;
    /** 已录入的成绩数量。 */
    public Long count;
    /** 总评平均分。 */
    public BigDecimal averageScore;
    /** 总评最高分。 */
    public BigDecimal maxScore;
    /** 总评最低分。 */
    public BigDecimal minScore;
    /** 总评达到 60 分的百分比。 */
    public BigDecimal passRate;
    /** 各分数段的名称和人数，例如 80-89 分共 3 人。 */
    public List<Map<String, Object>> distribution;
}
