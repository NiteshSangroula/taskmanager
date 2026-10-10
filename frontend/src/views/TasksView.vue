<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

function logout() {
  auth.logout()
  router.push({ name: 'login' })
}
</script>

<template>
  <main>
    <h1>My tasks</h1>
    <p>Logged in as {{ auth.email }}</p>
    <button @click="logout">Log out</button>
  </main>
</template>
