import http from '@/utils/http'

export const listByType = (type) => http.get(`/dict/type/${type}`)
export const listDicts = () => http.get('/dict/list')
export const saveDict = (data) => http.post('/dict', data)
export const updateDict = (data) => http.put('/dict', data)
export const deleteDict = (id) => http.delete(`/dict/${id}`)
