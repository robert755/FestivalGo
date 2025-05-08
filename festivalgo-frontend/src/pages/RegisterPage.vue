<template>
  <div class="page-wrapper">
    <div class="form-container">
      <h2 class="form-title">Înregistrare FestivalGo</h2>

      <form @submit.prevent="register" class="form-fields">
        <input v-model="form.username" type="text" placeholder="Username" required class="form-input" />
        <input v-model="form.email" type="email" placeholder="Email" required class="form-input" />
        <input v-model="form.password" type="password" placeholder="Parolă" required class="form-input" />
        <input v-model="form.firstName" type="text" placeholder="Prenume" required class="form-input" />
        <input v-model="form.lastName" type="text" placeholder="Nume" required class="form-input" />

        <button type="submit" class="submit-button">Înregistrează-te</button>
      </form>

      <p v-if="success" class="success-message">Cont creat cu succes!</p>
      <p v-if="error" class="error-message">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const form = ref({
  username: '',
  email: '',
  password: '',
  firstName: '',
  lastName: ''
})

const success = ref(false)
const error = ref(null)

const register = async () => {
  try {
    const res = await axios.post('http://localhost:8081/users/register', form.value)
    success.value = true
    error.value = null
  } catch (err) {
    success.value = false
    error.value = 'Înregistrarea a eșuat. Verifică datele introduse.'
    console.error(err)
  }
}
</script>

<style scoped>
.page-wrapper {
  min-height: 100vh;
  background-color: #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-container {
  background-color: white;
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.form-title {
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  color: #1f2937;
  margin-bottom: 1.5rem;
}

.form-fields {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  background-color: #f9fafb;
  font-size: 1rem;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}
.form-input:focus {
  outline: none;
  border-color: #6366f1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
  background-color: white;
}

.submit-button {
  width: 100%;
  background-color: #6366f1;
  color: white;
  font-weight: 600;
  padding: 0.75rem;
  border-radius: 0.5rem;
  border: none;
  transition: background-color 0.3s ease;
}
.submit-button:hover {
  background-color: #4f46e5;
}

.success-message {
  color: #16a34a;
  margin-top: 1rem;
  text-align: center;
  font-weight: 500;
}

.error-message {
  color: #dc2626;
  margin-top: 1rem;
  text-align: center;
  font-weight: 500;
}
</style>
