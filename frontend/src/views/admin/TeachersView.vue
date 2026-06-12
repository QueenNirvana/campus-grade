<template>
  <SimpleCrud title="教师管理" subtitle="维护教师档案并关联登录账号。" :api="api" :columns="columns" :fields="fields" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { crudApi } from '../../api'
const api = crudApi('teachers')
const columns = [
  { prop: 'teacherNo', label: '工号' }, { prop: 'name', label: '姓名' }, { prop: 'title', label: '职称' },
  { prop: 'phone', label: '电话' }, { prop: 'username', label: '账号' }
]
const fields = [
  { prop: 'userId', label: '用户ID', type: 'number' }, { prop: 'teacherNo', label: '工号' }, { prop: 'name', label: '姓名' },
  { prop: 'title', label: '职称' }, { prop: 'phone', label: '电话' }, { prop: 'email', label: '邮箱' }
]
</script>
