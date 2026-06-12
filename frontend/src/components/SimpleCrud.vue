<template>
  <div>
    <div class="toolbar">
      <div>
        <h1 class="page-title">{{ title }}</h1>
        <p v-if="subtitle" class="page-subtitle">{{ subtitle }}</p>
      </div>
      <el-button v-if="editable" type="primary" @click="openDialog()">新增</el-button>
    </div>
    <div class="content-panel">
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column v-for="col in columns" :key="col.prop" :prop="col.prop" :label="col.label" :min-width="col.width || 120" />
        <el-table-column v-if="editable" label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑' : '新增'" width="520px">
      <el-form label-width="96px">
        <el-form-item v-for="field in fields" :key="field.prop" :label="field.label">
          <el-select v-if="field.type === 'select'" v-model="form[field.prop]" style="width:100%">
            <el-option v-for="option in field.options || []" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
          <el-input-number v-else-if="field.type === 'number'" v-model="form[field.prop]" :min="field.min ?? 0" :max="field.max ?? 100000" style="width:100%" />
          <el-input v-else v-model="form[field.prop]" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  title: String,
  subtitle: String,
  columns: Array,
  fields: Array,
  api: Object,
  editable: { type: Boolean, default: true }
})

const rows = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = reactive({})

async function load() {
  loading.value = true
  try {
    rows.value = await props.api.list()
  } finally {
    loading.value = false
  }
}

function openDialog(row = null) {
  Object.keys(form).forEach((key) => delete form[key])
  Object.assign(form, row || {})
  dialogVisible.value = true
}

async function save() {
  if (form.id) {
    await props.api.update(form.id, form)
  } else {
    await props.api.create(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  await load()
}

async function remove(row) {
  await ElMessageBox.confirm('确定删除该记录吗？', '提示')
  await props.api.remove(row.id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
