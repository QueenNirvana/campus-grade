package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.Student;
import java.util.List;

/** 学生档案表的数据访问接口。 */
public interface StudentMapper {
    /** 查询全部学生及其班级、账号信息。 */
    List<Student> findAll();
    /** 按学生档案主键查询。 */
    Student findById(Long id);
    /** 按登录账号主键查找对应学生档案。 */
    Student findByUserId(Long userId);
    /** 新增学生档案。 */
    int insert(Student item);
    /** 更新学生档案。 */
    int update(Student item);
    /** 删除学生档案。 */
    int delete(Long id);
    /** 统计学生总数。 */
    long countAll();
}
