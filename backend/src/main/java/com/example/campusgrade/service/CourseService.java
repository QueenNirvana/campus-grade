package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.entity.Course;
import com.example.campusgrade.entity.Teacher;
import com.example.campusgrade.mapper.CourseMapper;
import com.example.campusgrade.mapper.TeacherMapper;
import com.example.campusgrade.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;

    public CourseService(CourseMapper courseMapper, TeacherMapper teacherMapper) {
        this.courseMapper = courseMapper;
        this.teacherMapper = teacherMapper;
    }

    public List<Course> visibleCourses() {
        if (SecurityUtils.isTeacher()) {
            return courseMapper.findByTeacherId(currentTeacherId());
        }
        return courseMapper.findAll();
    }

    public void assertTeacherOwnsCourse(Long courseId) {
        if (!SecurityUtils.isTeacher()) {
            return;
        }
        Course course = courseMapper.findById(courseId);
        if (course == null || !course.teacherId.equals(currentTeacherId())) {
            throw new BusinessException("教师只能管理本人负责课程");
        }
    }

    public Long currentTeacherId() {
        Teacher teacher = teacherMapper.findByUserId(SecurityUtils.currentUser().id);
        if (teacher == null) {
            throw new BusinessException("当前账号未关联教师档案");
        }
        return teacher.id;
    }
}
