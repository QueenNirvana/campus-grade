USE campus_grade_management;

INSERT INTO users(id, username, password, real_name, role, status) VALUES
(1, 'admin', 'admin123', '系统管理员', 'ADMIN', 1),
(2, 'teacher01', '123456', '王老师', 'TEACHER', 1),
(3, 'teacher02', '123456', '李老师', 'TEACHER', 1),
(4, 'student01', '123456', '张三', 'STUDENT', 1),
(5, 'student02', '123456', '李四', 'STUDENT', 1),
(6, 'student03', '123456', '王五', 'STUDENT', 1);

INSERT INTO classes(id, class_name, grade_year, major) VALUES
(1, '软件工程 2301 班', 2023, '软件工程'),
(2, '计算机科学 2302 班', 2023, '计算机科学与技术');

INSERT INTO teachers(id, user_id, teacher_no, name, title, phone, email) VALUES
(1, 2, 'T2023001', '王老师', '副教授', '13800000001', 'teacher01@example.edu.cn'),
(2, 3, 'T2023002', '李老师', '讲师', '13800000002', 'teacher02@example.edu.cn');

INSERT INTO students(id, user_id, class_id, student_no, name, gender, phone, email) VALUES
(1, 4, 1, 'S2023001', '张三', '男', '13900000001', 'student01@example.edu.cn'),
(2, 5, 1, 'S2023002', '李四', '女', '13900000002', 'student02@example.edu.cn'),
(3, 6, 2, 'S2023003', '王五', '男', '13900000003', 'student03@example.edu.cn');

INSERT INTO courses(id, course_code, course_name, credit, teacher_id, semester) VALUES
(1, 'MATH101', '高等数学', 4.0, 1, '2025-2026-1'),
(2, 'JAVA201', 'Java 程序设计', 3.5, 2, '2025-2026-1'),
(3, 'DB301', '数据库原理', 3.0, 1, '2025-2026-1');

INSERT INTO course_grade_rules(course_id, min_score, max_score, grade_point, label) VALUES
(1, 0, 59.99, 0.0, '不及格'),
(1, 60, 69.99, 2.0, '及格'),
(1, 70, 79.99, 3.0, '中等'),
(1, 80, 89.99, 3.7, '良好'),
(1, 90, 100, 4.0, '优秀'),
(2, 0, 59.99, 0.0, '不及格'),
(2, 60, 74.99, 2.0, '基础达标'),
(2, 75, 84.99, 2.8, '合格'),
(2, 85, 94.99, 3.3, '良好'),
(2, 95, 100, 4.0, '优秀'),
(3, 0, 59.99, 0.0, '不及格'),
(3, 60, 69.99, 2.0, '及格'),
(3, 70, 84.99, 3.2, '良好'),
(3, 85, 100, 4.0, '优秀');

INSERT INTO grades(student_id, course_id, usual_score, final_score, total_score, grade_point, remark) VALUES
(1, 1, 88, 83, 85, 3.7, '同为 85 分，高等数学绩点为 3.7'),
(1, 2, 90, 82, 85, 3.3, '同为 85 分，Java 程序设计绩点为 3.3'),
(2, 1, 76, 81, 79, 3.0, '正常'),
(2, 2, 82, 88, 86, 3.3, '正常'),
(3, 1, 58, 64, 62, 2.0, '正常'),
(3, 3, 92, 90, 91, 4.0, '正常');
