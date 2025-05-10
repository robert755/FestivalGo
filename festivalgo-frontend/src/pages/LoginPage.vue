<template>
  <div class="login-page" :style="{ backgroundImage: `url(${backgroundImage})` }">
    <form @submit.prevent="login" class="login-form">
      <h2 class="form-title">Autentificare</h2>

      <input
        v-model="username"
        type="text"
        placeholder="Username"
        required
        class="form-input"
      />
      <input
        v-model="password"
        type="password"
        placeholder="Parolă"
        required
        class="form-input"
      />
      <button type="submit" class="form-button">Autentificare</button>

      <p class="mt-3 text-sm text-center">
        Nu ai cont?
        <router-link to="/register" class="text-blue-600 underline hover:text-blue-800">
          Înregistrează-te aici
        </router-link>
      </p>

      <p v-if="error" class="error-message">{{ error }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

// 🔽 Import corect al imaginii folosind cale relativă
import backgroundImage from '../assets/image/festivalgo-background.webp'

const router = useRouter()

const username = ref('')
const password = ref('')
const error = ref(null)

const login = async () => {
  try {
    const res = await axios.post('http://localhost:8081/users/login', {
      username: username.value,
      password: password.value
    })

    localStorage.setItem('userId', res.data.id)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('role', res.data.role)

    if (res.data.role === 'ADMIN') {
      router.push('/admin')
    } else if (res.data.role === 'USER') {
      router.push('/welcome')
    }
  } catch (err) {
    if (err.response && err.response.status === 429) {
      error.value = 'Prea multe încercări! Încearcă din nou peste un minut.'
    } else if (err.response && err.response.status === 401) {
      error.value = 'Autentificare eșuată. Verifică datele!'
    } else {
      error.value = 'Eroare de rețea sau server.'
    }
    console.error(err)
  }
}
</script>

<style scoped>
.login-page {
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

.login-form {
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

.error-message {
  color: #f87171;
  margin-top: 1rem;
  text-align: center;
  font-weight: 500;
}
</style>
