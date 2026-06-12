from docx import Document
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_CELL_VERTICAL_ALIGNMENT
from docx.shared import Inches, Pt, RGBColor
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from pathlib import Path

BASE = Path(__file__).resolve().parent
OUT = BASE / "高校成绩管理系统课程设计报告.docx"
UI_IMAGE = BASE / "ui-reference.png"
LOGIN_IMAGE = BASE / "frontend-login.png"
DASHBOARD_IMAGE = BASE / "frontend-dashboard.png"


def set_font(run, size=11, bold=False, color=None):
    run.font.name = "Calibri"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "微软雅黑")
    run._element.rPr.rFonts.set(qn("w:ascii"), "Calibri")
    run.font.size = Pt(size)
    run.bold = bold
    if color:
        run.font.color.rgb = RGBColor.from_string(color)


def set_cell_text(cell, text, bold=False):
    cell.vertical_alignment = WD_CELL_VERTICAL_ALIGNMENT.CENTER
    p = cell.paragraphs[0]
    p.paragraph_format.space_after = Pt(0)
    run = p.add_run(str(text))
    set_font(run, 10.5, bold)


def style_table(table):
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.style = "Table Grid"
    for row in table.rows:
        for cell in row.cells:
            cell.margin_top = 80
            cell.margin_bottom = 80
            cell.margin_left = 120
            cell.margin_right = 120


def add_heading(doc, text, level=1):
    p = doc.add_heading(level=level)
    run = p.add_run(text)
    set_font(run, 16 if level == 1 else 13, True, "2E74B5" if level <= 2 else "1F4D78")
    return p


def add_para(doc, text):
    p = doc.add_paragraph()
    p.paragraph_format.space_after = Pt(6)
    p.paragraph_format.line_spacing = 1.1
    run = p.add_run(text)
    set_font(run, 11)


def add_bullets(doc, items):
    for item in items:
        p = doc.add_paragraph(style="List Bullet")
        p.paragraph_format.space_after = Pt(4)
        run = p.add_run(item)
        set_font(run, 11)


def add_table(doc, headers, rows):
    table = doc.add_table(rows=1, cols=len(headers))
    style_table(table)
    for i, header in enumerate(headers):
        set_cell_text(table.rows[0].cells[i], header, True)
    for row in rows:
        cells = table.add_row().cells
        for i, value in enumerate(row):
            set_cell_text(cells[i], value)
    doc.add_paragraph()
    return table


