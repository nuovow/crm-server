<template>
  <div class="page" v-loading="loading">
    <!-- 客户基本信息 -->
    <el-card shadow="never">
      <template #header>
        <div class="card-head">
          <span>客户信息</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="客户名称">{{ customer.name }}</el-descriptions-item>
        <el-descriptions-item label="行业">{{ customer.industry || '-' }}</el-descriptions-item>
        <el-descriptions-item label="来源">{{ customer.source || '-' }}</el-descriptions-item>
        <el-descriptions-item label="级别">
          <el-tag :type="levelTagType(customer.level)" effect="dark">{{ customer.level || '-' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="手机号">{{ customer.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatTime(customer.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ customer.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never">
      <el-tabs v-model="activeTab">
        <!-- 跟进记录时间线 -->
        <el-tab-pane label="跟进记录" name="follow">
          <el-button type="primary" class="mb" @click="followVisible = true">新增跟进</el-button>
          <el-empty v-if="!follows.length" description="暂无跟进记录" />
          <el-timeline v-else>
            <el-timeline-item
              v-for="f in follows"
              :key="f.id"
              :timestamp="formatTime(f.createTime)"
              placement="top"
              :type="followTagType(f.followType)"
            >
              <div class="follow-item">
                <el-tag size="small" :type="followTagType(f.followType)">{{ followLabel(f.followType) }}</el-tag>
                <span class="follow-content">{{ f.content }}</span>
                <div v-if="f.nextTime" class="follow-next">下次跟进：{{ formatTime(f.nextTime) }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>

        <!-- 联系人 -->
        <el-tab-pane label="联系人" name="contact">
          <el-button type="primary" class="mb" @click="openContactDialog()">新增联系人</el-button>
          <el-table :data="contacts" stripe>
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="position" label="职务" width="140" />
            <el-table-column prop="phone" label="手机号" width="140" />
            <el-table-column prop="wechat" label="微信" width="140" />
            <el-table-column label="主要联系人" width="110" align="center">
              <template #default="{ row }">
                <el-tag v-if="row.isPrimary === 1" type="success" size="small">是</el-tag>
                <span v-else>否</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140">
              <template #default="{ row }">
                <el-button link type="primary" @click="openContactDialog(row)">编辑</el-button>
                <el-button link type="danger" @click="handleDeleteContact(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 商机 -->
        <el-tab-pane label="商机" name="opportunity">
          <el-button type="primary" class="mb" @click="openOppDialog()">新增商机</el-button>
          <el-table :data="opportunities" stripe>
            <el-table-column prop="title" label="商机名称" min-width="150" />
            <el-table-column label="金额" width="130" align="right">
              <template #default="{ row }">￥{{ row.amount ?? '-' }}</template>
            </el-table-column>
            <el-table-column label="阶段" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="stageTagType(row.stage)">{{ stageLabel(row.stage) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预计成交日" width="130">
              <template #default="{ row }">{{ row.expectedDate || '-' }}</template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
            <el-table-column label="操作" width="140">
              <template #default="{ row }">
                <el-button link type="primary" @click="openOppDialog(row)">编辑</el-button>
                <el-button link type="danger" @click="handleDeleteOpp(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增跟进弹窗 -->
    <el-dialog v-model="followVisible" title="新增跟进" width="520px">
      <el-form ref="followFormRef" :model="followForm" :rules="followRules" label-width="90px">
        <el-form-item label="跟进方式" prop="followType">
          <el-select v-model="followForm.followType" style="width: 100%">
            <el-option v-for="t in FOLLOW_TYPES" :key="t.value" :label="t.label" :value="t.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联联系人" prop="contactId">
          <el-select v-model="followForm.contactId" placeholder="可不选" clearable style="width: 100%">
            <el-option v-for="c in contacts" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进内容" prop="content">
          <el-input v-model="followForm.content" type="textarea" :rows="4" maxlength="1000" show-word-limit />
        </el-form-item>
        <el-form-item label="下次跟进" prop="nextTime">
          <el-date-picker v-model="followForm.nextTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="followVisible = false">取消</el-button>
        <el-button type="primary" :loading="savingFollow" @click="handleAddFollow">保存</el-button>
      </template>
    </el-dialog>

    <!-- 联系人弹窗 -->
    <el-dialog v-model="contactVisible" :title="contactForm.id ? '编辑联系人' : '新增联系人'" width="480px">
      <el-form ref="contactFormRef" :model="contactForm" :rules="contactRules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="contactForm.name" maxlength="32" />
        </el-form-item>
        <el-form-item label="职务" prop="position">
          <el-input v-model="contactForm.position" maxlength="32" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="contactForm.phone" maxlength="11" />
        </el-form-item>
        <el-form-item label="微信号" prop="wechat">
          <el-input v-model="contactForm.wechat" maxlength="64" />
        </el-form-item>
        <el-form-item label="主要联系人" prop="isPrimary">
          <el-switch v-model="contactForm.isPrimary" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="contactVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveContact">保存</el-button>
      </template>
    </el-dialog>

    <!-- 商机弹窗 -->
    <el-dialog v-model="oppVisible" :title="oppForm.id ? '编辑商机' : '新增商机'" width="520px">
      <el-form ref="oppFormRef" :model="oppForm" :rules="oppRules" label-width="100px">
        <el-form-item label="商机名称" prop="title">
          <el-input v-model="oppForm.title" maxlength="64" />
        </el-form-item>
        <el-form-item label="预计金额" prop="amount">
          <el-input-number v-model="oppForm.amount" :min="0" :precision="2" :step="1000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="阶段" prop="stage">
          <el-select v-model="oppForm.stage" style="width: 100%">
            <el-option v-for="s in STAGES" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预计成交日" prop="expectedDate">
          <el-date-picker v-model="oppForm.expectedDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="oppForm.remark" type="textarea" :rows="3" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="oppVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveOpp">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCustomer } from '@/api/customer'
import { listFollowsByCustomer, addFollow } from '@/api/follow'
import {
  listContactsByCustomer,
  saveContact,
  updateContact,
  deleteContact,
} from '@/api/contact'
import {
  listOpportunitiesByCustomer,
  saveOpportunity,
  updateOpportunity,
  deleteOpportunity,
} from '@/api/opportunity'

const route = useRoute()
const router = useRouter()
const customerId = route.params.id

const FOLLOW_TYPES = [
  { value: 'PHONE', label: '电话' },
  { value: 'VISIT', label: '拜访' },
  { value: 'WECHAT', label: '微信' },
  { value: 'EMAIL', label: '邮件' },
]
const STAGES = [
  { value: 'INITIAL', label: '初期沟通' },
  { value: 'PROPOSAL', label: '方案报价' },
  { value: 'NEGOTIATION', label: '商务谈判' },
  { value: 'WON', label: '赢单' },
  { value: 'LOST', label: '输单' },
]

const loading = ref(false)
const customer = ref({})
const activeTab = ref('follow')
const follows = ref([])
const contacts = ref([])
const opportunities = ref([])

const followVisible = ref(false)
const savingFollow = ref(false)
const followFormRef = ref(null)
const followForm = reactive({ followType: '', contactId: null, content: '', nextTime: null })
const followRules = {
  followType: [{ required: true, message: '请选择跟进方式', trigger: 'change' }],
  content: [{ required: true, message: '跟进内容不能为空', trigger: 'blur' }],
}

const contactVisible = ref(false)
const contactFormRef = ref(null)
const emptyContact = { id: null, name: '', position: '', phone: '', wechat: '', isPrimary: 0 }
const contactForm = reactive({ ...emptyContact })
const contactRules = {
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  phone: [
    { required: true, message: '手机号不能为空', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' },
  ],
}

const oppVisible = ref(false)
const oppFormRef = ref(null)
const emptyOpp = { id: null, title: '', amount: 0, stage: '', expectedDate: null, remark: '' }
const oppForm = reactive({ ...emptyOpp })
const oppRules = {
  title: [{ required: true, message: '商机名称不能为空', trigger: 'blur' }],
  stage: [{ required: true, message: '请选择阶段', trigger: 'change' }],
}

const loadAll = async () => {
  loading.value = true
  try {
    const [c, f, ct, o] = await Promise.all([
      getCustomer(customerId),
      listFollowsByCustomer(customerId),
      listContactsByCustomer(customerId),
      listOpportunitiesByCustomer(customerId),
    ])
    customer.value = c
    follows.value = f
    contacts.value = ct
    opportunities.value = o
  } finally {
    loading.value = false
  }
}

const handleAddFollow = async () => {
  await followFormRef.value.validate()
  savingFollow.value = true
  try {
    await addFollow({ ...followForm, customerId })
    ElMessage.success('跟进已记录')
    followVisible.value = false
    Object.assign(followForm, { followType: '', contactId: null, content: '', nextTime: null })
    follows.value = await listFollowsByCustomer(customerId)
  } finally {
    savingFollow.value = false
  }
}

const openContactDialog = (row) => {
  Object.assign(contactForm, emptyContact, row || { customerId })
  contactVisible.value = true
}

const handleSaveContact = async () => {
  await contactFormRef.value.validate()
  const payload = { ...contactForm, customerId }
  if (contactForm.id) await updateContact(payload)
  else await saveContact(payload)
  ElMessage.success('保存成功')
  contactVisible.value = false
  contacts.value = await listContactsByCustomer(customerId)
}

const handleDeleteContact = async (row) => {
  await ElMessageBox.confirm(`确定删除联系人「${row.name}」吗？`, '警告', { type: 'warning' })
  await deleteContact(row.id)
  ElMessage.success('删除成功')
  contacts.value = await listContactsByCustomer(customerId)
}

const openOppDialog = (row) => {
  Object.assign(oppForm, emptyOpp, row || {})
  oppVisible.value = true
}

const handleSaveOpp = async () => {
  await oppFormRef.value.validate()
  const payload = { ...oppForm, customerId }
  if (oppForm.id) await updateOpportunity(payload)
  else await saveOpportunity(payload)
  ElMessage.success('保存成功')
  oppVisible.value = false
  opportunities.value = await listOpportunitiesByCustomer(customerId)
}

const handleDeleteOpp = async (row) => {
  await ElMessageBox.confirm(`确定删除商机「${row.title}」吗？`, '警告', { type: 'warning' })
  await deleteOpportunity(row.id)
  ElMessage.success('删除成功')
  opportunities.value = await listOpportunitiesByCustomer(customerId)
}

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 19) : '')
const levelTagType = (l) => ({ A: 'danger', B: 'warning', C: 'info' }[l] || 'info')
const followLabel = (v) => FOLLOW_TYPES.find((t) => t.value === v)?.label || v
const followTagType = (v) => ({ PHONE: 'primary', VISIT: 'success', WECHAT: 'warning', EMAIL: 'info' }[v] || 'info')
const stageLabel = (v) => STAGES.find((s) => s.value === v)?.label || v
const stageTagType = (v) =>
  ({ INITIAL: 'info', PROPOSAL: 'primary', NEGOTIATION: 'warning', WON: 'success', LOST: 'danger' }[v] || 'info')

onMounted(loadAll)
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mb {
  margin-bottom: 14px;
}

.follow-item {
  line-height: 1.8;
}

.follow-content {
  margin-left: 8px;
}

.follow-next {
  font-size: 12px;
  color: #909399;
}
</style>
