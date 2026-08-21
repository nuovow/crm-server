<template>
  <!-- 递归菜单：目录渲染子菜单，叶子菜单按 path 跳转（router 模式）；type=2 是按钮权限，不进导航 -->
  <template v-for="item in visibleMenus" :key="item.id">
    <el-sub-menu v-if="hasChildMenu(item)" :index="String(item.id)">
      <template #title>
        <span>{{ item.name }}</span>
      </template>
      <menu-tree :menus="item.children" />
    </el-sub-menu>
    <el-menu-item v-else :index="item.path">{{ item.name }}</el-menu-item>
  </template>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  menus: { type: Array, default: () => [] },
})

const visibleMenus = computed(() => props.menus.filter((m) => m.type !== 2))

// 挂着按钮(type=2)子节点的叶子菜单不算目录——过滤后无菜单子节点就按叶子渲染，点击才能跳转
const hasChildMenu = (item) => item.children?.some((c) => c.type !== 2) ?? false
</script>
