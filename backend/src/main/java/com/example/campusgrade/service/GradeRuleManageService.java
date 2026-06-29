package com.example.campusgrade.service;

import com.example.campusgrade.dto.CourseGradeRuleConfig;
import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.entity.Course;
import com.example.campusgrade.entity.CourseGradeRule;
import com.example.campusgrade.mapper.CourseGradeRuleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程成绩规则的读取和整体保存服务。
 *
 * <p>它负责协调 CourseService、GradeRuleService 和 Mapper：
 * 先校验权限与规则，再同时保存课程权重和绩点区间。</p>
 */
@Service
public class GradeRuleManageService {
    /** 读写课程绩点区间。 */
    private final CourseGradeRuleMapper ruleMapper;
    /** 校验权重和绩点区间是否有效。 */
    private final GradeRuleService gradeRuleService;
    /** 查询课程并校验教师课程归属。 */
    private final CourseService courseService;

    /** 注入规则管理依赖。 */
    public GradeRuleManageService(CourseGradeRuleMapper ruleMapper, GradeRuleService gradeRuleService,
                                  CourseService courseService) {
        this.ruleMapper = ruleMapper;
        this.gradeRuleService = gradeRuleService;
        this.courseService = courseService;
    }

    /** 把课程表中的权重和规则表中的区间组装成一个配置 DTO。 */
    public CourseGradeRuleConfig getConfig(Long courseId) {
        // 教师只能读取自己课程的规则；管理员直接通过。
        courseService.assertTeacherOwnsCourse(courseId);
        Course course = courseService.findRequiredCourse(courseId);
        CourseGradeRuleConfig config = new CourseGradeRuleConfig();
        config.courseId = course.id;
        config.usualWeight = course.usualWeight;
        config.finalWeight = course.finalWeight;
        config.rules = ruleMapper.findByCourseId(courseId);
        return config;
    }

    /**
     * 整体替换一门课程的绩点规则。
     *
     * <p>@Transactional 保证“删除旧规则 + 插入新规则”是一个整体；
     * 中途失败时数据库会回滚，不会留下半套规则。</p>
     */
    @Transactional
    public List<CourseGradeRule> saveRules(Long courseId, List<CourseGradeRule> rules) {
        // 必须在删除旧数据前完成校验。
        gradeRuleService.validateRules(rules);
        // courseId 来自 URL，统一覆盖前端传入值，防止规则被写到其他课程。
        rules.forEach(rule -> rule.courseId = courseId);
        ruleMapper.deleteByCourseId(courseId);
        rules.forEach(ruleMapper::insert);
        return ruleMapper.findByCourseId(courseId);
    }

    /** 在同一事务中保存课程权重和全部绩点规则。 */
    @Transactional
    public CourseGradeRuleConfig saveConfig(Long courseId, CourseGradeRuleConfig config) {
        if (config == null) {
            throw new BusinessException("课程成绩规则不能为空");
        }
        // 先校验再写数据库，避免保存无效权重。
        gradeRuleService.validateWeights(config.usualWeight, config.finalWeight);
        courseService.updateWeights(courseId, config.usualWeight, config.finalWeight);
        saveRules(courseId, config.rules);
        return getConfig(courseId);
    }
}
