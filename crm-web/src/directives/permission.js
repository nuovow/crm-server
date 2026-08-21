import { useUserStore } from '@/stores/user'

// v-permission="'customer:delete'"：无权限则直接移除元素（与后端@RequirePermission同源数据）
export const permission = {
  mounted(el, binding) {
    const userStore = useUserStore()
    if (binding.value && !userStore.hasPermission(binding.value)) {
      el.parentNode?.removeChild(el)
    }
  },
}
