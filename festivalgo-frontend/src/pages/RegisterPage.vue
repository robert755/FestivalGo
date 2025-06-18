<template>
  <div class="register-page" :style="{ backgroundImage: `url(${backgroundImage})` }">
    <form @submit.prevent="register" class="register-form">
      <h2 class="form-title">Înregistrare FestivalGo</h2>

      <input v-model="form.username" type="text" placeholder="Username" required class="form-input" />
      <p v-if="errors.username" class="field-error">{{ errors.username }}</p>

      <input v-model="form.email" type="email" placeholder="Email" required class="form-input" />
      <p v-if="errors.email" class="field-error">{{ errors.email }}</p>

      <input v-model="form.password" type="password" placeholder="Parolă(cel putin 6 caractere)" required class="form-input" />
      <p v-if="errors.password" class="field-error">{{ errors.password }}</p>

      <input v-model="form.firstName" type="text" placeholder="Prenume" required class="form-input" />
      <p v-if="errors.firstName" class="field-error">{{ errors.firstName }}</p>

      <input v-model="form.lastName" type="text" placeholder="Nume" required class="form-input" />
      <p v-if="errors.lastName" class="field-error">{{ errors.lastName }}</p>

      <button type="submit" class="form-button">Înregistrează-te</button>

      <p v-if="errors.global" class="error-message">{{ errors.global }}</p>
      <p v-if="success" class="success-message">Cont creat cu succes!</p>
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
const errors = ref({})

const register = async () => {
  try {
    await axios.post('http://localhost:8081/users/register', form.value)
    success.value = true
    errors.value = {}
  } catch (err) {
    success.value = false
    console.log('BACKEND RESPONSE:', err.response?.data)

    const backendData = err.response?.data
    const fieldErrors = {}

    // Dacă este o listă de stringuri "field: mesaj"
    if (Array.isArray(backendData)) {
      backendData.forEach(msg => {
        const [field, ...rest] = msg.split(':')
        const key = field.trim()
        const message = rest.join(':').trim()
        fieldErrors[key] = message
      })
    }

    // Dacă este un obiect de forma { "field": "mesaj" }
    else if (typeof backendData === 'object' && backendData !== null) {
      Object.entries(backendData).forEach(([key, value]) => {
        fieldErrors[key] = value
      })
    }

    // Dacă e altceva neașteptat
    else {
      fieldErrors.global = 'Înregistrarea a eșuat. Verifică datele introduse.'
    }

    errors.value = fieldErrors
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

.field-error {
  color: #f87171;
  font-size: 0.875rem;
  margin-top: -0.75rem;
  margin-bottom: 0.75rem;
  padding-left: 0.25rem;
}
</style>
