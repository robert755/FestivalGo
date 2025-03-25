<template>
  <div class="login-page">
    <h2 class="text-xl font-bold mb-4">Autentificare</h2>

    <form @submit.prevent="login">
      <input
        v-model="username"
        type="text"
        placeholder="Username"
        required
        class="input"
      />
      <input
        v-model="password"
        type="password"
        placeholder="Parolă"
        required
        class="input"
      />
      <button type="submit" class="btn">Autentificare</button>
    </form>

    <!-- 🔹 Link către înregistrare -->
    <p class="mt-3 text-sm">
      Nu ai cont?
      <router-link to="/register" class="text-blue-600 underline hover:text-blue-800">
        Înregistrează-te aici
      </router-link>
    </p>

    <!-- 🔺 Mesaj de eroare -->
    <p v-if="error" class="text-red-600 mt-4">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

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

    // ✅ Salvăm în localStorage
    localStorage.setItem('userId', res.data.id)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('role', res.data.role)

    // 🔁 Redirecționare către pagina cu festivaluri
    router.push('/festivals')
  } catch (err) {
    error.value = 'Autentificare eșuată. Verifică datele!'
    console.error(err)
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
