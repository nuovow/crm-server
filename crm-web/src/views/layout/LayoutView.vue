<template>
  <el-container class="layout">
    <el-aside width="210px" class="layout-aside">
      <div class="layout-logo">CRM 管理系统</div>
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#1f2d3d"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <menu-tree :menus="menus" />
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <span class="header-title">{{ route.meta.title || '工作台' }}</span>
        <el-dropdown @command="handleCommand">
          <span class="header-user">
            {{ userStore.userInfo.nickname || userStore.userInfo.username || '用户' }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import MenuTree from '@/components/MenuTree.vue'
import { getUserTree } from '@/api/menu'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const menus = ref([])
const activeMenu = computed(() => route.path)

onMounted(async () => {
  try {
    menus.value = await getUserTree()
  } catch {
    menus.value = []
  }
  if (!userStore.permissions.length) {
    await userStore.loadMe().catch(() => {})
  }
})

const handleCommand = async (command) => {
  if (command === 'logout') {
    await ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' })
    userStore.logout()
    ElMessage.success('已退出')
    router.push('/login')
  }
}
</script>

<style scoped>
.layout {
  height: 100%;
}

.layout-aside {
  background: #1f2d3d;
}

.layout-logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 17px;
  font-weight: 600;
  letter-spacing: 2px;
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e4e7ed;
  background: #fff;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  color: #303133;
}

.layout-main {
  background: #f0f2f5;
}
</style>
