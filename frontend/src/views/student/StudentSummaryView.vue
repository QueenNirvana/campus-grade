<template>
  <div>
    <h1 class="page-title">成绩汇总</h1>
    <div class="stat-grid">
      <div class="stat-card"><div class="stat-label">课程数</div><div class="stat-value">{{ summary.courseCount }}</div></div>
      <div class="stat-card"><div class="stat-label">通过课程</div><div class="stat-value">{{ summary.passedCount }}</div></div>
      <div class="stat-card"><div class="stat-label">平均分</div><div class="stat-value">{{ Number(summary.averageScore || 0).toFixed(1) }}</div></div>
      <div class="stat-card"><div class="stat-label">平均绩点</div><div class="stat-value">{{ Number(summary.averageGradePoint || 0).toFixed(2) }}</div></div>
    </div>
    <div class="content-panel">
      <el-alert type="success" :closable="false" title="本页面数据由后端按当前登录学生过滤，不能查询其他学生成绩。" />
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { statisticsApi } from '../../api'
const summary = ref({})
onMounted(async () => { summary.value = await statisticsApi.studentSummary() })
</script>
