package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.Course;
import java.util.List;

/** 课程表的数据访问接口，对应 CourseMapper.xml。 */
public interface CourseMapper {
    /** 查询全部课程及授课教师姓名。 */
    List<Course> findAll();
    /** 查询指定教师负责的课程。 */
    List<Course> findByTeacherId(Long teacherId);
    /** 按主键查询一门课程。 */
    Course findById(Long id);
    /** 新增课程并回填主键。 */
    int insert(Course item);
    /** 更新课程基础信息。 */
    int update(Course item);
    /** 只更新某课程的平时与期末成绩权重。 */
    int updateWeights(Course item);
    /** 按主键删除课程。 */
    int delete(Long id);
    /** 统计课程总数。 */
    long countAll();
}
