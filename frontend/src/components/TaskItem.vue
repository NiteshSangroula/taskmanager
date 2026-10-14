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
  <li class="task card" :class="{ done: isDone }">
    <template v-if="!editing">
      <input type="checkbox" :checked="isDone" :disabled="busy" aria-label="Done" @change="toggle" />
      <div class="text">
        <strong class="name">{{ task.title }}</strong>
        <span class="desc">{{ task.description }}</span>
      </div>
      <div class="actions">
        <button class="secondary small" :disabled="busy" @click="startEdit">Edit</button>
        <button class="danger small" :disabled="busy" @click="remove">Delete</button>
      </div>
    </template>

    <template v-else>
      <div class="edit">
        <div class="field">
          <input v-model="draft.title" placeholder="Title" aria-label="Title" />
          <small v-if="fieldErrors.title" class="err">{{ fieldErrors.title }}</small>
        </div>
        <div class="field">
          <input v-model="draft.description" placeholder="Description" aria-label="Description" />
          <small v-if="fieldErrors.description" class="err">{{ fieldErrors.description }}</small>
        </div>
        <div class="actions">
          <button class="small" :disabled="busy" @click="save">Save</button>
          <button class="secondary small" :disabled="busy" @click="editing = false">Cancel</button>
        </div>
      </div>
    </template>

    <p v-if="error && !Object.keys(fieldErrors).length" class="banner full">{{ error }}</p>
  </li>
</template>

<style scoped>
.task {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
  padding: 1rem 1.2rem;
  box-shadow: 0 4px 0 var(--wood-dark), 0 8px 14px rgba(0, 0, 0, .4);
}

.text {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: .2rem;
}

.name {
  font-family: var(--font-head);
  font-size: 1.05rem;
}

.desc {
  color: var(--ink-soft);
  overflow-wrap: anywhere;
}

.actions {
  display: flex;
  gap: .5rem;
}

.edit {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: .7rem;
}

.full {
  flex-basis: 100%;
}

.task.done {
  background: var(--paper-dark);
}

.task.done .name {
  text-decoration: line-through;
}

.task.done .name,
.task.done .desc {
  opacity: .6;
}
</style>
