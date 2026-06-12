<template>
  <SimpleCrud title="我的成绩" subtitle="学生只能查看本人课程成绩。" :api="api" :columns="columns" :fields="[]" :editable="false" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { gradeApi } from '../../api'
const api = gradeApi
const columns = [
  { prop: 'courseCode', label: '课程编号' }, { prop: 'courseName', label: '课程' }, { prop: 'credit', label: '学分' },
  { prop: 'usualScore', label: '平时' }, { prop: 'finalScore', label: '期末' }, { prop: 'totalScore', label: '总评' },
  { prop: 'gradePoint', label: '绩点' }, { prop: 'teacherName', label: '教师' }
]
</script>
