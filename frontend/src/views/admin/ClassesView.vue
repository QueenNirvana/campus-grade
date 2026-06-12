<template>
  <SimpleCrud title="班级管理" subtitle="维护年级、专业和班级名称。" :api="api" :columns="columns" :fields="fields" />
</template>
<script setup>
import SimpleCrud from '../../components/SimpleCrud.vue'
import { crudApi } from '../../api'
const api = crudApi('classes')
const columns = [{ prop: 'className', label: '班级名称' }, { prop: 'gradeYear', label: '年级' }, { prop: 'major', label: '专业' }]
const fields = [{ prop: 'className', label: '班级名称' }, { prop: 'gradeYear', label: '年级', type: 'number', min: 2000, max: 2100 }, { prop: 'major', label: '专业' }]
</script>
