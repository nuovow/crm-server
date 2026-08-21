import http from '@/utils/http'

export const listRoles = () => http.get('/role/list')
export const saveRole = (data) => http.post('/role', data)
export const updateRole = (data) => http.put('/role', data)
export const deleteRole = (id) => http.delete(`/role/${id}`)
export const getMenuIds = (id) => http.get(`/role/${id}/menus`)
export const assignMenus = (data) => http.post('/role/assignMenus', data)
