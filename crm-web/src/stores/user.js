import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, getMe } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({})
  const permissions = ref([])

  async function login(form) {
    const data = await loginApi(form)
    token.value = data.token
    userInfo.value = data.user
    permissions.value = data.permissions || []
    localStorage.setItem('token', data.token)
  }

  async function loadMe() {
    const data = await getMe()
    userInfo.value = { ...userInfo.value, username: data.username, userId: data.userId }
    permissions.value = data.permissions || []
  }

  function hasPermission(code) {
    return permissions.value.includes(code)
  }

  function logout() {
    token.value = ''
    userInfo.value = {}
    permissions.value = []
    localStorage.removeItem('token')
  }

  return { token, userInfo, permissions, login, loadMe, hasPermission, logout }
})
