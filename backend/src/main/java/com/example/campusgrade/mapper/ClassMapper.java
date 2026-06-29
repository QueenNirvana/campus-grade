package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.ClassInfo;
import java.util.List;

/**
 * 班级表的数据访问接口。
 *
 * <p>方法名必须与 ClassMapper.xml 中 SQL 标签的 id 一致，
 * MyBatis 会在运行时生成本接口的实现类。</p>
 */
public interface ClassMapper {
    /** 查询全部班级。 */
    List<ClassInfo> findAll();
    /** 按主键查询班级。 */
    ClassInfo findById(Long id);
    /** 新增班级并回填生成的 id。 */
    int insert(ClassInfo item);
    /** 更新班级，返回受影响行数。 */
    int update(ClassInfo item);
    /** 按主键删除班级。 */
    int delete(Long id);
}
