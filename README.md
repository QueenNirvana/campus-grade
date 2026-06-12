# 高校成绩管理系统

本项目是课程设计《高校成绩管理系统》的可运行演示版，采用 Spring Boot + 原生 MyBatis XML + MySQL 8 + Vue 3 + Element Plus 实现。

## 技术栈

- 后端：Spring Boot 3、Spring Security、JWT、MyBatis Mapper 接口 + XML SQL
- 前端：Vue 3、Vite、Element Plus、Pinia、Axios
- 数据库：MySQL 8
- 报告：`docs/report/高校成绩管理系统课程设计报告.docx`

## 目录说明

- `backend/`：Spring Boot 后端
- `frontend/`：Vue 3 前端
- `database/schema.sql`：建库建表脚本
- `database/seed.sql`：演示数据
- `docs/report/`：课程设计报告和界面参考图

## 数据库初始化

默认数据库名：`campus_grade_management`

PowerShell 示例：

```powershell
Get-Content -Raw database/schema.sql | mysql -uroot -p你的密码
Get-Content -Raw database/seed.sql | mysql -uroot -p你的密码 campus_grade_management
```

如果你的 MySQL 用户名或密码不是 `root/root`，请修改 `backend/src/main/resources/application.yml`，或用环境变量覆盖：

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="你的密码"
```

## 启动后端

```powershell
cd backend
mvn spring-boot:run
```

默认地址：`http://localhost:8080`

## 启动前端

```powershell
cd frontend
npm install
npm run dev -- --host 127.0.0.1
```

默认地址：`http://127.0.0.1:5173`

## 演示账号

| 角色 | 用户名 | 密码 |
|---|---|---|
| 管理员 | admin | admin123 |
| 教师 | teacher01 | 123456 |
| 学生 | student01 | 123456 |

## 核心演示点

本系统的绩点规则不是全局统一表，而是按课程配置。

种子数据中：

- 高等数学：85 分对应绩点 3.7
- Java 程序设计：85 分对应绩点 3.3

录入或修改成绩时，后端会根据课程 ID 查询 `course_grade_rules`，自动计算并保存 `grades.grade_point`。

## 权限说明

- 管理员：管理用户、学生、教师、班级、课程、绩点规则、成绩和统计。
- 教师：只能查看本人课程，只能录入和修改本人课程成绩。
- 学生：只能查看本人档案、本人课程成绩和成绩汇总。

## 已执行验证

- `backend`: `mvn test` 通过，包含课程绩点规则覆盖、重叠、缺口和课程级计算测试。
- `frontend`: `npm run build` 通过。
- MySQL 脚本已提供；本机 root 密码未知时需要按实际密码导入。

## 云服务器部署准备

部署交接说明见：

- `DEPLOYMENT_HANDOFF.md`
- `deploy/`

其中包含 Nginx 配置模板、systemd 服务模板、后端环境变量示例、数据库导入脚本和下一个会话需要确认的服务器信息清单。
