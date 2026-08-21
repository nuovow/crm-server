<template>
  <div class="page">
    <el-card shadow="never">
      <div class="toolbar">
        <el-button v-permission="'role:add'" type="primary" @click="openDialog()">新增角色</el-button>
      </div>

      <el-table :data="roles" v-loading="loading" stripe>
        <el-table-column prop="name" label="角色名" width="160" />
        <el-table-column prop="code" label="编码" width="160" />
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="170">
          <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'role:edit'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'role:assignMenu'" link type="primary" @click="openAssign(row)">分配权限</el-button>
            <el-button v-permission="'role:delete'" link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 角色弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑角色' : '新增角色'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="form.name" maxlength="32" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" maxlength="32" placeholder="如 SALES_LEADER" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限弹窗 -->
    <el-dialog v-model="assignVisible" :title="`分配权限 - ${currentRole?.name}`" width="460px">
      <el-tree
        ref="treeRef"
        :data="menuTree"
        node-key="id"
        show-checkbox
        default-expand-all
        :props="{ label: 'name', children: 'children' }"
      />
      <template #footer>
        <el-button @click="assignVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssign">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRoles, saveRole, updateRole, deleteRole, getMenuIds, assignMenus } from '@/api/role'
import { getTree } from '@/api/menu'

const loading = ref(false)
const roles = ref([])

const dialogVisible = ref(false)
const formRef = ref(null)
const emptyForm = { id: null, name: '', code: '', remark: '' }
const form = reactive({ ...emptyForm })
const rules = {
  name: [{ required: true, message: '角色名不能为空', trigger: 'blur' }],
  code: [
    { required: true, message: '角色编码不能为空', trigger: 'blur' },
    { pattern: /^[A-Z][A-Z_0-9]{1,31}$/, message: '大写字母开头，可含下划线数字', trigger: 'blur' },
  ],
}

const menuTree = ref([])
const assignVisible = ref(false)
const currentRole = ref(null)
const treeRef = ref(null)

const loadRoles = async () => {
  loading.value = true
  try {
    roles.value = await listRoles()
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  Object.assign(form, emptyForm, row || {})
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  if (form.id) await updateRole(form)
  else await saveRole(form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadRoles()
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除角色「${row.name}」吗？有用户占用时会被拒绝。`, '警告', { type: 'warning' })
  await deleteRole(row.id)
  ElMessage.success('删除成功')
  loadRoles()
}

const openAssign = async (row) => {
  currentRole.value = row
  assignVisible.value = true
  const menuIds = await getMenuIds(row.id)
  // 回显要避开父节点：父节点由子节点勾选自动联动，手动勾父节点会把整棵子树全选
  const childIds = menuIds.filter((id) => {
    const node = treeRef.value?.getNode(id)
    return node && !node.childNodes?.length
  })
  nextTick(() => treeRef.value.setCheckedKeys(childIds, true))
}

const handleAssign = async () => {
  // 全量覆盖语义：勾选节点 + 半选父节点一起提交
  const checked = treeRef.value.getCheckedKeys()
  const halfChecked = treeRef.value.getHalfCheckedKeys()
  await assignMenus({ roleId: currentRole.value.id, menuIds: [...checked, ...halfChecked] })
  ElMessage.success('分配成功')
  assignVisible.value = false
}

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 19) : '')

onMounted(async () => {
  loadRoles()
  menuTree.value = await getTree()
})
</script>

<style scoped>
.toolbar {
  margin-bottom: 14px;
}
</style>
