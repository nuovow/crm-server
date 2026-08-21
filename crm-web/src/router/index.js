import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/LoginView.vue'),
  },
  {
    path: '/',
    component: () => import('@/views/layout/LayoutView.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue'),
        meta: { title: '数据看板' },
      },
      {
        path: 'customer',
        name: 'customer',
        component: () => import('@/views/customer/CustomerView.vue'),
        meta: { title: '客户列表' },
      },
      {
        path: 'customer/detail/:id',
        name: 'customerDetail',
        component: () => import('@/views/customer/CustomerDetailView.vue'),
        meta: { title: '客户详情' },
      },
      {
        path: 'user',
        name: 'user',
        component: () => import('@/views/system/UserView.vue'),
        meta: { title: '用户列表' },
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('@/views/system/RoleView.vue'),
        meta: { title: '角色管理' },
      },
      {
        path: 'dict',
        name: 'dict',
        component: () => import('@/views/system/DictView.vue'),
        meta: { title: '字典管理' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard',
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

const WHITE_LIST = ['/login']

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (WHITE_LIST.includes(to.path)) {
    return token ? { path: '/' } : true
  }
  if (!token) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  return true
})

export default router
