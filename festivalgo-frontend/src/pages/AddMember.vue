<template>
  <div class="add-member-container">
    <h4>Adaugă un membru în grup</h4>

    <select v-model="selectedUserId">
      <option disabled value="">Selectează un utilizator</option>
      <option v-for="user in allUsers" :key="user.id" :value="user.id">
        {{ user.username }}
      </option>
    </select>

    <button @click="addUserToGroup">Adaugă în grup</button>
    <p v-if="successMessage" class="success">{{ successMessage }}</p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const props = defineProps(['groupId'])

const allUsers = ref([])
const selectedUserId = ref("")
const successMessage = ref("")
const errorMessage = ref("")

const loadUsers = async () => {
  try {
    const res = await axios.get('http://localhost:8081/users') // trebuie să ai un endpoint GET /users
    allUsers.value = res.data
  } catch (err) {
    console.error("Eroare la încărcarea utilizatorilor:", err)
  }
}

const addUserToGroup = async () => {
  if (!selectedUserId.value) return
  try {
    const res = await axios.post(`http://localhost:8081/chat/groups/${props.groupId}/add`, null, {
      params: { userId: selectedUserId.value }
    })
    successMessage.value = res.data
    errorMessage.value = ""
  } catch (err) {
    errorMessage.value = "Eroare la adăugare"
    successMessage.value = ""
    console.error(err)
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.add-member-container {
  background-color: #2a2a2d;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(255, 0, 255, 0.1);
  margin-top: 20px;
  color: white;
  font-family: 'Segoe UI', sans-serif;
}

.add-member-container h4 {
  margin-bottom: 10px;
  font-size: 18px;
  color: #e0e0e0;
  text-shadow: 0 0 4px #b300ff;
}

select {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 8px;
  background-color: #3c3c42;
  color: white;
  margin-bottom: 15px;
  font-size: 15px;
  outline: none;
  cursor: pointer;
}

button {
  background-color: #b300ff;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  
}

button:hover {
  background-color: #9900cc;
}

.success {
  margin-top: 10px;
  color: #4ade80; /* verde deschis */
}

.error {
  margin-top: 10px;
  color: #f87171; /* roșu pastelat */
}

</style>
