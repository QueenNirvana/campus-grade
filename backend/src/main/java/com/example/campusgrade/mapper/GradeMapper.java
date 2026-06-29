package com.example.campusgrade.mapper;

import com.example.campusgrade.dto.CourseStats;
import com.example.campusgrade.dto.OverviewStats;
import com.example.campusgrade.dto.StudentSummary;
import com.example.campusgrade.entity.Grade;
import java.util.List;
import java.util.Map;

/** 成绩明细和成绩统计的数据访问接口。 */
public interface GradeMapper {
    /** 查询全部成绩及学生、班级、课程和教师展示信息。 */
    List<Grade> findAll();
    /** 查询某位教师所授课程的成绩。 */
    List<Grade> findByTeacherId(Long teacherId);
    /** 查询某位学生自己的成绩。 */
    List<Grade> findByStudentId(Long studentId);
    /** 按主键查询单条成绩。 */
    Grade findById(Long id);
    /** 插入后端已计算完总评和绩点的成绩。 */
    int insert(Grade item);
    /** 更新成绩记录。 */
    int update(Grade item);
    /** 按主键删除成绩。 */
    int delete(Long id);
    /** 统计成绩记录总数。 */
    long countAll();
    /** 查询管理员首页的全系统概览。 */
    OverviewStats overview();
    /** 查询课程总体统计值。 */
    CourseStats courseStats(Long courseId);
    /** 查询课程各分数段人数。 */
    List<Map<String, Object>> courseDistribution(Long courseId);
    /** 查询学生个人成绩汇总。 */
    StudentSummary studentSummary(Long studentId);
}