def build():
    doc = Document()
    section = doc.sections[0]
    section.top_margin = Inches(1)
    section.bottom_margin = Inches(1)
    section.left_margin = Inches(1)
    section.right_margin = Inches(1)

    title = doc.add_paragraph()
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    title_run = title.add_run("高校成绩管理系统课程设计报告")
    set_font(title_run, 24, True, "0B2545")
    subtitle = doc.add_paragraph()
    subtitle.alignment = WD_ALIGN_PARAGRAPH.CENTER
    sub_run = subtitle.add_run("Spring Boot + Vue 3 + MySQL 8")
    set_font(sub_run, 12, False, "555555")
    doc.add_paragraph()

    add_heading(doc, "一、需求分析")
    add_para(doc, "本系统面向高校课程成绩管理场景，提供管理员、教师、学生三类角色的成绩管理功能。系统用于课程设计提交和答辩演示，要求后端、前端、数据库和报告均可对应说明。")
    add_bullets(doc, [
        "管理员负责用户、学生、教师、班级、课程、课程绩点规则、成绩和统计管理。",
        "教师只能查看本人课程并维护本人课程成绩。",
        "学生只能查看本人信息、本人课程成绩和成绩汇总。",
        "绩点规则按课程配置，同一分数在不同课程中可得到不同绩点。"
    ])

    add_heading(doc, "二、系统架构")
    add_para(doc, "系统采用前后端分离架构。前端通过 Axios 调用后端 REST API，后端通过 Spring Security 和 JWT 完成认证授权，通过 MyBatis XML 访问 MySQL 数据库。")
    add_table(doc, ["层次", "技术", "说明"], [
        ["前端", "Vue 3、Vite、Element Plus、Pinia、Axios", "后台管理系统界面、角色菜单、表格和表单"],
        ["后端", "Spring Boot、Spring Security、JWT", "REST API、登录认证、角色权限控制"],
        ["持久层", "原生 MyBatis Mapper 接口和 XML SQL", "显式 SQL，便于课程设计说明"],
        ["数据库", "MySQL 8", "保存用户、课程、绩点规则和成绩"]
    ])

    add_heading(doc, "三、功能模块设计")
    add_table(doc, ["角色", "页面", "功能"], [
        ["管理员", "用户/学生/教师/班级/课程管理", "维护系统基础数据"],
        ["管理员", "绩点规则管理", "按课程维护分数区间与绩点映射"],
        ["管理员", "成绩管理/统计分析", "录入成绩、查看系统和课程统计"],
        ["教师", "我的课程/成绩录入/课程统计", "管理本人负责课程"],
        ["学生", "个人信息/我的成绩/成绩汇总", "查看个人档案和本人课程成绩"]
    ])

    add_heading(doc, "四、数据库设计")
    add_table(doc, ["表名", "用途", "关键字段"], [
        ["users", "登录账号", "username、password、real_name、role、status"],
        ["classes", "班级信息", "class_name、grade_year、major"],
        ["students", "学生档案", "user_id、class_id、student_no、name"],
        ["teachers", "教师档案", "user_id、teacher_no、name、title"],
        ["courses", "课程信息", "course_code、course_name、credit、teacher_id"],
        ["course_grade_rules", "课程级绩点规则", "course_id、min_score、max_score、grade_point、label"],
        ["grades", "成绩记录", "student_id、course_id、total_score、grade_point"]
    ])

    add_heading(doc, "五、接口设计")
    add_table(doc, ["接口", "方法", "说明"], [
        ["/api/auth/login", "POST", "用户登录并返回 JWT"],
        ["/api/auth/me", "GET", "获取当前登录用户"],
        ["/api/users", "GET/POST/PUT/DELETE", "用户管理，仅管理员"],
        ["/api/courses/{courseId}/grade-rules", "GET/POST", "查询和保存课程绩点规则"],
        ["/api/grades", "GET/POST/PUT/DELETE", "按角色过滤的成绩管理"],
        ["/api/statistics/overview", "GET", "管理员系统概览"],
        ["/api/statistics/courses/{courseId}", "GET", "课程统计"],
        ["/api/statistics/student-summary", "GET", "学生本人汇总"]
    ])

    add_heading(doc, "六、关键实现")
    add_para(doc, "系统关键业务是课程级绩点计算。管理员为每门课程配置分数区间，后端保存成绩时根据 course_id 查询对应规则，再根据 total_score 匹配区间并写入 grade_point。")
    add_bullets(doc, [
        "规则必须覆盖 0-100 分。",
        "同一课程规则区间不能重叠。",
        "录入成绩前课程必须有有效规则。",
        "教师保存成绩时会校验课程是否属于当前教师。",
        "学生查询成绩时使用当前登录账号关联的 student_id 过滤。"
    ])

    add_heading(doc, "七、测试计划与结果")
    add_table(doc, ["测试项", "结果"], [
        ["后端单元测试", "mvn test 通过，覆盖绩点规则校验和课程级绩点计算"],
        ["前端构建测试", "npm run build 通过"],
        ["SQL 脚本", "提供 schema.sql 和 seed.sql，可按本机 MySQL 密码导入"],
        ["权限验证", "后端服务层限制教师课程归属和学生本人数据访问"],
        ["演示账号", "admin/admin123、teacher01/123456、student01/123456"]
    ])

    add_heading(doc, "八、系统截图")
    add_para(doc, "下图为系统后台管理界面参考图，实际前端实现采用同类布局：左侧角色菜单、顶部用户信息、内容区表格和统计卡片。")
    if UI_IMAGE.exists():
        doc.add_picture(str(UI_IMAGE), width=Inches(6.2))
    add_para(doc, "登录页面截图：")
    if LOGIN_IMAGE.exists():
        doc.add_picture(str(LOGIN_IMAGE), width=Inches(6.2))
    add_para(doc, "管理员控制台截图：")
    if DASHBOARD_IMAGE.exists():
        doc.add_picture(str(DASHBOARD_IMAGE), width=Inches(6.2))

    add_heading(doc, "九、总结")
    add_para(doc, "本项目完成了高校成绩管理系统课程设计要求的核心范围，包含可运行后端、可运行前端、MySQL 建表和种子数据、角色登录权限、课程级绩点规则、成绩管理、统计分析和中文 Word 报告。系统范围保持在课程设计演示所需功能内，未加入移动端、选课、审批、导入导出、微服务等额外复杂功能。")

    doc.save(OUT)
    print(OUT)


if __name__ == "__main__":
    build()
