package com.example.campusgrade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.campusgrade.mapper")
public class CampusGradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusGradeApplication.class, args);
    }
}
