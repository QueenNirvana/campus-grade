package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.dto.GradeSaveRequest;
import com.example.campusgrade.entity.Course;
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

/**
 * 成绩模块的核心业务服务。
 *
 * <p>负责按角色限制可见数据、阻止学生修改成绩、校验教师课程归属，
 * 并使用课程自己的权重和绩点规则计算后再保存成绩。</p>
 */
@Service
public class GradeService {
    /** 成绩数据访问对象。 */
    private final GradeMapper gradeMapper;
    /** 查询登录账号关联的学生档案。 */
    private final StudentMapper studentMapper;
    /** 查询课程专属绩点规则。 */
    private final CourseGradeRuleMapper ruleMapper;
    /** 执行成绩和绩点的校验、计算。 */
    private final GradeRuleService gradeRuleService;
    /** 查询课程权重并校验教师是否拥有课程。 */
    private final CourseService courseService;

    /** 注入成绩业务所需的 Mapper 和服务。 */
    public GradeService(GradeMapper gradeMapper, StudentMapper studentMapper, CourseGradeRuleMapper ruleMapper,
                        GradeRuleService gradeRuleService, CourseService courseService) {
        this.gradeMapper = gradeMapper;
        this.studentMapper = studentMapper;
        this.ruleMapper = ruleMapper;
        this.gradeRuleService = gradeRuleService;
        this.courseService = courseService;
    }

    /**
     * 按登录角色返回可见成绩。
     *
     * <p>学生只看本人，教师只看本人授课课程，管理员查看全部。</p>
     */
    public List<Grade> visibleGrades() {
        if (SecurityUtils.isStudent()) {
            return gradeMapper.findByStudentId(currentStudentId());
        }
        if (SecurityUtils.isTeacher()) {
            return gradeMapper.findByTeacherId(courseService.currentTeacherId());
        }
        return gradeMapper.findAll();
    }

    /**
     * 新增或修改成绩。
     *
     * <p>request.id 为空时新增，否则修改。无论哪种情况，totalScore 和 gradePoint
     * 都由后端重新计算，绝不直接采用前端传值。</p>
     */
    public Grade save(GradeSaveRequest request) {
        // 学生只能查询，不能通过直接调用接口篡改成绩。
        if (SecurityUtils.isStudent()) {
            throw new BusinessException("学生不能录入或修改成绩");
        }
        // 教师只能操作自己负责的课程；管理员会直接通过此检查。
        courseService.assertTeacherOwnsCourse(request.courseId);
        Course course = courseService.findRequiredCourse(request.courseId);
        List<CourseGradeRule> rules = ruleMapper.findByCourseId(request.courseId);
        // 第一步：使用当前课程自己的平时/期末权重计算总评。
        BigDecimal totalScore = gradeRuleService.calculateTotalScore(
                request.usualScore,
                request.finalScore,
                course.usualWeight,
                course.finalWeight
        );
        // 第二步：使用同一课程自己的分数区间规则换算绩点。
        BigDecimal gradePoint = gradeRuleService.calculateGradePoint(rules, totalScore);
        // 将请求字段与后端计算字段组合为持久化实体。
        Grade grade = new Grade();
        grade.id = request.id;
        grade.studentId = request.studentId;
        grade.courseId = request.courseId;
        grade.usualScore = request.usualScore;
        grade.finalScore = request.finalScore;
        grade.totalScore = totalScore;
        grade.gradePoint = gradePoint;
        grade.remark = request.remark;
        if (grade.id == null) {
            // 没有主键表示创建新成绩，数据库生成的 id 会回填到 grade。
            gradeMapper.insert(grade);
        } else {
            // 修改前先确认旧记录存在，并校验教师也拥有旧记录原来的课程。
            Grade old = gradeMapper.findById(grade.id);
            if (old == null) {
                throw new BusinessException("成绩记录不存在");
            }
            courseService.assertTeacherOwnsCourse(old.courseId);
            gradeMapper.update(grade);
        }
        // 重新查询以返回包含学生、课程、教师名称等关联字段的完整结果。
        return gradeMapper.findById(grade.id);
    }

    /** 删除成绩，并执行与修改相同的角色及课程归属校验。 */
    public void delete(Long id) {
        if (SecurityUtils.isStudent()) {
            throw new BusinessException("学生不能删除成绩");
        }
        Grade grade = gradeMapper.findById(id);
        if (grade == null) {
            // 删除不存在的记录按无操作处理，接口仍可正常返回。
            return;
        }
        courseService.assertTeacherOwnsCourse(grade.courseId);
        gradeMapper.delete(id);
    }

    /**
     * 获取当前账号所关联的学生档案主键。
     *
     * <p>users.id 是登录账号主键，而成绩表使用 students.id，
     * 因此需要先通过 user_id 完成一次映射。</p>
     */
    public Long currentStudentId() {
        Student student = studentMapper.findByUserId(SecurityUtils.currentUser().id);
        if (student == null) {
            throw new BusinessException("当前账号未关联学生档案");
        }
        return student.id;
    }
}
