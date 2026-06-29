<template>
  <SimpleCrud title="课程管理" subtitle="维护课程编号、学分、学期、授课教师和默认成绩占比。" :api="api" :columns="columns" :fields="fields" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { crudApi } from '../../api'
const api = crudApi('courses')
const columns = [
  { prop: 'courseCode', label: '课程编号' }, { prop: 'courseName', label: '课程名称' },
  { prop: 'credit', label: '学分' }, { prop: 'teacherName', label: '授课教师' }, { prop: 'semester', label: '学期' },
  { prop: 'usualWeight', label: '平时占比' }, { prop: 'finalWeight', label: '期末占比' }
]
const fields = [
  { prop: 'courseCode', label: '课程编号' }, { prop: 'courseName', label: '课程名称' },
  { prop: 'credit', label: '学分', type: 'number', min: 0, max: 20 }, { prop: 'teacherId', label: '教师ID', type: 'number' },
  { prop: 'semester', label: '学期' },
  { prop: 'usualWeight', label: '平时占比', type: 'number', min: 0, max: 100 },
  { prop: 'finalWeight', label: '期末占比', type: 'number', min: 0, max: 100 }
]
</script>
