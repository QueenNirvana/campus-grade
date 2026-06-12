package com.example.campusgrade.service;

import com.example.campusgrade.entity.CourseGradeRule;
import com.example.campusgrade.mapper.CourseGradeRuleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradeRuleManageService {
    private final CourseGradeRuleMapper ruleMapper;
    private final GradeRuleService gradeRuleService;

    public GradeRuleManageService(CourseGradeRuleMapper ruleMapper, GradeRuleService gradeRuleService) {
        this.ruleMapper = ruleMapper;
        this.gradeRuleService = gradeRuleService;
    }

    @Transactional
    public List<CourseGradeRule> saveRules(Long courseId, List<CourseGradeRule> rules) {
        rules.forEach(rule -> rule.courseId = courseId);
        gradeRuleService.validateRules(rules);
        ruleMapper.deleteByCourseId(courseId);
        rules.forEach(ruleMapper::insert);
        return ruleMapper.findByCourseId(courseId);
    }
}
