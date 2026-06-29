package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.Teacher;
import java.util.List;

/** 教师档案表的数据访问接口。 */
public interface TeacherMapper {
    /** 查询全部教师及其账号信息。 */
    List<Teacher> findAll();
    /** 按教师档案主键查询。 */
    Teacher findById(Long id);
    /** 按登录账号主键查找对应教师档案。 */
    Teacher findByUserId(Long userId);
    /** 新增教师档案。 */
    int insert(Teacher item);
    /** 更新教师档案。 */
    int update(Teacher item);
    /** 删除教师档案。 */
    int delete(Long id);
    /** 统计教师总数。 */
    long countAll();
}
