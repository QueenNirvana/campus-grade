<template>
  <SimpleCrud title="用户管理" subtitle="维护登录账号、角色和状态。" :api="api" :columns="columns" :fields="fields" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { crudApi } from '../../api'
const api = crudApi('users')
const columns = [
  { prop: 'username', label: '用户名' }, { prop: 'realName', label: '姓名' },
  { prop: 'role', label: '角色' }, { prop: 'status', label: '状态' }
]
const fields = [
  { prop: 'username', label: '用户名' }, { prop: 'password', label: '密码' }, { prop: 'realName', label: '姓名' },
  { prop: 'role', label: '角色', type: 'select', options: [{ label: '管理员', value: 'ADMIN' }, { label: '教师', value: 'TEACHER' }, { label: '学生', value: 'STUDENT' }] },
  { prop: 'status', label: '状态', type: 'number', min: 0, max: 1 }
]
</script>
