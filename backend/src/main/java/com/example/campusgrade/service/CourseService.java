package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.entity.Course;
import com.example.campusgrade.entity.Teacher;
import com.example.campusgrade.mapper.CourseMapper;
import com.example.campusgrade.mapper.TeacherMapper;
import com.example.campusgrade.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 课程查询、教师课程权限和成绩权重更新服务。
 */
@Service
public class CourseService {
    /** 课程数据访问对象。 */
    private final CourseMapper courseMapper;
    /** 将登录账号关联到教师档案。 */
    private final TeacherMapper teacherMapper;

    /** 注入课程和教师 Mapper。 */
    public CourseService(CourseMapper courseMapper, TeacherMapper teacherMapper) {
        this.courseMapper = courseMapper;
        this.teacherMapper = teacherMapper;
    }

    /**
     * 按角色返回可见课程。
     *
     * <p>教师只看到本人课程；管理员和学生当前可读取全部课程基础信息。</p>
     */
    public List<Course> visibleCourses() {
        if (SecurityUtils.isTeacher()) {
            return courseMapper.findByTeacherId(currentTeacherId());
        }
        return courseMapper.findAll();
    }

    /**
     * 如果当前用户是教师，校验课程是否由该教师负责。
     *
     * <p>管理员不是教师，因此直接通过；这使同一方法可同时服务管理员和教师。</p>
     */
    public void assertTeacherOwnsCourse(Long courseId) {
        if (!SecurityUtils.isTeacher()) {
            return;
        }
        Course course = findRequiredCourse(courseId);
        if (!course.teacherId.equals(currentTeacherId())) {
            throw new BusinessException("教师只能管理本人负责课程");
        }
    }

    /** 查询必须存在的课程，不存在时抛出清晰的业务异常。 */
    public Course findRequiredCourse(Long courseId) {
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        return course;
    }

    /** 在通过课程归属校验后，只更新指定课程的两个成绩权重。 */
    public Course updateWeights(Long courseId, BigDecimal usualWeight, BigDecimal finalWeight) {
        assertTeacherOwnsCourse(courseId);
        Course course = findRequiredCourse(courseId);
        course.usualWeight = usualWeight;
        course.finalWeight = finalWeight;
        courseMapper.updateWeights(course);
        return findRequiredCourse(courseId);
    }

    /**
     * 查询当前登录教师对应的教师档案主键。
     *
     * <p>users 与 teachers 是一对一关联，但权限判断课程归属时需要 teachers.id。</p>
     */
    public Long currentTeacherId() {
        Teacher teacher = teacherMapper.findByUserId(SecurityUtils.currentUser().id);
        if (teacher == null) {
            throw new BusinessException("当前账号未关联教师档案");
        }
        return teacher.id;
    }
}
