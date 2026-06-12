# Campus Grade Management Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a runnable course-design demonstration of “高校成绩管理系统” with backend, frontend, MySQL schema/seed data, role permissions, course-specific grade point rules, statistics, and a matching Chinese Word report.

**Architecture:** A Spring Boot monolith exposes JWT-protected `/api` endpoints backed by MyBatis mapper interfaces and XML SQL. A Vue 3 + Vite + Element Plus frontend consumes those APIs with Pinia auth state and role-based routes. SQL scripts and the Word report are first-class deliverables under `database/` and `docs/report/`.

**Tech Stack:** Spring Boot 3, Java 17, MyBatis XML, MySQL 8, JWT, Vue 3, Vite, Element Plus, Pinia, Axios, python-docx for report generation.

---

## File Structure

- Create `backend/pom.xml`: Spring Boot dependencies and build config.
- Create `backend/src/main/java/com/example/campusgrade/...`: backend packages for controller, service, mapper, entity, dto, config, security.
- Create `backend/src/main/resources/application.yml`: database, MyBatis, JWT configuration.
- Create `backend/src/main/resources/mapper/*.xml`: native MyBatis SQL.
- Create `backend/src/test/java/com/example/campusgrade/...`: unit tests for grade rule validation and grade permissions.
- Create `database/schema.sql`: MySQL schema.
- Create `database/seed.sql`: demonstration accounts, classes, students, teachers, courses, grade rules, grades.
- Create `frontend/package.json`, `frontend/vite.config.js`, `frontend/src/...`: Vue app shell, role routes, API modules, pages.
- Create `docs/report/build_report.py`: deterministic Chinese DOCX generator.
- Create `docs/report/高校成绩管理系统课程设计报告.docx`: final Word report.
- Create `README.md`: Chinese run instructions and test accounts.

## Task 1: Database Schema And Seed Data

**Files:**
- Create: `database/schema.sql`
- Create: `database/seed.sql`

- [ ] **Step 1: Write schema for core tables**

Define `users`, `classes`, `students`, `teachers`, `courses`, `course_grade_rules`, and `grades`. Add foreign keys, unique keys, and indexes for role filtering.

- [ ] **Step 2: Write seed data**

Insert `admin/admin123`, `teacher01/123456`, `student01/123456`, sample classes, teachers, students, courses, grade rules, and grades.

- [ ] **Step 3: Verify SQL syntax locally where MySQL is available**

Run:

```powershell
mysql -u root -p < database/schema.sql
mysql -u root -p campus_grade_management < database/seed.sql
```

Expected: both scripts load without errors. If local MySQL is unavailable, verify the scripts structurally and document the limitation.

## Task 2: Backend Scaffold And Core Domain

**Files:**
- Create: `backend/pom.xml`
- Create: `backend/src/main/java/com/example/campusgrade/CampusGradeApplication.java`
- Create: `backend/src/main/java/com/example/campusgrade/entity/*.java`
- Create: `backend/src/main/java/com/example/campusgrade/dto/*.java`
- Create: `backend/src/main/resources/application.yml`

- [ ] **Step 1: Scaffold Spring Boot project**

Use Spring Boot Web, Security, Validation, MyBatis, MySQL driver, JWT, Lombok, and test dependencies.

- [ ] **Step 2: Add common API response and exception handling**

Implement `ApiResponse<T>`, `BusinessException`, and `GlobalExceptionHandler`.

- [ ] **Step 3: Add entity and DTO classes**

Represent users, classes, students, teachers, courses, course grade rules, grades, login request/response, statistics DTOs, and save requests.

## Task 3: Backend Tests First For Grade Rules

**Files:**
- Create: `backend/src/test/java/com/example/campusgrade/service/GradeRuleServiceTest.java`
- Create: `backend/src/main/java/com/example/campusgrade/service/GradeRuleService.java`

- [ ] **Step 1: Write failing tests**

Test that valid rules cover `0-100`, overlapping ranges fail, gaps fail, and scores map to the course-specific rule.

- [ ] **Step 2: Run tests and verify red**

Run:

```powershell
cd backend
mvn test -Dtest=GradeRuleServiceTest
```

Expected: tests fail because `GradeRuleService` behavior is not implemented.

- [ ] **Step 3: Implement rule validation and point calculation**

Implement validation and score-to-rule lookup.

- [ ] **Step 4: Run tests and verify green**

Run:

```powershell
cd backend
mvn test -Dtest=GradeRuleServiceTest
```

Expected: tests pass.

## Task 4: Backend Security, Mappers, Services, Controllers

