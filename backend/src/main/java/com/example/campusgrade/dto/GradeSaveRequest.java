package com.example.campusgrade.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 新增或修改成绩时接收前端数据的 DTO。
 *
 * <p>前端只需提交平时成绩和期末成绩。totalScore 即使出现在请求中，
 * 服务层也不会直接采用，而会根据课程权重重新计算，以防止伪造总评成绩。</p>
 */
public class GradeSaveRequest {
    /** 成绩记录主键；新增时为空，修改时由控制器写入。 */
    public Long id;
    /** 接受成绩的学生主键。 */
    @NotNull(message = "学生不能为空")
    public Long studentId;
    /** 成绩所属课程主键。 */
    @NotNull(message = "课程不能为空")
    public Long courseId;
    /** 教师录入的平时成绩，范围由服务层校验为 0-100。 */
    @NotNull(message = "平时成绩不能为空")
    public BigDecimal usualScore;
    /** 教师录入的期末成绩，范围由服务层校验为 0-100。 */
    @NotNull(message = "期末成绩不能为空")
    public BigDecimal finalScore;
    /** 兼容请求结构的总评字段；保存时以服务端重新计算的结果为准。 */
    public BigDecimal totalScore;
    /** 可选备注，例如补考说明。 */
    public String remark;
}
