<template>
  <div class="page">
    <el-card shadow="never">
      <div class="toolbar">
        <el-radio-group v-model="activeType" @change="filterByType">
          <el-radio-button value="">全部</el-radio-button>
          <el-radio-button v-for="t in types" :key="t" :value="t">{{ typeLabel(t) }}</el-radio-button>
        </el-radio-group>
        <el-button v-permission="'dict:add'" type="primary" @click="openDialog()">新增字典</el-button>
      </div>

      <el-table :data="filtered" v-loading="loading" stripe>
        <el-table-column prop="type" label="类型" width="180">
          <template #default="{ row }">
            <el-tag>{{ typeLabel(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="label" label="显示文本" min-width="160" />
        <el-table-column prop="value" label="存储值" min-width="140" />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="remark" label="备注" min-width="140" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'dict:edit'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'dict:delete'" link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑字典' : '新增字典'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" allow-create filterable default-first-option style="width: 100%" placeholder="选择或输入新类型">
            <el-option v-for="t in types" :key="t" :label="typeLabel(t)" :value="t" />
          </el-select>
        </el-form-item>
        <el-form-item label="显示文本" prop="label">
          <el-input v-model="form.label" maxlength="64" />
        </el-form-item>
        <el-form-item label="存储值" prop="value">
          <el-input v-model="form.value" maxlength="64" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listDicts, saveDict, updateDict, deleteDict } from '@/api/dict'

const TYPE_LABELS = {
  customer_level: '客户级别',
  customer_source: '客户来源',
}

const loading = ref(false)
const dicts = ref([])
const activeType = ref('')

const types = computed(() => [...new Set(dicts.value.map((d) => d.type))])
const filtered = computed(() =>
  activeType.value ? dicts.value.filter((d) => d.type === activeType.value) : dicts.value
)

const dialogVisible = ref(false)
const formRef = ref(null)
const emptyForm = { id: null, type: '', label: '', value: '', sort: 0, remark: '' }
const form = reactive({ ...emptyForm })
const rules = {
  type: [{ required: true, message: '类型不能为空', trigger: 'change' }],
  label: [{ required: true, message: '显示文本不能为空', trigger: 'blur' }],
  value: [{ required: true, message: '存储值不能为空', trigger: 'blur' }],
}

const typeLabel = (t) => TYPE_LABELS[t] || t

const loadData = async () => {
  loading.value = true
  try {
    dicts.value = await listDicts()
  } finally {
    loading.value = false
  }
}

const filterByType = () => {}

const openDialog = (row) => {
  Object.assign(form, emptyForm, row || { type: activeType.value || '' })
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  if (form.id) await updateDict(form)
  else await saveDict(form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除字典「${row.label}」吗？`, '警告', { type: 'warning' })
  await deleteDict(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}
</style>
