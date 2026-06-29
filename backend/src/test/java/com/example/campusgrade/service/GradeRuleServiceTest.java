package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.entity.CourseGradeRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * GradeRuleService 的单元测试示例。
 *
 * <p>这些测试不连接数据库，只验证课程规则校验和分数计算算法。
 * 每个带 @Test 的方法都是一个可独立运行的测试场景。</p>
 */
class GradeRuleServiceTest {
    /** 被测试对象没有外部依赖，可以直接 new。 */
    private final GradeRuleService service = new GradeRuleService();

    /** 完整覆盖 0-100 且不重叠的连续区间应通过校验。 */
    @Test
    void acceptsRulesCoveringZeroToOneHundredWithoutOverlap() {
        // Arrange：准备一套合法规则。
        List<CourseGradeRule> rules = List.of(
                new CourseGradeRule(1L, 0, 59.99, 0, "不及格"),
                new CourseGradeRule(1L, 60, 79.99, 3.0, "中等"),
                new CourseGradeRule(1L, 80, 100, 4.0, "优秀")
        );

        // Act + Assert：执行校验时不应抛出异常。
        assertDoesNotThrow(() -> service.validateRules(rules));
    }

    /** 两个闭区间都包含 60 分时属于重叠，应抛出业务异常。 */
    @Test
    void rejectsOverlappingRules() {
        List<CourseGradeRule> rules = List.of(
                new CourseGradeRule(1L, 0, 60, 0, "不及格"),
                new CourseGradeRule(1L, 60, 100, 4.0, "优秀")
        );

        assertThrows(BusinessException.class, () -> service.validateRules(rules));
    }

    /** 59.99 后直接从 70 开始会留下成绩空档，应拒绝保存。 */
    @Test
    void rejectsRulesWithScoreGap() {
        List<CourseGradeRule> rules = List.of(
                new CourseGradeRule(1L, 0, 59.99, 0, "不及格"),
                new CourseGradeRule(1L, 70, 100, 4.0, "优秀")
        );

        assertThrows(BusinessException.class, () -> service.validateRules(rules));
    }

    /** 相同的 85 分应根据不同课程规则得到不同绩点。 */
    @Test
    void calculatesGradePointFromCourseSpecificRules() {
        // 课程 1 的 85 分落入 80-89.99 区间，绩点为 3.7。
        List<CourseGradeRule> mathRules = List.of(
                new CourseGradeRule(1L, 0, 79.99, 2.0, "基础"),
                new CourseGradeRule(1L, 80, 89.99, 3.7, "良好"),
                new CourseGradeRule(1L, 90, 100, 4.0, "优秀")
        );
        // 课程 2 的 85 分落入 85-94.99 区间，绩点为 3.3。
        List<CourseGradeRule> javaRules = List.of(
                new CourseGradeRule(2L, 0, 84.99, 2.8, "合格"),
                new CourseGradeRule(2L, 85, 94.99, 3.3, "良好"),
                new CourseGradeRule(2L, 95, 100, 4.0, "优秀")
        );

        assertEquals(new BigDecimal("3.7"), service.calculateGradePoint(mathRules, new BigDecimal("85")));
        assertEquals(new BigDecimal("3.3"), service.calculateGradePoint(javaRules, new BigDecimal("85")));
    }

    /** 验证 30% 平时成绩与 70% 期末成绩得到 87.00 分。 */
    @Test
    void calculatesTotalScoreFromCourseWeights() {
        BigDecimal totalScore = service.calculateTotalScore(
                new BigDecimal("80"),
                new BigDecimal("90"),
                new BigDecimal("30"),
                new BigDecimal("70")
        );

        assertEquals(0, new BigDecimal("87.00").compareTo(totalScore));
    }

    /** 两个权重之和只有 90 时应被规则校验拒绝。 */
    @Test
    void rejectsWeightsThatDoNotAddToOneHundred() {
        assertThrows(BusinessException.class, () -> service.validateWeights(
                new BigDecimal("40"),
                new BigDecimal("50")
        ));
    }
}
