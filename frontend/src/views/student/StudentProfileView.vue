<template>
  <div>
    <h1 class="page-title">个人信息</h1>
    <div class="content-panel">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="学号">{{ profile.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ profile.name }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ profile.className }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ profile.gender }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ profile.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ profile.email }}</el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import request from '../../utils/request'
const profile = ref({})
onMounted(async () => { profile.value = await request.get('/students/me') })
</script>
