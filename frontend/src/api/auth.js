import { request } from './client'

export const login = (email, password) =>
  request('/auth/login', { method: 'POST', body: { email, password }, auth: false })

export const register = (payload) =>
  request('/auth/register', { method: 'POST', body: payload, auth: false })
