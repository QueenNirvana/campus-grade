package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.dto.GradeSaveRequest;
import com.example.campusgrade.entity.CourseGradeRule;
import com.example.campusgrade.entity.Grade;
import com.example.campusgrade.entity.Student;
import com.example.campusgrade.mapper.CourseGradeRuleMapper;
import com.example.campusgrade.mapper.GradeMapper;
import com.example.campusgrade.mapper.StudentMapper;
import com.example.campusgrade.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GradeService {
    private final GradeMapper gradeMapper;
    private final StudentMapper studentMapper;
    private final CourseGradeRuleMapper ruleMapper;
    private final GradeRuleService gradeRuleService;
    private final CourseService courseService;

    public GradeService(GradeMapper gradeMapper, StudentMapper studentMapper, CourseGradeRuleMapper ruleMapper,
                        GradeRuleService gradeRuleService, CourseService courseService) {
        this.gradeMapper = gradeMapper;
        this.studentMapper = studentMapper;
        this.ruleMapper = ruleMapper;
        this.gradeRuleService = gradeRuleService;
        this.courseService = courseService;
    }

    public List<Grade> visibleGrades() {
        if (SecurityUtils.isStudent()) {
            return gradeMapper.findByStudentId(currentStudentId());
        }
        if (SecurityUtils.isTeacher()) {
            return gradeMapper.findByTeacherId(courseService.currentTeacherId());
        }
        return gradeMapper.findAll();
    }

    public Grade save(GradeSaveRequest request) {
        if (SecurityUtils.isStudent()) {
            throw new BusinessException("学生不能录入或修改成绩");
        }
        courseService.assertTeacherOwnsCourse(request.courseId);
        List<CourseGradeRule> rules = ruleMapper.findByCourseId(request.courseId);
        BigDecimal gradePoint = gradeRuleService.calculateGradePoint(rules, request.totalScore);
        Grade grade = new Grade();
        grade.id = request.id;
        grade.studentId = request.studentId;
        grade.courseId = request.courseId;
        grade.usualScore = request.usualScore;
        grade.finalScore = request.finalScore;
        grade.totalScore = request.totalScore;
        grade.gradePoint = gradePoint;
        grade.remark = request.remark;
        if (grade.id == null) {
            gradeMapper.insert(grade);
        } else {
            Grade old = gradeMapper.findById(grade.id);
            if (old == null) {
                throw new BusinessException("成绩记录不存在");
            }
            courseService.assertTeacherOwnsCourse(old.courseId);
            gradeMapper.update(grade);
        }
        return gradeMapper.findById(grade.id);
    }

    public void delete(Long id) {
        if (SecurityUtils.isStudent()) {
            throw new BusinessException("学生不能删除成绩");
        }
        Grade grade = gradeMapper.findById(id);
        if (grade == null) {
            return;
        }
        courseService.assertTeacherOwnsCourse(grade.courseId);
        gradeMapper.delete(id);
    }

    public Long currentStudentId() {
        Student student = studentMapper.findByUserId(SecurityUtils.currentUser().id);
        if (student == null) {
            throw new BusinessException("当前账号未关联学生档案");
        }
        return student.id;
    }
}
