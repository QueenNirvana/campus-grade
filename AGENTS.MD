# AGENTS.MD

## Project Overview

This repository is for a course design project named **高校成绩管理系统** (`Campus Grade Management System`).

The system must be built as a complete web-based full-stack application. Do not build a mobile app.

## Confirmed Goal

Build a **runnable demonstration version** suitable for course design submission and defense.

The final delivery should include:

- A runnable backend
- A runnable frontend
- MySQL schema and seed data
- Role-based login and permissions
- Course-specific grade point rule management
- Grade entry, grade query, and basic statistics
- A complete Chinese Word course design report matching the implemented system

## Required Tech Stack

- Frontend: Vue 3, Vite, Element Plus
- Frontend state/API: Pinia, Axios
- Backend: Spring Boot
- Persistence: Native MyBatis with Mapper interfaces and XML SQL
- Database: MySQL 8
- Authentication: JWT-based login
- UI style: backend/admin management system style
- Language for user-facing documentation and course report: Chinese

## Core Scope

Build the standard course-design version of the system with three roles:

- Administrator
- Teacher
- Student

Required modules:

- Login and role-based access
- User management
- Student management
- Teacher management
- Class management
- Course management
- Course-specific grade point rule management
- Grade entry and grade query
- Basic grade statistics
- Complete Chinese Word course design report

Do not add unrelated large features unless explicitly requested. The default scope excludes:

- Mobile app
- Course selection workflow
- Grade approval workflow
- Excel import/export
- Complex audit logs
- Microservices

## Important Business Rule: Grade Point Calculation

Grade point calculation is not global.

Different courses may use different grade point conversion rules. The rules are manually defined for each course by an administrator.

Implementation requirements:

- Store grade point rules per course.
- A course's rules should map score ranges to grade point values.
- When a teacher or administrator records a grade, calculate `grade_point` using that course's configured rule.
- Do not use one universal grade point table for all courses.
- The same total score may produce different grade points in different courses.

Recommended table:

- `course_grade_rules(id, course_id, min_score, max_score, grade_point, label)`

Rule validation:

- Rules for one course should cover the score range `0-100`.
- Rules for one course should not overlap.
- A course should have valid grade point rules before grades are entered for that course.

## Recommended Backend Structure

Use a simple Spring Boot monolith with clear layers:

- `controller`
- `service`
- `mapper`
- `entity`
- `dto`
- `config`
- `security`

Recommended backend modules:

- Authentication module
- User and role module
- Student module
- Teacher module
- Class module
- Course module
- Course grade rule module
- Grade module
- Statistics module

Security expectations:

- Administrators can manage all data.
- Teachers can view their own courses and manage grades only for their own courses.
- Students can view only their own information and grades.

## Recommended Frontend Structure

Use Vue 3 with Element Plus.

Recommended pages:

- Login page
- Main layout with sidebar, header, and content area

Administrator pages:

- User management
- Student management
- Teacher management
- Class management
- Course management
- Course grade rule management
- Grade management
- Statistics

Teacher pages:

- My courses
- Grade entry
- Course statistics

Student pages:

- Personal information
- My grades
- Grade summary

Use Pinia for auth/user state. Use Axios for API requests and attach JWT tokens centrally.

## Database Defaults

Provide SQL for schema creation and initial data.

Default database name:

- `campus_grade_management`

Recommended initial accounts:

- Administrator: `admin / admin123`
- Teacher example: `teacher01 / 123456`
- Student example: `student01 / 123456`

Include sample courses with different grade point rules to demonstrate that grade point calculation is course-specific.

## Documentation Requirements

The final project should include a complete Chinese Word course design report covering:

- Requirement analysis
- System architecture
- Functional module design
- Database design
- API/interface design
- Key implementation details
- Testing plan and test results
- Screenshots
- Summary

The report must match the implemented system, especially database tables, roles, pages, APIs, screenshots, and grade point rules.

## Verification Expectations

Before claiming completion, verify:

- MySQL schema and seed data can be loaded.
- Spring Boot backend starts successfully.
- Vue frontend starts successfully.
- Administrator, teacher, and student accounts can log in.
- Role-based menus and permissions work.
- Course-specific grade point rules can be configured.
- Grade point calculation uses the selected course's rules.
- Students cannot view other students' grades.
- Teachers cannot edit grades for courses they do not teach.
- Core pages have no obvious console errors.
- The Word report matches the implemented system.
