<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  task: { type: Object, required: true },
  onSave: { type: Function, required: true },   // async (id, payload) => void, throws on failure
  onRemove: { type: Function, required: true }, // async (id) => void
})

const editing = ref(false)
const draft = ref({ title: '', description: '' })
const error = ref('')
const fieldErrors = ref({})
const busy = ref(false)

const isDone = computed(() => props.task.status === 'DONE')

function startEdit() {
  draft.value = { title: props.task.title, description: props.task.description }
  error.value = ''
  fieldErrors.value = {}
  editing.value = true
}

async function run(fn) {
  busy.value = true
  error.value = ''
  fieldErrors.value = {}
  try {
    await fn()
    return true
  } catch (e) {
    error.value = e.message
    fieldErrors.value = e.fieldErrors || {}
    return false
  } finally {
    busy.value = false
  }
}

async function save() {
  const ok = await run(() =>
    props.onSave(props.task.id, {
      title: draft.value.title,
      description: draft.value.description,
      status: props.task.status,
    }),
  )
  if (ok) editing.value = false
}

function toggle() {
  return run(() =>
    props.onSave(props.task.id, {
      title: props.task.title,
      description: props.task.description,
      status: isDone.value ? 'TODO' : 'DONE',
    }),
  )
}

function remove() {
  return run(() => props.onRemove(props.task.id))
}
</script>

<template>
  <li>
    <template v-if="!editing">
      <input type="checkbox" :checked="isDone" :disabled="busy" @change="toggle" />
      <strong :style="{ textDecoration: isDone ? 'line-through' : 'none' }">{{ task.title }}</strong>
      <span> – {{ task.description }}</span>
      <button :disabled="busy" @click="startEdit">Edit</button>
      <button :disabled="busy" @click="remove">Delete</button>
    </template>

    <template v-else>
      <input v-model="draft.title" placeholder="Title" />
      <small v-if="fieldErrors.title">{{ fieldErrors.title }}</small>
      <input v-model="draft.description" placeholder="Description" />
      <small v-if="fieldErrors.description">{{ fieldErrors.description }}</small>
      <button :disabled="busy" @click="save">Save</button>
      <button :disabled="busy" @click="editing = false">Cancel</button>
    </template>

    <p v-if="error && !Object.keys(fieldErrors).length">{{ error }}</p>
  </li>
</template>
