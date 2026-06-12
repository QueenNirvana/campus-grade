package com.example.campusgrade.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class GradeSaveRequest {
    public Long id;
    @NotNull(message = "学生不能为空")
    public Long studentId;
    @NotNull(message = "课程不能为空")
    public Long courseId;
    @NotNull(message = "平时成绩不能为空")
    public BigDecimal usualScore;
    @NotNull(message = "期末成绩不能为空")
    public BigDecimal finalScore;
    @NotNull(message = "总评成绩不能为空")
    public BigDecimal totalScore;
    public String remark;
}