**Files:**
- Create: `backend/src/main/java/com/example/campusgrade/security/*.java`
- Create: `backend/src/main/java/com/example/campusgrade/config/*.java`
- Create: `backend/src/main/java/com/example/campusgrade/mapper/*.java`
- Create: `backend/src/main/resources/mapper/*.xml`
- Create: `backend/src/main/java/com/example/campusgrade/service/*.java`
- Create: `backend/src/main/java/com/example/campusgrade/controller/*.java`

- [ ] **Step 1: Implement JWT security**

Add password hashing, login, JWT generation, JWT filter, and role annotations.

- [ ] **Step 2: Implement MyBatis mapper interfaces and XML SQL**

Add CRUD and role-filtered query SQL for users, classes, students, teachers, courses, rules, grades, and statistics.

- [ ] **Step 3: Implement services**

Add business logic for role filtering, teacher course ownership checks, student self-access checks, rule validation, and grade point calculation.

- [ ] **Step 4: Implement controllers**

Expose `/api/auth`, `/api/users`, `/api/classes`, `/api/students`, `/api/teachers`, `/api/courses`, `/api/courses/{courseId}/grade-rules`, `/api/grades`, and `/api/statistics`.

- [ ] **Step 5: Run backend tests**

Run:

```powershell
cd backend
mvn test
```

Expected: tests pass.

## Task 5: Frontend Scaffold And Role-Based App Shell

**Files:**
- Create: `frontend/package.json`
- Create: `frontend/index.html`
- Create: `frontend/vite.config.js`
- Create: `frontend/src/main.js`
- Create: `frontend/src/App.vue`
- Create: `frontend/src/router/index.js`
- Create: `frontend/src/stores/auth.js`
- Create: `frontend/src/utils/request.js`
- Create: `frontend/src/layout/MainLayout.vue`
- Create: `frontend/src/styles.css`

- [ ] **Step 1: Scaffold Vue 3 app**

Install Vue 3, Vite, Element Plus, Pinia, Axios, Vue Router, and lucide-vue-next.

- [ ] **Step 2: Implement auth store and Axios interceptor**

Persist JWT and attach `Authorization: Bearer <token>` centrally.

- [ ] **Step 3: Implement role-based router and layout**

Build login page, dark sidebar, header, and role-specific menus.

- [ ] **Step 4: Run frontend build**

Run:

```powershell
cd frontend
npm install
npm run build
```

Expected: Vite build succeeds.

## Task 6: Frontend Feature Pages

**Files:**
- Create: `frontend/src/api/*.js`
- Create: `frontend/src/views/LoginView.vue`
- Create: `frontend/src/views/admin/*.vue`
- Create: `frontend/src/views/teacher/*.vue`
- Create: `frontend/src/views/student/*.vue`
- Create: `frontend/src/components/*.vue`

- [ ] **Step 1: Implement API modules**

Add typed endpoint wrappers for auth, users, classes, students, teachers, courses, grade rules, grades, and statistics.

- [ ] **Step 2: Implement administrator pages**

Add management tables and dialogs for users, students, teachers, classes, courses, rules, grades, and statistics.

- [ ] **Step 3: Implement teacher pages**

Add my courses, grade entry, and course statistics.

- [ ] **Step 4: Implement student pages**

Add personal information, my grades, and grade summary.

- [ ] **Step 5: Verify browser workflows**

Run:

```powershell
cd frontend
npm run dev -- --host 127.0.0.1
```

Expected: login and role pages render without console errors.

## Task 7: Documentation And Word Report

**Files:**
- Create: `README.md`
- Create: `docs/report/build_report.py`
- Create: `docs/report/高校成绩管理系统课程设计报告.docx`

- [ ] **Step 1: Write Chinese README**

Include prerequisites, database setup, backend start, frontend start, and demo accounts.

- [ ] **Step 2: Generate Chinese DOCX**

Use bundled Python dependencies and `python-docx` to create a report covering requirements, architecture, modules, database, APIs, key implementation, tests, screenshots placeholders or captured screenshots, and summary.

- [ ] **Step 3: Render and inspect DOCX**

Run the Documents skill renderer. If LibreOffice is unavailable, perform structural checks and document the limitation.

## Task 8: End-To-End Verification

**Files:**
- Modify: `README.md`

- [ ] **Step 1: Start backend**

Run:

```powershell
cd backend
mvn spring-boot:run
```

Expected: backend starts on `http://localhost:8080`.

- [ ] **Step 2: Start frontend**

Run:

```powershell
cd frontend
npm run dev -- --host 127.0.0.1
```

Expected: frontend starts on a local Vite URL.

- [ ] **Step 3: Verify required demonstrations**

Check login for admin, teacher, and student; role menus; course rule configuration; grade point calculation; student data isolation; teacher course ownership restriction; statistics pages.

- [ ] **Step 4: Commit implementation**

Run:

```powershell
git add .
git commit -m "feat: implement campus grade management demo"
```
