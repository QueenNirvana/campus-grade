package com.example.campusgrade.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CourseStats {
    public Long courseId;
    public String courseName;
    public Long count;
    public BigDecimal averageScore;
    public BigDecimal maxScore;
    public BigDecimal minScore;
    public BigDecimal passRate;
    public List<Map<String, Object>> distribution;
}
