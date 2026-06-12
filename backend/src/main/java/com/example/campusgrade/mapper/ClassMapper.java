package com.example.campusgrade.mapper;

import com.example.campusgrade.entity.ClassInfo;
import java.util.List;

public interface ClassMapper {
    List<ClassInfo> findAll();
    ClassInfo findById(Long id);
    int insert(ClassInfo item);
    int update(ClassInfo item);
    int delete(Long id);
}
