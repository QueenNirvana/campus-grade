DROP DATABASE IF EXISTS campus_grade_management;
CREATE DATABASE campus_grade_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_grade_management;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(120) NOT NULL,
  real_name VARCHAR(50) NOT NULL,
  role VARCHAR(20) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE classes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  class_name VARCHAR(80) NOT NULL,
  grade_year INT NOT NULL,
  major VARCHAR(80) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE students (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  class_id BIGINT NOT NULL,
  student_no VARCHAR(30) NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL,
  gender VARCHAR(10),
  phone VARCHAR(30),
  email VARCHAR(80),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_students_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_students_class FOREIGN KEY (class_id) REFERENCES classes(id)
);

CREATE TABLE teachers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  teacher_no VARCHAR(30) NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL,
  title VARCHAR(50),
  phone VARCHAR(30),
  email VARCHAR(80),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_teachers_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE courses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_code VARCHAR(30) NOT NULL UNIQUE,
  course_name VARCHAR(100) NOT NULL,
  credit DECIMAL(4,1) NOT NULL,
  teacher_id BIGINT NOT NULL,
  semester VARCHAR(30) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_courses_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

CREATE TABLE course_grade_rules (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id BIGINT NOT NULL,
  min_score DECIMAL(5,2) NOT NULL,
  max_score DECIMAL(5,2) NOT NULL,
  grade_point DECIMAL(3,1) NOT NULL,
  label VARCHAR(50) NOT NULL,
  CONSTRAINT fk_rules_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
  INDEX idx_rules_course_score (course_id, min_score, max_score)
);

CREATE TABLE grades (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  usual_score DECIMAL(5,2) NOT NULL,
  final_score DECIMAL(5,2) NOT NULL,
  total_score DECIMAL(5,2) NOT NULL,
  grade_point DECIMAL(3,1) NOT NULL,
  remark VARCHAR(200),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_grades_student FOREIGN KEY (student_id) REFERENCES students(id),
  CONSTRAINT fk_grades_course FOREIGN KEY (course_id) REFERENCES courses(id),
  CONSTRAINT uk_grades_student_course UNIQUE (student_id, course_id),
  INDEX idx_grades_course (course_id),
  INDEX idx_grades_student (student_id)
);
