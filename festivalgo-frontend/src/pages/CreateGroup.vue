<template>
  <div class="create-group-container">
    <input v-model="groupName" placeholder="Nume grup" />
    <button @click="createGroup">Creează</button>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
const emit = defineEmits(['grupCreat'])

const groupName = ref("")
const userId = localStorage.getItem("userId")
const errorMessage = ref("")

const createGroup = async () => {
  if (!groupName.value.trim()) {
    errorMessage.value = "Introdu un nume valid"
    return
  }

  try {
    await axios.post(`http://localhost:8081/chat/groups/create`, null, {
      params: {
        name: groupName.value,
        creatorId: userId
      }
    })
    emit('grupCreat')
    groupName.value = ""
    errorMessage.value = ""
  } catch (err) {
    console.error("Eroare creare grup:", err)
    errorMessage.value = "A apărut o eroare"
  }
}
</script>

<style scoped>
.create-group-container {
  margin-bottom: 10px;
}
input {
  width: 100%;
  padding: 8px;
  margin-bottom: 5px;
}
button {
  padding: 8px 12px;
}
.error {
  color: red;
  margin-top: 5px;
}
</style>
