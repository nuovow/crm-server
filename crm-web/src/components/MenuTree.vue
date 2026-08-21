<template>
  <!-- 递归菜单：目录渲染子菜单，叶子菜单按 path 跳转（router 模式） -->
  <template v-for="item in menus" :key="item.id">
    <el-sub-menu v-if="item.children && item.children.length" :index="String(item.id)">
      <template #title>
        <span>{{ item.name }}</span>
      </template>
      <menu-tree :menus="item.children" />
    </el-sub-menu>
    <el-menu-item v-else :index="item.path">{{ item.name }}</el-menu-item>
  </template>
</template>

<script setup>
defineProps({
  menus: { type: Array, default: () => [] },
})
</script>
