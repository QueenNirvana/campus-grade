<template>
  <div>
    <h1 class="page-title">课程绩点规则管理</h1>
    <p class="page-subtitle">每门课程独立维护 0-100 分区间与绩点映射，保存前后端都会校验不能重叠和缺口。</p>
    <div class="content-panel">
      <el-form inline>
        <el-form-item label="课程">
          <el-select v-model="courseId" style="width:260px" @change="loadRules">
            <el-option v-for="course in courses" :key="course.id" :label="`${course.courseName}（${course.courseCode}）`" :value="course.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存规则</el-button>
          <el-button @click="addRule">新增区间</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="rules" border>
        <el-table-column label="最低分">
          <template #default="{ row }"><el-input-number v-model="row.minScore" :min="0" :max="100" /></template>
        </el-table-column>
        <el-table-column label="最高分">
          <template #default="{ row }"><el-input-number v-model="row.maxScore" :min="0" :max="100" /></template>
        </el-table-column>
        <el-table-column label="绩点">
          <template #default="{ row }"><el-input-number v-model="row.gradePoint" :min="0" :max="4" :step="0.1" /></template>
        </el-table-column>
        <el-table-column label="等级">
          <template #default="{ row }"><el-input v-model="row.label" /></template>
        </el-table-column>
        <el-table-column label="操作" width="90">
          <template #default="{ $index }"><el-button type="danger" size="small" @click="rules.splice($index, 1)">删除</el-button></template>
        </el-table-column>
      </el-table>
      <el-alert style="margin-top:14px" type="info" :closable="false" title="示例：种子数据中高等数学 85 分绩点为 3.7，Java 程序设计 85 分绩点为 3.3。" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { crudApi, ruleApi } from '../../api'

const courses = ref([])
const courseId = ref()
const rules = ref([])

function addRule() {
  rules.value.push({ minScore: 0, maxScore: 100, gradePoint: 0, label: '新规则' })
}

async function loadRules() {
  if (!courseId.value) return
  rules.value = await ruleApi.list(courseId.value)
}

async function save() {
  await ruleApi.save(courseId.value, rules.value)
  ElMessage.success('绩点规则保存成功')
  await loadRules()
}

onMounted(async () => {
  courses.value = await crudApi('courses').list()
  courseId.value = courses.value[0]?.id
  await loadRules()
})
</script>
