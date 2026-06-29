<template>
  <div>
    <h1 class="page-title">课程成绩规则管理</h1>
    <p class="page-subtitle">每门课程独立维护平时/期末成绩占比，并按总评成绩区间换算绩点。</p>
    <div class="content-panel">
      <el-form inline>
        <el-form-item label="课程">
          <el-select v-model="courseId" style="width:260px" @change="loadRules">
            <el-option v-for="course in courses" :key="course.id" :label="`${course.courseName}（${course.courseCode}）`" :value="course.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="平时占比">
          <el-input-number v-model="config.usualWeight" :min="0" :max="100" :step="5" :formatter="formatPercent" :parser="parsePercent" />
        </el-form-item>
        <el-form-item label="期末占比">
          <el-input-number v-model="config.finalWeight" :min="0" :max="100" :step="5" :formatter="formatPercent" :parser="parsePercent" />
        </el-form-item>
        <el-form-item>
          <el-tag :type="weightTotal === 100 ? 'success' : 'danger'">合计 {{ weightTotal }}%</el-tag>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="!courseId" @click="save">保存规则</el-button>
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
      <el-alert style="margin-top:14px" type="info" :closable="false" title="保存成绩时，后端先按本课程占比计算总评，再按下方区间计算绩点。" />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { crudApi, ruleApi } from '../../api'

const courses = ref([])
const courseId = ref()
const rules = ref([])
const config = reactive({
  usualWeight: 30,
  finalWeight: 70
})

const weightTotal = computed(() => Number(config.usualWeight || 0) + Number(config.finalWeight || 0))

const formatPercent = (value) => (value === undefined || value === null || value === '' ? '' : `${value}%`)
const parsePercent = (value) => String(value).replace(/[^\d.]/g, '')

function addRule() {
  rules.value.push({ minScore: 0, maxScore: 100, gradePoint: 0, label: '新规则' })
}

async function loadRules() {
  if (!courseId.value) return
  const data = await ruleApi.list(courseId.value)
  config.usualWeight = Number(data.usualWeight ?? 30)
  config.finalWeight = Number(data.finalWeight ?? 70)
  rules.value = data.rules || []
}

async function save() {
  await ruleApi.save(courseId.value, {
    courseId: courseId.value,
    usualWeight: config.usualWeight,
    finalWeight: config.finalWeight,
    rules: rules.value
  })
  ElMessage.success('课程成绩规则保存成功')
  await loadRules()
}

onMounted(async () => {
  courses.value = await crudApi('courses').list()
  courseId.value = courses.value[0]?.id
  await loadRules()
})
</script>
