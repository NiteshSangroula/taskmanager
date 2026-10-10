<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/auth'

const router = useRouter()

const username = ref('')
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
    await register({
      username: username.value,
      email: email.value,
      password: password.value,
    })
    router.push({ name: 'login' })
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
    <h1>Create account</h1>
    <form @submit.prevent="submit">
      <label>Username
        <input v-model="username" type="text" autocomplete="username" />
      </label>
      <small v-if="fieldErrors.username">{{ fieldErrors.username }}</small>

      <label>Email
        <input v-model="email" type="email" autocomplete="email" />
      </label>
      <small v-if="fieldErrors.email">{{ fieldErrors.email }}</small>

      <label>Password
        <input v-model="password" type="password" autocomplete="new-password" />
      </label>
      <small v-if="fieldErrors.password">{{ fieldErrors.password }}</small>

      <p v-if="error && !Object.keys(fieldErrors).length">{{ error }}</p>
      <button :disabled="loading">{{ loading ? 'Creating…' : 'Register' }}</button>
    </form>
    <RouterLink to="/login">Already have an account?</RouterLink>
  </main>
</template>
