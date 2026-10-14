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
  <main class="page narrow">
    <div class="card">
      <h1 class="title">Log in</h1>
      <form class="stack" @submit.prevent="submit">
        <div class="field">
          <label for="email">Email</label>
          <input id="email" v-model="email" type="email" autocomplete="email" />
          <small v-if="fieldErrors.email" class="err">{{ fieldErrors.email }}</small>
        </div>

        <div class="field">
          <label for="password">Password</label>
          <input id="password" v-model="password" type="password" autocomplete="current-password" />
          <small v-if="fieldErrors.password" class="err">{{ fieldErrors.password }}</small>
        </div>

        <p v-if="error && !Object.keys(fieldErrors).length" class="banner">{{ error }}</p>
        <button :disabled="loading">{{ loading ? 'Logging in…' : 'Log in' }}</button>
      </form>
      <p class="foot">
        <RouterLink to="/register">Create an account</RouterLink>
      </p>
    </div>
  </main>
</template>
