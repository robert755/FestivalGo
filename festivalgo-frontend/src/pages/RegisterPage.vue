<template>
    <div class="register-page">
      <h2 class="text-xl font-bold mb-4">Înregistrare FestivalGo</h2>
  
      <form @submit.prevent="register">
        <input v-model="form.username" type="text" placeholder="Username" required class="input" />
        <input v-model="form.email" type="email" placeholder="Email" required class="input" />
        <input v-model="form.password" type="password" placeholder="Parolă" required class="input" />
        <input v-model="form.firstName" type="text" placeholder="Prenume" required class="input" />
        <input v-model="form.lastName" type="text" placeholder="Nume" required class="input" />
  
        <button type="submit" class="btn">Înregistrează-te</button>
      </form>
  
      <p v-if="success" class="text-green-600 mt-4">Cont creat cu succes! 🎉</p>
      <p v-if="error" class="text-red-600 mt-4">{{ error }}</p>
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
      console.error(err) // 🔥 te rog adaugă asta dacă nu există
    }
  }
  </script>
  
  <style scoped>
  .register-page {
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
  