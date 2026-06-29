<template>
  <div>
    <h1 class="page-title">控制台</h1>
    <p class="page-subtitle">围绕角色权限、课程级成绩规则和成绩统计的课程设计演示首页。</p>
    <div class="stat-grid">
      <div class="stat-card" v-for="item in cards" :key="item.label">
        <div class="stat-label">{{ item.label }}</div>
        <div class="stat-value">{{ item.value }}</div>
      </div>
    </div>
    <div class="content-panel">
      <h3>核心演示点</h3>
      <el-alert title="成绩规则按课程配置：先按平时/期末占比计算总评，再按课程绩点区间换算绩点。" type="success" show-icon :closable="false" />
      <el-divider />
      <el-descriptions :column="3" border>
        <el-descriptions-item label="当前角色">{{ roleName }}</el-descriptions-item>
        <el-descriptions-item label="用户">{{ auth.user?.realName }}</el-descriptions-item>
        <el-descriptions-item label="权限模式">前端菜单 + 后端接口双重控制</el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { statisticsApi } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const stats = ref({})
const roleName = computed(() => ({ ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }[auth.role]))
const cards = computed(() => {
  if (auth.role === 'ADMIN') {
    return [
      { label: '用户数', value: stats.value.userCount ?? '-' },
      { label: '学生数', value: stats.value.studentCount ?? '-' },
      { label: '教师数', value: stats.value.teacherCount ?? '-' },
      { label: '平均分', value: Number(stats.value.averageScore ?? 0).toFixed(1) }
    ]
  }
  return [
    { label: '角色', value: roleName.value },
    { label: '账号', value: auth.user?.username },
    { label: '系统', value: '已登录' },
    { label: '模式', value: '演示版' }
  ]
})

onMounted(async () => {
  if (auth.role === 'ADMIN') {
    try {
      stats.value = await statisticsApi.overview()
    } catch {
      stats.value = {}
    }
  }
})
</script>
