package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.User;
import java.util.List;

/** 系统用户表的数据访问接口。 */
public interface UserMapper {
    /** 查询全部用户。 */
    List<User> findAll();
    /** 按主键查询用户。 */
    User findById(Long id);
    /** 按唯一用户名查询用户，登录流程会调用此方法。 */
    User findByUsername(String username);
    /** 新增用户账号。 */
    int insert(User user);
    /** 更新用户资料；密码为空时 XML 会保留原密码。 */
    int update(User user);
    /** 按主键删除用户。 */
    int delete(Long id);
    /** 统计用户总数。 */
    long countAll();
}
