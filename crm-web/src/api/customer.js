import http from '@/utils/http'

export const pageCustomers = (params) => http.get('/customer/page', { params })
export const getCustomer = (id) => http.get(`/customer/${id}`)
export const saveCustomer = (data) => http.post('/customer', data)
export const updateCustomer = (data) => http.put('/customer', data)
export const deleteCustomer = (id) => http.delete(`/customer/${id}`)
export const exportCustomers = () =>
  http.get('/customer/export', { responseType: 'blob' })
export const importCustomers = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/customer/import', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
