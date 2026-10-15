import { request } from './client'

export const getTasks = () => request('/tasks')
export const createTask = (task) => request('/tasks', { method: 'POST', body: task })
export const updateTask = (id, task) => request(`/tasks/${id}`, { method: 'PUT', body: task })
export const deleteTask = (id) => request(`/tasks/${id}`, { method: 'DELETE' })
