package com.example.campusgrade.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Course {
    public Long id;
    public String courseCode;
    public String courseName;
    public BigDecimal credit;
    public Long teacherId;
    public String teacherName;
    public String semester;
    public LocalDateTime createdAt;
}
