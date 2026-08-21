import http from '@/utils/http'

export const login = (data) => http.post('/user/login', data)
export const register = (data) => http.post('/user/register', data)
export const getMe = () => http.get('/user/me')

export const pageUsers = (params) => http.get('/user/page', { params })
export const updateStatus = (id, status) => http.put(`/user/${id}/status/${status}`)
export const deleteUser = (id) => http.delete(`/user/${id}`)
export const getRoleIds = (id) => http.get(`/user/${id}/roles`)
export const assignRoles = (data) => http.post('/user/assignRoles', data)
