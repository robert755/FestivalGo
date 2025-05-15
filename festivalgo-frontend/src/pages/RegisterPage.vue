<template>
  <div class="register-page" :style="{ backgroundImage: `url(${backgroundImage})` }">
    <form @submit.prevent="register" class="register-form">
      <h2 class="form-title">Înregistrare FestivalGo</h2>

      <input v-model="form.username" type="text" placeholder="Username" required class="form-input" />
      <input v-model="form.email" type="email" placeholder="Email" required class="form-input" />
      <input v-model="form.password" type="password" placeholder="Parolă" required class="form-input" />
      <input v-model="form.firstName" type="text" placeholder="Prenume" required class="form-input" />
      <input v-model="form.lastName" type="text" placeholder="Nume" required class="form-input" />

      <button type="submit" class="form-button">Înregistrează-te</button>

      <p v-if="success" class="success-message">Cont creat cu succes!</p>
      <p v-if="error" class="error-message">{{ error }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import backgroundImage from '../assets/image/festivalgo-background.webp'

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
    await axios.post('http://localhost:8081/users/register', form.value)
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
.register-page {
  min-height: 100vh;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  overflow: hidden;
}

.register-form {
  backdrop-filter: blur(20px);
  background-color: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 2rem;
  border-radius: 1.25rem;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.25);
  width: 100%;
  max-width: 400px;
}

.form-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #ffffff;
  text-align: center;
  margin-bottom: 1.5rem;
}

.form-input {
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.75rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 0.5rem;
  font-size: 1rem;
  background-color: rgba(255, 255, 255, 0.25);
  color: white;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}
.form-input::placeholder {
  color: #e0e0e0;
}
.form-input:focus {
  outline: none;
  border-color: #a78bfa;
  box-shadow: 0 0 0 3px rgba(167, 139, 250, 0.3);
  background-color: rgba(255, 255, 255, 0.35);
}

.form-button {
  width: 100%;
  background-color: #6366f1;
  color: white;
  font-weight: 600;
  padding: 0.75rem;
  border-radius: 0.5rem;
  border: none;
  transition: background-color 0.3s ease;
}
.form-button:hover {
  background-color: #4f46e5;
}

.success-message {
  color: #16a34a;
  margin-top: 1rem;
  text-align: center;
  font-weight: 500;
}

.error-message {
  color: #f87171;
  margin-top: 1rem;
  text-align: center;
  font-weight: 500;
}
</style>
