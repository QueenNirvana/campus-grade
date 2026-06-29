package com.example.campusgrade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 成绩管理系统后端的启动入口。
 *
 * <p>{@link SpringBootApplication} 会启用 Spring Boot 的自动配置和组件扫描；
 * {@link MapperScan} 会扫描指定包中的 MyBatis Mapper 接口，并为它们创建实现对象。</p>
 */
@SpringBootApplication
@MapperScan("com.example.campusgrade.mapper")
public class CampusGradeApplication {
    /**
     * Java 程序入口，启动内嵌 Web 服务器并初始化 Spring 容器。
     *
     * @param args 启动时传入的命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CampusGradeApplication.class, args);
    }
}
