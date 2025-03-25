<template>
    <div class="login-page">
      <h2 class="text-xl font-bold mb-4">Autentificare FestivalGo</h2>
  
      <form @submit.prevent="login">
        <input v-model="email" type="email" placeholder="Email" required class="input" />
        <input v-model="password" type="password" placeholder="Parolă" required class="input" />
        <button type="submit" class="btn">Autentificare</button>
      </form>
  
      <p v-if="error" class="text-red-600 mt-4">{{ error }}</p>
  
      <p class="mt-3">
        Nu ai cont?
        <router-link to="/register" class="text-blue-500">Înregistrează-te aici</router-link>
      </p>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import axios from 'axios'
  import { useRouter } from 'vue-router'
  
  const email = ref('')
  const password = ref('')
  const error = ref(null)
  const router = useRouter()
  
  const login = async () => {
    try {
      const res = await axios.post('http://localhost:8081/users/login', {
        email: email.value,
        password: password.value
      })
  
      // Salvează userId-ul local
      localStorage.setItem('userId', res.data.id)
  
      // Redirecționează către lista de festivaluri
      router.push('/festivals')
    } catch (err) {
      error.value = 'Autentificare eșuată. Verifică datele.'
    }
  }
  </script>
  
  <style scoped>
  .login-page {
    max-width: 400px;
    margin: auto;
    padding: 2rem;
  }
  .input {
    display: block;
    width: 100%;
    margin-bottom: 1rem;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 6px;
  }
  .btn {
    background-color: #2563eb;
    color: white;
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 6px;
  }
  </style>
  