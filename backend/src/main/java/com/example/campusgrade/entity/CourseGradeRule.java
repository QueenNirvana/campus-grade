package com.example.campusgrade.entity;

import java.math.BigDecimal;

/** 单个课程绩点换算区间，对应 course_grade_rules 表。 */
public class CourseGradeRule {
    /** 规则主键。 */
    public Long id;
    /** 规则所属课程主键。 */
    public Long courseId;
    /** 区间最低分，匹配时包含该边界。 */
    public BigDecimal minScore;
    /** 区间最高分，匹配时包含该边界。 */
    public BigDecimal maxScore;
    /** 分数落入此区间时得到的绩点。 */
    public BigDecimal gradePoint;
    /** 区间文字标签，例如“优秀”或“不及格”。 */
    public String label;

    /** 无参构造方法，供 MyBatis 和 JSON 反序列化创建对象。 */
    public CourseGradeRule() {
    }

    /** 便于初始化示例数据和测试数据的构造方法。 */
    public CourseGradeRule(Long courseId, double minScore, double maxScore, double gradePoint, String label) {
        this.courseId = courseId;
        // valueOf 比直接 new BigDecimal(double) 更能避免二进制浮点误差。
        this.minScore = BigDecimal.valueOf(minScore);
        this.maxScore = BigDecimal.valueOf(maxScore);
        this.gradePoint = BigDecimal.valueOf(gradePoint);
        this.label = label;
    }
}
