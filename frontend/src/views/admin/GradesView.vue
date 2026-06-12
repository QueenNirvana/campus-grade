<template>
  <SimpleCrud title="成绩管理" subtitle="保存成绩时后端按课程绩点规则自动计算 gradePoint。" :api="api" :columns="columns" :fields="fields" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { gradeApi } from '../../api'
const api = gradeApi
const columns = [
  { prop: 'studentNo', label: '学号' }, { prop: 'studentName', label: '学生' }, { prop: 'courseName', label: '课程' },
  { prop: 'usualScore', label: '平时' }, { prop: 'finalScore', label: '期末' }, { prop: 'totalScore', label: '总评' },
  { prop: 'gradePoint', label: '绩点' }, { prop: 'teacherName', label: '教师' }, { prop: 'remark', label: '备注', width: 180 }
]
const fields = [
  { prop: 'studentId', label: '学生ID', type: 'number' }, { prop: 'courseId', label: '课程ID', type: 'number' },
  { prop: 'usualScore', label: '平时成绩', type: 'number', min: 0, max: 100 },
  { prop: 'finalScore', label: '期末成绩', type: 'number', min: 0, max: 100 },
  { prop: 'totalScore', label: '总评成绩', type: 'number', min: 0, max: 100 },
  { prop: 'remark', label: '备注' }
]
</script>
