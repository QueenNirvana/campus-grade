<template>
  <div>
    <h1 class="page-title">统计分析</h1>
    <p class="page-subtitle">展示系统概览和指定课程的平均分、最高分、最低分、及格率、分数段分布。</p>
    <div class="stat-grid">
      <div class="stat-card"><div class="stat-label">用户数</div><div class="stat-value">{{ overview.userCount }}</div></div>
      <div class="stat-card"><div class="stat-label">学生数</div><div class="stat-value">{{ overview.studentCount }}</div></div>
      <div class="stat-card"><div class="stat-label">课程数</div><div class="stat-value">{{ overview.courseCount }}</div></div>
      <div class="stat-card"><div class="stat-label">平均分</div><div class="stat-value">{{ Number(overview.averageScore || 0).toFixed(1) }}</div></div>
    </div>
    <div class="content-panel">
      <el-form inline>
        <el-form-item label="课程">
          <el-select v-model="courseId" style="width:260px" @change="loadCourse">
            <el-option v-for="course in courses" :key="course.id" :value="course.id" :label="course.courseName" />
          </el-select>
        </el-form-item>
      </el-form>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="平均分">{{ Number(courseStats.averageScore || 0).toFixed(1) }}</el-descriptions-item>
        <el-descriptions-item label="最高分">{{ courseStats.maxScore }}</el-descriptions-item>
        <el-descriptions-item label="最低分">{{ courseStats.minScore }}</el-descriptions-item>
        <el-descriptions-item label="及格率">{{ Number(courseStats.passRate || 0).toFixed(1) }}%</el-descriptions-item>
      </el-descriptions>
      <el-table :data="courseStats.distribution || []" border style="margin-top:16px">
        <el-table-column prop="bucket" label="分数段" />
        <el-table-column prop="count" label="人数" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { crudApi, statisticsApi } from '../../api'

const overview = ref({})
const courses = ref([])
const courseId = ref()
const courseStats = ref({})

async function loadCourse() {
  if (courseId.value) courseStats.value = await statisticsApi.course(courseId.value)
}

onMounted(async () => {
  overview.value = await statisticsApi.overview()
  courses.value = await crudApi('courses').list()
  courseId.value = courses.value[0]?.id
  await loadCourse()
})
</script>
