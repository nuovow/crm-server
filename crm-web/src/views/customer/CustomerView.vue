<template>
  <div class="page">
    <!-- 搜索区 -->
    <el-card shadow="never" class="search-card">
      <el-form inline :model="query" @submit.prevent>
        <el-form-item label="客户名称">
          <el-input v-model="query.name" placeholder="名称模糊搜索" clearable @keyup.enter="loadData" />
        </el-form-item>
        <el-form-item label="级别">
          <el-select v-model="query.level" placeholder="全部" clearable style="width: 140px">
            <el-option v-for="d in levelDicts" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="query.phone" placeholder="手机号" clearable @keyup.enter="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区 -->
    <el-card shadow="never">
      <div class="toolbar">
        <el-button v-permission="'customer:add'" type="primary" @click="openDialog()">新增客户</el-button>
        <el-button v-permission="'customer:import'" @click="triggerImport">导入</el-button>
        <el-button v-permission="'customer:export'" @click="handleExport">导出</el-button>
        <input ref="fileRef" type="file" accept=".xlsx,.xls" hidden @change="handleImport" />
      </div>

      <el-table :data="rows" v-loading="loading" stripe>
        <el-table-column prop="name" label="客户名称" min-width="140" />
        <el-table-column prop="industry" label="行业" width="110" />
        <el-table-column prop="source" label="来源" width="110" />
        <el-table-column label="级别" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="levelTagType(row.level)" effect="dark">{{ row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="170">
          <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="goDetail(row.id)">详情</el-button>
            <el-button v-permission="'customer:edit'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'customer:delete'" link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadData"
        @size-change="loadData"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑客户' : '新增客户'" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="客户名称" prop="name">
          <el-input v-model="form.name" maxlength="64" />
        </el-form-item>
        <el-form-item label="行业" prop="industry">
          <el-input v-model="form.industry" maxlength="32" />
        </el-form-item>
        <el-form-item label="客户来源" prop="source">
          <el-select v-model="form.source" placeholder="请选择" clearable style="width: 100%">
            <el-option v-for="d in sourceDicts" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户级别" prop="level">
          <el-select v-model="form.level" placeholder="请选择" clearable style="width: 100%">
            <el-option v-for="d in levelDicts" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" maxlength="11" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" maxlength="255" :rows="3" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  pageCustomers,
  saveCustomer,
  updateCustomer,
  deleteCustomer,
  exportCustomers,
  importCustomers,
} from '@/api/customer'
import { listByType } from '@/api/dict'

const router = useRouter()

const loading = ref(false)
const rows = ref([])
const total = ref(0)
const query = reactive({ pageNum: 1, pageSize: 10, name: '', level: '', phone: '' })

const levelDicts = ref([])
const sourceDicts = ref([])

const dialogVisible = ref(false)
const saving = ref(false)
const formRef = ref(null)
const emptyForm = { id: null, name: '', industry: '', source: '', level: '', phone: '', remark: '' }
const form = reactive({ ...emptyForm })

const phoneReg = /^1[3-9]\d{9}$/
const rules = {
  name: [{ required: true, message: '客户名称不能为空', trigger: 'blur' }],
  level: [{ pattern: /^[ABC]$/, message: '客户级别只能是A、B、C', trigger: 'change' }],
  phone: [{ pattern: phoneReg, message: '手机号格式不正确', trigger: 'blur' }],
}

const loadData = async () => {
  loading.value = true
  try {
    const page = await pageCustomers(query)
    rows.value = page.records
    total.value = Number(page.total)
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  query.name = ''
  query.level = ''
  query.phone = ''
  query.pageNum = 1
  loadData()
}

const openDialog = (row) => {
  Object.assign(form, emptyForm, row || {})
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  saving.value = true
  try {
    if (form.id) {
      await updateCustomer(form)
    } else {
      await saveCustomer(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } finally {
    saving.value = false
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除客户「${row.name}」吗？`, '警告', { type: 'warning' })
  await deleteCustomer(row.id)
  ElMessage.success('删除成功')
  loadData()
}

const goDetail = (id) => router.push(`/customer/detail/${id}`)

const handleExport = async () => {
  const blob = await exportCustomers()
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'customers.xlsx'
  a.click()
  URL.revokeObjectURL(url)
}

const fileRef = ref(null)
const triggerImport = () => fileRef.value.click()

const handleImport = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  const result = await importCustomers(file)
  ElMessageBox.alert(
    `共 ${result.totalRow} 行，成功 ${result.successRow} 行，失败 ${result.totalRow - result.successRow} 行<br/>` +
      (result.failList?.length ? `<div style="text-align:left">${result.failList.join('<br/>')}</div>` : ''),
    '导入结果',
    { dangerouslyUseHTMLString: true }
  )
  loadData()
  e.target.value = ''
}

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 19) : '')

const levelTagType = (level) => ({ A: 'danger', B: 'warning', C: 'info' }[level] || 'info')

onMounted(async () => {
  loadData()
  levelDicts.value = await listByType('customer_level')
  sourceDicts.value = await listByType('customer_source')
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.toolbar {
  margin-bottom: 14px;
}

.pager {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
