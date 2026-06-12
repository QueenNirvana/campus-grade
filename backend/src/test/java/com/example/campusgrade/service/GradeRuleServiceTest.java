package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.entity.CourseGradeRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GradeRuleServiceTest {
    private final GradeRuleService service = new GradeRuleService();

    @Test
    void acceptsRulesCoveringZeroToOneHundredWithoutOverlap() {
        List<CourseGradeRule> rules = List.of(
                new CourseGradeRule(1L, 0, 59.99, 0, "不及格"),
                new CourseGradeRule(1L, 60, 79.99, 3.0, "中等"),
                new CourseGradeRule(1L, 80, 100, 4.0, "优秀")
        );

        assertDoesNotThrow(() -> service.validateRules(rules));
    }

    @Test
    void rejectsOverlappingRules() {
        List<CourseGradeRule> rules = List.of(
                new CourseGradeRule(1L, 0, 60, 0, "不及格"),
                new CourseGradeRule(1L, 60, 100, 4.0, "优秀")
        );

        assertThrows(BusinessException.class, () -> service.validateRules(rules));
    }

    @Test
    void rejectsRulesWithScoreGap() {
        List<CourseGradeRule> rules = List.of(
                new CourseGradeRule(1L, 0, 59.99, 0, "不及格"),
                new CourseGradeRule(1L, 70, 100, 4.0, "优秀")
        );

        assertThrows(BusinessException.class, () -> service.validateRules(rules));
    }

    @Test
    void calculatesGradePointFromCourseSpecificRules() {
        List<CourseGradeRule> mathRules = List.of(
                new CourseGradeRule(1L, 0, 79.99, 2.0, "基础"),
                new CourseGradeRule(1L, 80, 89.99, 3.7, "良好"),
                new CourseGradeRule(1L, 90, 100, 4.0, "优秀")
        );
        List<CourseGradeRule> javaRules = List.of(
                new CourseGradeRule(2L, 0, 84.99, 2.8, "合格"),
                new CourseGradeRule(2L, 85, 94.99, 3.3, "良好"),
                new CourseGradeRule(2L, 95, 100, 4.0, "优秀")
        );

        assertEquals(new BigDecimal("3.7"), service.calculateGradePoint(mathRules, new BigDecimal("85")));
        assertEquals(new BigDecimal("3.3"), service.calculateGradePoint(javaRules, new BigDecimal("85")));
    }
}
