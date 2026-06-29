# 后端中文注释补充 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为后端全部源代码、配置和 MyBatis SQL 补充适合 Java Web 新手阅读的中文注释，且不改变业务逻辑。

**Architecture:** 按公共组件、安全认证、控制器、DTO/实体、Mapper、服务和资源配置分层注释。Java 使用类/字段/方法 Javadoc 与关键流程行内注释，XML/YAML/POM 使用各自合法的注释语法。

**Tech Stack:** Java 17、Spring Boot、Spring Security、JWT、MyBatis XML、MySQL、Maven

---

### Task 1: Java 入口、公共组件与安全认证

**Files:**
- Modify: `backend/src/main/java/com/example/campusgrade/CampusGradeApplication.java`
- Modify: `backend/src/main/java/com/example/campusgrade/common/*.java`
- Modify: `backend/src/main/java/com/example/campusgrade/config/*.java`
- Modify: `backend/src/main/java/com/example/campusgrade/security/*.java`

- [ ] 为每个类和公开方法增加用途、输入输出及关键认证流程说明。

### Task 2: 控制器、DTO、实体与 Mapper 接口

**Files:**
- Modify: `backend/src/main/java/com/example/campusgrade/controller/*.java`
- Modify: `backend/src/main/java/com/example/campusgrade/dto/*.java`
- Modify: `backend/src/main/java/com/example/campusgrade/entity/*.java`
- Modify: `backend/src/main/java/com/example/campusgrade/mapper/*.java`

- [ ] 解释 REST 注解、角色限制、请求/响应字段、数据库字段与接口职责。

### Task 3: 服务层业务逻辑

**Files:**
- Modify: `backend/src/main/java/com/example/campusgrade/service/*.java`

- [ ] 解释登录、权限过滤、课程权重、绩点区间、成绩计算和统计流程。

### Task 4: 构建、配置、SQL 与测试代码

**Files:**
- Modify: `backend/pom.xml`
- Modify: `backend/src/main/resources/application.yml`
- Modify: `backend/src/main/resources/mapper/*.xml`
- Modify: `backend/src/test/java/**/*.java`

- [ ] 说明依赖用途、配置项、SQL 映射关系和各测试场景。

### Task 5: 静态检查

**Files:**
- Inspect: `backend/**`

- [ ] 检查所有后端文本代码均有解释性注释。
- [ ] 运行 `git diff --check -- backend`，确认没有空白错误；按用户要求不运行测试。
