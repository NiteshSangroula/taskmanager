const TOKEN_KEY = 'token'

export const getToken = () => localStorage.getItem(TOKEN_KEY)
export const setToken = (t) => localStorage.setItem(TOKEN_KEY, t)
export const clearToken = () => localStorage.removeItem(TOKEN_KEY)

export function decodeToken(token) {
  try {
    const part = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(atob(part))
  } catch {
    return null
  }
}

export function isTokenExpired(token) {
  const exp = decodeToken(token)?.exp
  return !exp || exp * 1000 <= Date.now()
}

function forceLogin() {
  clearToken()
  window.location.href = '/login'
}

export async function request(path, { method = 'GET', body, auth = true } = {}) {
  const headers = {}
  if (body !== undefined) headers['Content-Type'] = 'application/json'

  if (auth) {
    const token = getToken()
    if (!token || isTokenExpired(token)) {
      forceLogin()
      throw new Error('Session expired')
    }
    headers.Authorization = `Bearer ${token}`
  }

  const res = await fetch(`/api${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  const type = res.headers.get('content-type') || ''
  const data = res.status === 204
    ? null
    : type.includes('application/json') ? await res.json() : await res.text()

  if (!res.ok) {
    if (auth && (res.status === 401 || res.status === 403)) forceLogin()

    let message = 'Something went wrong'
    let fieldErrors = {}
    if (data && typeof data === 'object') {
      if (typeof data.message === 'string') message = data.message
      else {
        fieldErrors = data
        message = Object.values(data)[0] || 'Validation failed'
      }
    } else if (typeof data === 'string' && data) {
      message = data
    }

    const err = new Error(message)
    err.status = res.status
    err.fieldErrors = fieldErrors
    throw err
  }
  return data
}
