<template>
  <div class="success-container">
    <h1>✅ Plata a fost finalizată cu succes!</h1>
    <p>Se confirmă participarea ta la festival...</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(async () => {
  const userId = localStorage.getItem('pendingUserId')
  const festivalId = localStorage.getItem('pendingFestivalId')

  if (!userId || !festivalId) {
    alert("Nu s-au găsit datele de plată.")
    return
  }

  try {
    await axios.post('http://localhost:8081/api/payment/finalize', {
      userId: parseInt(userId),
      festivalId: parseInt(festivalId)
    })

    alert("✅ Biletul a fost confirmat! Vezi festivalul în lista ta de participări.")
    
    localStorage.removeItem('pendingUserId')
    localStorage.removeItem('pendingFestivalId')

    setTimeout(() => {
      router.push('my-participations')
    }, 2500)

  } catch (err) {
    console.error("Eroare la confirmarea biletului:", err)
    alert("A apărut o eroare la finalizarea participării.")
  }
})
</script>

<style scoped>
.success-container {
  max-width: 600px;
  margin: auto;
  padding: 3rem;
  text-align: center;
  color: #22c55e;
  font-family: 'Segoe UI', sans-serif;
}

h1 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1.2rem;
  color: #ccc;
}
</style>
