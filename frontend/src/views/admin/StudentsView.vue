<template>
  <SimpleCrud title="学生管理" subtitle="维护学生档案并关联登录账号和班级。" :api="api" :columns="columns" :fields="fields" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { crudApi } from '../../api'
const api = crudApi('students')
const columns = [
  { prop: 'studentNo', label: '学号' }, { prop: 'name', label: '姓名' }, { prop: 'className', label: '班级' },
  { prop: 'gender', label: '性别' }, { prop: 'phone', label: '电话' }
]
const fields = [
  { prop: 'userId', label: '用户ID', type: 'number' }, { prop: 'classId', label: '班级ID', type: 'number' },
  { prop: 'studentNo', label: '学号' }, { prop: 'name', label: '姓名' }, { prop: 'gender', label: '性别' },
  { prop: 'phone', label: '电话' }, { prop: 'email', label: '邮箱' }
]
</script>
