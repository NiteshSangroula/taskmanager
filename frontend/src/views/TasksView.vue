<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getTasks, createTask, updateTask, deleteTask } from '@/api/tasks'
import TaskItem from '@/components/TaskItem.vue'

const auth = useAuthStore()
const router = useRouter()

const tasks = ref([])
const loading = ref(false)
const error = ref('')

const title = ref('')
const description = ref('')
const fieldErrors = ref({})
const adding = ref(false)

async function load() {
  loading.value = true
  error.value = ''
  try {
    tasks.value = await getTasks()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function add() {
  adding.value = true
  error.value = ''
  fieldErrors.value = {}
  try {
    const created = await createTask({ title: title.value, description: description.value })
    tasks.value.push(created)
    title.value = ''
    description.value = ''
  } catch (e) {
    error.value = e.message
    fieldErrors.value = e.fieldErrors || {}
  } finally {
    adding.value = false
  }
}

// These two throw on failure; TaskItem catches and shows the error on that row.
async function save(id, payload) {
  const updated = await updateTask(id, payload)
  const i = tasks.value.findIndex((t) => t.id === id)
  if (i !== -1) tasks.value[i] = updated
}

async function remove(id) {
  await deleteTask(id)
  tasks.value = tasks.value.filter((t) => t.id !== id)
}

function logout() {
  auth.logout()
  router.push({ name: 'login' })
}

onMounted(load)
</script>

<template>
  <main class="page">
    <header class="topbar">
      <h1>My Tasks</h1>
      <span class="who">{{ auth.email }}</span>
      <button class="secondary small" @click="logout">Log out</button>
    </header>

    <form class="card task-form" @submit.prevent="add">
      <div class="field">
        <input v-model="title" placeholder="Title" aria-label="Title" />
        <small v-if="fieldErrors.title" class="err">{{ fieldErrors.title }}</small>
      </div>
      <div class="field">
        <input v-model="description" placeholder="Description" aria-label="Description" />
        <small v-if="fieldErrors.description" class="err">{{ fieldErrors.description }}</small>
      </div>
      <button :disabled="adding">{{ adding ? 'Adding…' : 'Add task' }}</button>
    </form>

    <p v-if="error && !Object.keys(fieldErrors).length" class="banner">{{ error }}</p>
    <p v-if="loading" class="loading">Loading…</p>
    <p v-else-if="!tasks.length" class="empty">No tasks yet. Add your first one above.</p>

    <ul class="task-list">
      <TaskItem v-for="t in tasks" :key="t.id" :task="t" :on-save="save" :on-remove="remove" />
    </ul>
  </main>
</template>
