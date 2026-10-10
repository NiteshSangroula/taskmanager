import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import * as authApi from '@/api/auth'
import { getToken, setToken, clearToken, decodeToken, isTokenExpired } from '@/api/client'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(getToken())

  const isLoggedIn = computed(() => !!token.value && !isTokenExpired(token.value))
  const email = computed(() => (token.value ? decodeToken(token.value)?.sub : null))

  async function login(emailInput, password) {
    const t = await authApi.login(emailInput, password)
    setToken(t)
    token.value = t
  }

  function logout() {
    clearToken()
    token.value = null
  }

  return { token, isLoggedIn, email, login, logout }
})
