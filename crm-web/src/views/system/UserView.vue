<template>
  <div class="page">
    <el-card shadow="never">
      <el-form inline :model="query" @submit.prevent class="mb">
        <el-form-item label="用户名">
          <el-input v-model="query.username" placeholder="用户名模糊搜索" clearable @keyup.enter="loadData" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="rows" v-loading="loading" stripe>
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="nickname" label="昵称" width="140" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170">
          <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button
              v-permission="'user:edit'"
              link
              :type="row.status === 1 ? 'warning' : 'success'"
              :disabled="row.id === userId"
              @click="toggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button v-permission="'user:assignRole'" link type="primary" @click="openAssign(row)">分配角色</el-button>
            <el-button
              v-permission="'user:delete'"
              link
              type="danger"
              :disabled="row.id === userId"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadData"
      />
    </el-card>

    <!-- 分配角色弹窗 -->
    <el-dialog v-model="assignVisible" :title="`分配角色 - ${currentUser?.username}`" width="420px">
      <el-checkbox-group v-model="checkedRoleIds">
        <div v-for="r in roles" :key="r.id" class="role-item">
          <el-checkbox :value="r.id">{{ r.name }}（{{ r.code }}）</el-checkbox>
        </div>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="assignVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssign">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageUsers, updateStatus, deleteUser, getRoleIds, assignRoles } from '@/api/user'
import { listRoles } from '@/api/role'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userId = computed(() => Number(userStore.userInfo.userId) || Number(userStore.userInfo.id))

const loading = ref(false)
const rows = ref([])
const total = ref(0)
const query = reactive({ pageNum: 1, pageSize: 10, username: '', status: null })

const roles = ref([])
const assignVisible = ref(false)
const currentUser = ref(null)
const checkedRoleIds = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const page = await pageUsers(query)
    rows.value = page.records
    total.value = Number(page.total)
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  query.username = ''
  query.status = null
  query.pageNum = 1
  loadData()
}

const toggleStatus = async (row) => {
  const next = row.status === 1 ? 0 : 1
  await ElMessageBox.confirm(`确定${next === 1 ? '启用' : '禁用'}用户「${row.username}」吗？`, '提示', { type: 'warning' })
  await updateStatus(row.id, next)
  ElMessage.success('操作成功')
  loadData()
}

const openAssign = async (row) => {
  currentUser.value = row
  checkedRoleIds.value = await getRoleIds(row.id)
  assignVisible.value = true
}

const handleAssign = async () => {
  await assignRoles({ userId: currentUser.value.id, roleIds: checkedRoleIds.value })
  ElMessage.success('分配成功')
  assignVisible.value = false
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除用户「${row.username}」吗？`, '警告', { type: 'warning' })
  await deleteUser(row.id)
  ElMessage.success('删除成功')
  loadData()
}

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 19) : '')

onMounted(async () => {
  loadData()
  roles.value = await listRoles()
})
</script>

<style scoped>
.mb {
  margin-bottom: 14px;
}

.pager {
  margin-top: 14px;
  justify-content: flex-end;
}

.role-item {
  margin: 6px 0;
}
</style>
