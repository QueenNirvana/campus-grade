<template>
  <SimpleCrud title="我的课程" subtitle="教师只能看到本人负责课程。" :api="api" :columns="columns" :fields="[]" :editable="false" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { crudApi } from '../../api'
const api = crudApi('courses')
const columns = [
  { prop: 'courseCode', label: '课程编号' }, { prop: 'courseName', label: '课程名称' },
  { prop: 'credit', label: '学分' }, { prop: 'semester', label: '学期' }
]
</script>
