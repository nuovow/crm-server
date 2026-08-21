import http from '@/utils/http'

export const getOverview = () => http.get('/stats/overview')
export const getCustomerLevel = () => http.get('/stats/customer/level')
export const getCustomerSource = () => http.get('/stats/customer/source')
export const getFunnel = () => http.get('/stats/funnel')
