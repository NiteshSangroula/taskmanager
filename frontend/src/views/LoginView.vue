<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')
const fieldErrors = ref({})

async function submit() {
  loading.value = true
  error.value = ''
  fieldErrors.value = {}
  try {
    await auth.login(email.value, password.value)
    router.push({ name: 'tasks' })
  } catch (e) {
    error.value = e.message
    fieldErrors.value = e.fieldErrors || {}
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main>
    <h1>Log in</h1>
    <form @submit.prevent="submit">
      <label>Email
        <input v-model="email" type="email" autocomplete="email" />
      </label>
      <small v-if="fieldErrors.email">{{ fieldErrors.email }}</small>

      <label>Password
        <input v-model="password" type="password" autocomplete="current-password" />
      </label>
      <small v-if="fieldErrors.password">{{ fieldErrors.password }}</small>

      <p v-if="error && !Object.keys(fieldErrors).length">{{ error }}</p>
      <button :disabled="loading">{{ loading ? 'Logging in…' : 'Log in' }}</button>
    </form>
    <RouterLink to="/register">Create an account</RouterLink>
  </main>
</template>
