package com.example.campusgrade.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Grade {
    public Long id;
    public Long studentId;
    public Long courseId;
    public BigDecimal usualScore;
    public BigDecimal finalScore;
    public BigDecimal totalScore;
    public BigDecimal gradePoint;
    public String remark;
    public String studentNo;
    public String studentName;
    public String className;
    public String courseCode;
    public String courseName;
    public BigDecimal credit;
    public Long teacherId;
    public String teacherName;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
