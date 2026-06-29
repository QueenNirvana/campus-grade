package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.CourseGradeRule;
import java.util.List;

/** 课程绩点换算规则表的数据访问接口。 */
public interface CourseGradeRuleMapper {
    /** 按课程查询规则，并按最低分升序排列。 */
    List<CourseGradeRule> findByCourseId(Long courseId);
    /** 删除某课程的全部旧规则，便于整体替换。 */
    int deleteByCourseId(Long courseId);
    /** 插入一条绩点区间规则。 */
    int insert(CourseGradeRule item);
}
