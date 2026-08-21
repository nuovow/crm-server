import http from '@/utils/http'

export const getTree = () => http.get('/menu/tree')
export const getUserTree = () => http.get('/menu/userTree')
