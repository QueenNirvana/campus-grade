package com.example.campusgrade.service;

import com.example.campusgrade.common.BusinessException;
import com.example.campusgrade.entity.CourseGradeRule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class GradeRuleService {
    private static final BigDecimal MIN_SCORE = BigDecimal.ZERO;
    private static final BigDecimal MAX_SCORE = new BigDecimal("100");
    private static final BigDecimal MAX_CONTIGUOUS_STEP = new BigDecimal("0.01");

    public void validateRules(List<CourseGradeRule> rules) {
        if (rules == null || rules.isEmpty()) {
            throw new BusinessException("绩点规则不能为空");
        }
        List<CourseGradeRule> sortedRules = rules.stream()
                .sorted(Comparator.comparing(rule -> rule.minScore))
                .toList();

        CourseGradeRule first = sortedRules.get(0);
        if (first.minScore.compareTo(MIN_SCORE) != 0) {
            throw new BusinessException("绩点规则必须从 0 分开始");
        }

        CourseGradeRule previous = null;
        for (CourseGradeRule rule : sortedRules) {
            validateSingleRule(rule);
            if (previous != null) {
                if (rule.minScore.compareTo(previous.maxScore) <= 0) {
                    throw new BusinessException("绩点规则区间不能重叠");
                }
                BigDecimal gap = rule.minScore.subtract(previous.maxScore);
                if (gap.compareTo(MAX_CONTIGUOUS_STEP) > 0) {
                    throw new BusinessException("绩点规则必须覆盖 0-100 分");
                }
            }
            previous = rule;
        }

        CourseGradeRule last = sortedRules.get(sortedRules.size() - 1);
        if (last.maxScore.compareTo(MAX_SCORE) != 0) {
            throw new BusinessException("绩点规则必须覆盖到 100 分");
        }
    }

    public BigDecimal calculateGradePoint(List<CourseGradeRule> rules, BigDecimal score) {
        validateRules(rules);
        if (score == null || score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
            throw new BusinessException("成绩必须在 0-100 分之间");
        }
        return rules.stream()
                .filter(rule -> score.compareTo(rule.minScore) >= 0 && score.compareTo(rule.maxScore) <= 0)
                .findFirst()
                .map(rule -> rule.gradePoint)
                .orElseThrow(() -> new BusinessException("未找到匹配的课程绩点规则"));
    }

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
}
