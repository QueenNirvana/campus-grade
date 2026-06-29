package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.entity.CourseGradeRule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * 成绩计算规则的校验和计算服务。
 *
 * <p>该服务不访问数据库，只处理传入的数字和规则，因此核心算法集中、容易复用。
 * 每门课程传入自己的权重和规则，系统不会使用全局统一规则。</p>
 */
@Service
public class GradeRuleService {
    /** 合法成绩下界。 */
    private static final BigDecimal MIN_SCORE = BigDecimal.ZERO;
    /** 合法成绩上界。 */
    private static final BigDecimal MAX_SCORE = new BigDecimal("100");
    /** 两个连续小数区间允许的最大边界步长，例如 59.99 后接 60.00。 */
    private static final BigDecimal MAX_CONTIGUOUS_STEP = new BigDecimal("0.01");
    /** 计算百分比时使用的常量 100。 */
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * 校验一组绩点规则是否完整覆盖 0-100 分且互不重叠。
     *
     * @param rules 某一门课程的全部绩点区间
     */
    public void validateRules(List<CourseGradeRule> rules) {
        if (rules == null || rules.isEmpty()) {
            throw new BusinessException("绩点规则不能为空");
        }
        // 先按最低分排序，后续才可以依次比较相邻区间。
        List<CourseGradeRule> sortedRules = rules.stream()
                .sorted(Comparator.comparing(rule -> rule.minScore))
                .toList();

        // 第一个区间必须从 0 分开始。
        CourseGradeRule first = sortedRules.get(0);
        if (first.minScore.compareTo(MIN_SCORE) != 0) {
            throw new BusinessException("绩点规则必须从 0 分开始");
        }

        CourseGradeRule previous = null;
        for (CourseGradeRule rule : sortedRules) {
            validateSingleRule(rule);
            if (previous != null) {
                // 当前最小值小于或等于上一最大值时，两个闭区间会重叠。
                if (rule.minScore.compareTo(previous.maxScore) <= 0) {
                    throw new BusinessException("绩点规则区间不能重叠");
                }
                // 允许 59.99 到 60.00 这种 0.01 的连续小数边界，不允许更大空档。
                BigDecimal gap = rule.minScore.subtract(previous.maxScore);
                if (gap.compareTo(MAX_CONTIGUOUS_STEP) > 0) {
                    throw new BusinessException("绩点规则必须覆盖 0-100 分");
                }
            }
            previous = rule;
        }

        // 最后一个区间必须正好覆盖到 100 分。
        CourseGradeRule last = sortedRules.get(sortedRules.size() - 1);
        if (last.maxScore.compareTo(MAX_SCORE) != 0) {
            throw new BusinessException("绩点规则必须覆盖到 100 分");
        }
    }

    /** 根据某课程自己的区间规则，把总评成绩换算为绩点。 */
    public BigDecimal calculateGradePoint(List<CourseGradeRule> rules, BigDecimal score) {
        validateRules(rules);
        if (score == null || score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
            throw new BusinessException("成绩必须在 0-100 分之间");
        }
        // 区间两端都包含；规则校验已经保证任意成绩只会命中一个区间。
        return rules.stream()
                .filter(rule -> score.compareTo(rule.minScore) >= 0 && score.compareTo(rule.maxScore) <= 0)
                .findFirst()
                .map(rule -> rule.gradePoint)
                .orElseThrow(() -> new BusinessException("未找到匹配的课程绩点规则"));
    }

    /**
     * 按课程专属权重计算总评成绩。
     *
     * <p>公式：平时成绩 × 平时权重 / 100 + 期末成绩 × 期末权重 / 100。
     * 结果保留两位小数并采用四舍五入。</p>
     */
    public BigDecimal calculateTotalScore(BigDecimal usualScore, BigDecimal finalScore,
                                           BigDecimal usualWeight, BigDecimal finalWeight) {
        validateScore(usualScore, "平时成绩");
        validateScore(finalScore, "期末成绩");
        validateWeights(usualWeight, finalWeight);
        return usualScore.multiply(usualWeight)
                .add(finalScore.multiply(finalWeight))
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    /** 校验两个权重均在 0-100 内且总和正好为 100。 */
    public void validateWeights(BigDecimal usualWeight, BigDecimal finalWeight) {
        if (usualWeight == null || finalWeight == null) {
            throw new BusinessException("平时成绩占比和期末成绩占比不能为空");
        }
        if (usualWeight.compareTo(MIN_SCORE) < 0 || usualWeight.compareTo(MAX_SCORE) > 0
                || finalWeight.compareTo(MIN_SCORE) < 0 || finalWeight.compareTo(MAX_SCORE) > 0) {
            throw new BusinessException("成绩占比必须在 0-100 之间");
        }
        if (usualWeight.add(finalWeight).compareTo(ONE_HUNDRED) != 0) {
            throw new BusinessException("平时成绩占比和期末成绩占比之和必须等于 100");
        }
    }

    /** 校验单个绩点区间的必填值、范围和边界顺序。 */
    private void validateSingleRule(CourseGradeRule rule) {
        if (rule.minScore == null || rule.maxScore == null || rule.gradePoint == null) {
            throw new BusinessException("绩点规则分数和绩点不能为空");
        }
        if (rule.minScore.compareTo(MIN_SCORE) < 0 || rule.maxScore.compareTo(MAX_SCORE) > 0) {
            throw new BusinessException("绩点规则区间必须在 0-100 分之间");
        }
        if (rule.minScore.compareTo(rule.maxScore) >= 0) {
            throw new BusinessException("绩点规则最小分必须小于最大分");
        }
    }

    /** 校验一个原始成绩不为空且在 0-100 分之间。 */
    private void validateScore(BigDecimal score, String label) {
        if (score == null) {
            throw new BusinessException(label + "不能为空");
        }
        if (score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
            throw new BusinessException(label + "必须在 0-100 分之间");
        }
    }
}
