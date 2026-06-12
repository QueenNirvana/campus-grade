<template>
  <SimpleCrud title="成绩录入" subtitle="教师只能录入和修改本人课程成绩，绩点由后端按课程规则计算。" :api="api" :columns="columns" :fields="fields" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { gradeApi } from '../../api'
const api = gradeApi
const columns = [
  { prop: 'studentNo', label: '学号' }, { prop: 'studentName', label: '学生' }, { prop: 'courseName', label: '课程' },
  { prop: 'totalScore', label: '总评' }, { prop: 'gradePoint', label: '绩点' }, { prop: 'remark', label: '备注' }
]
const fields = [
  { prop: 'studentId', label: '学生ID', type: 'number' }, { prop: 'courseId', label: '课程ID', type: 'number' },
  { prop: 'usualScore', label: '平时成绩', type: 'number', min: 0, max: 100 },
  { prop: 'finalScore', label: '期末成绩', type: 'number', min: 0, max: 100 },
  { prop: 'totalScore', label: '总评成绩', type: 'number', min: 0, max: 100 },
  { prop: 'remark', label: '备注' }
]
</script>
