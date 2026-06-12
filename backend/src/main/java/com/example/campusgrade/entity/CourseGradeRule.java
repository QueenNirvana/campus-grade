package com.example.campusgrade.entity;

import java.math.BigDecimal;

public class CourseGradeRule {
    public Long id;
    public Long courseId;
    public BigDecimal minScore;
    public BigDecimal maxScore;
    public BigDecimal gradePoint;
    public String label;

    public CourseGradeRule() {
    }

    public CourseGradeRule(Long courseId, double minScore, double maxScore, double gradePoint, String label) {
        this.courseId = courseId;
        this.minScore = BigDecimal.valueOf(minScore);
        this.maxScore = BigDecimal.valueOf(maxScore);
        this.gradePoint = BigDecimal.valueOf(gradePoint);
        this.label = label;
    }
}
