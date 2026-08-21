import http from '@/utils/http'

export const pageContacts = (params) => http.get('/contact/page', { params })
export const listContactsByCustomer = (customerId) => http.get(`/contact/list/${customerId}`)
export const saveContact = (data) => http.post('/contact', data)
export const updateContact = (data) => http.put('/contact', data)
export const deleteContact = (id) => http.delete(`/contact/${id}`)
