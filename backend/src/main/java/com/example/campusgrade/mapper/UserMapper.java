package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.User;
import java.util.List;

public interface UserMapper {
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);
    int insert(User user);
    int update(User user);
    int delete(Long id);
    long countAll();
}
