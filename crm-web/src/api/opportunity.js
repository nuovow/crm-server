import http from '@/utils/http'

export const pageOpportunities = (params) => http.get('/opportunity/page', { params })
export const listOpportunitiesByCustomer = (customerId) => http.get(`/opportunity/list/${customerId}`)
export const saveOpportunity = (data) => http.post('/opportunity', data)
export const updateOpportunity = (data) => http.put('/opportunity', data)
export const deleteOpportunity = (id) => http.delete(`/opportunity/${id}`)
