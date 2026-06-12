# 云服务器部署交接说明

本文件用于交给下一个 Codex 会话继续完成云服务器部署。

## 当前项目状态

- 项目名称：高校成绩管理系统
- 后端：Spring Boot 3 + Spring Security + JWT + MyBatis XML
- 前端：Vue 3 + Vite + Element Plus + Pinia + Axios
- 数据库：MySQL 8
- 当前主要提交：
  - `9e1b1b3 docs: expand java course design report`
  - `0e213e6 feat: implement campus grade management demo`
- 前端接口配置：Axios 使用 `/api`，适合通过 Nginx 反向代理到后端。
- 后端数据库配置：支持环境变量 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD`。

## 本地已知问题

本地登录失败的根因不是账号错误，也不是 `UserMapper.xml` 错误，而是后端没有拿到正确的 MySQL 连接配置。

报错链路：

```text
POST /api/auth/login
UserMapper.findByUsername
CannotGetJdbcConnectionException: Failed to obtain JDBC Connection
```

原因：默认配置使用 `root/root`，但本机 MySQL 不接受该密码。云服务器部署时必须显式配置数据库账号和密码。

## 推荐云服务器部署架构

```text
Internet
  |
  v
Nginx :80/:443
  |-- /          -> Vue dist 静态文件
  |-- /api/...   -> http://127.0.0.1:8080/api/...

Spring Boot :8080
  |
  v
MySQL 8 :3306
```

## 需要下一个会话向用户确认的信息

```text
服务器 IPv4：
服务器系统：Ubuntu / Debian / CentOS / 宝塔 / 其他
SSH 用户：
SSH 端口：
SSH 密码或密钥是否可用：
是否有域名：
是否需要 HTTPS：
MySQL 是否已安装：
MySQL root 密码或可用数据库账号：
```

## 建议服务器目录

```text
/opt/campus-grade-management/
  backend/
    campus-grade-management-0.0.1-SNAPSHOT.jar
  .env

/var/www/campus-grade-management/
  index.html
  assets/

/etc/nginx/conf.d/campus-grade-management.conf
/etc/systemd/system/campus-grade-management.service
```

## 部署前本地打包命令

后端：

```powershell
cd backend
mvn clean package
```

生成：

```text
backend/target/campus-grade-management-0.0.1-SNAPSHOT.jar
```

前端：

```powershell
cd frontend
npm install
npm run build
```

生成：

```text
frontend/dist/
```

## 云服务器数据库初始化

建议在服务器上创建数据库和专用用户：

```sql
CREATE DATABASE IF NOT EXISTS campus_grade_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'campus_grade'@'localhost' IDENTIFIED BY '请替换为强密码';
GRANT ALL PRIVILEGES ON campus_grade_management.* TO 'campus_grade'@'localhost';
FLUSH PRIVILEGES;
```

注意：当前 `database/schema.sql` 会先 `DROP DATABASE IF EXISTS campus_grade_management`，适合首次部署或重置演示数据。生产保留数据时不要直接执行该脚本。

首次演示部署可执行：

```bash
mysql -uroot -p < database/schema.sql
mysql -uroot -p campus_grade_management < database/seed.sql
```

## 后端环境变量

服务器上建议创建：

```text
/opt/campus-grade-management/.env
```

内容参考：

```bash
DB_URL=jdbc:mysql://localhost:3306/campus_grade_management?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
DB_USERNAME=campus_grade
DB_PASSWORD=请替换为数据库密码
```

## 已准备的部署模板

- `deploy/env.example`
- `deploy/nginx-campus-grade-management.conf`
- `deploy/campus-grade-management.service`
- `deploy/start-backend.sh`
- `deploy/import-database.sh`

下一个会话可以根据用户服务器系统、域名和密码，把模板中的占位符替换为真实值。

## 验证清单

部署完成后依次验证：

```bash
systemctl status campus-grade-management
curl http://127.0.0.1:8080/api/auth/login
curl http://服务器IPv4/
```

浏览器验证：

- `http://服务器IPv4/`
- 管理员：`admin / admin123`
- 教师：`teacher01 / 123456`
- 学生：`student01 / 123456`

业务验证：

- 管理员能登录并看到全部菜单。
- 教师只能看到本人课程。
- 学生只能看到本人信息和成绩。
- 高等数学 85 分绩点为 3.7。
- Java 程序设计 85 分绩点为 3.3。
