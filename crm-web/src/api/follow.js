import http from '@/utils/http'

export const addFollow = (data) => http.post('/follow', data)
export const listFollowsByCustomer = (customerId) => http.get(`/follow/list/${customerId}`)
