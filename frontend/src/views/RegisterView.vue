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
  <main class="page narrow">
    <div class="card">
      <h1 class="title">Create account</h1>
      <form class="stack" @submit.prevent="submit">
        <div class="field">
          <label for="username">Username</label>
          <input id="username" v-model="username" type="text" autocomplete="username" />
          <small v-if="fieldErrors.username" class="err">{{ fieldErrors.username }}</small>
        </div>

        <div class="field">
          <label for="email">Email</label>
          <input id="email" v-model="email" type="email" autocomplete="email" />
          <small v-if="fieldErrors.email" class="err">{{ fieldErrors.email }}</small>
        </div>

        <div class="field">
          <label for="password">Password</label>
          <input id="password" v-model="password" type="password" autocomplete="new-password" />
          <small v-if="fieldErrors.password" class="err">{{ fieldErrors.password }}</small>
        </div>

        <p v-if="error && !Object.keys(fieldErrors).length" class="banner">{{ error }}</p>
        <button :disabled="loading">{{ loading ? 'Creating…' : 'Register' }}</button>
      </form>
      <p class="foot">
        <RouterLink to="/login">Already have an account?</RouterLink>
      </p>
    </div>
  </main>
</template>
