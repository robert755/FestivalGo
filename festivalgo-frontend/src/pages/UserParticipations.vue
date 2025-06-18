<template>
  <div class="participari-container">
    <h1 class="titlu">Participările mele</h1>

    <div v-if="participari.length === 0" class="info">
      Nu ești înscris la niciun festival momentan.
    </div>

    <div class="card-container">
      <div
        class="card"
        v-for="p in participari"
        :key="p.id"
      >
        <img
          :src="`http://localhost:8081/uploads/${p.festival.imagePath}`"
          alt="Festival"
          class="card-img"
        />

        <div class="card-info">
          <h2>{{ p.festival?.name || 'Festival șters' }}</h2>
          <p>{{ p.festival?.startDate }} – {{ p.festival?.endDate }}</p>

          <template v-if="p.status === 'PARTICIPA'">
            <button @click="anuleazaParticipare(p.id)">Te-ai răzgândit?</button>
            <button @click="cumparaBilet(p.festival.id, p.user.id)">Cumpără bilet</button>
          </template>

          <template v-else-if="p.status === 'BILET_CUMPARAT'">
            <button disabled>Bilet cumpărat</button>
          </template>

          <template v-else-if="p.status === 'ANULAT'">
            <button @click="cumparaBilet(p.festival.id, p.user.id)">Cumpără bilet</button>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const participari = ref([])

onMounted(async () => {
  const userId = localStorage.getItem('userId')
  if (!userId) return

  try {
    const res = await axios.get(`http://localhost:8081/participari/user/${userId}`)
    participari.value = res.data
  } catch (err) {
    console.error('Eroare la încărcarea participărilor:', err)
  }
})

const anuleazaParticipare = async (id) => {
  if (!confirm("Ești sigur că vrei să anulezi această participare?")) return

  try {
    await axios.put(`http://localhost:8081/participari/anuleaza/${id}`)
    participari.value = participari.value.filter(p => p.id !== id)
  } catch (err) {
    console.error('Eroare la anularea participării:', err)
  }
}

const cumparaBilet = async (festivalId, userId) => {
  try {
    const successUrl = window.location.origin + '/payment-success'

    // ✅ Salvăm local user și festival înainte de redirect
    localStorage.setItem('pendingUserId', userId)
    localStorage.setItem('pendingFestivalId', festivalId)

    const response = await axios.post('http://localhost:8081/api/payment/checkout-session', null, {
      params: { festivalId, userId, successUrl }
    })

    const { url } = response.data
    window.location.href = url
  } catch (error) {
    alert('Eroare la inițierea plății.')
    console.error(error)
  }
}
</script>

<style scoped>
.participari-container {
  padding: 2rem;
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(to bottom, #0d0d0d, #1f0037);
  font-family: 'Segoe UI', sans-serif;
  color: white;
}

.titlu {
  font-size: 36px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 2rem;
  color: #bb86fc;
  text-shadow: 0 0 10px rgba(187, 134, 252, 0.5);
}

.info {
  font-size: 16px;
  color: #888;
  text-align: center;
  margin-top: 3rem;
  font-style: italic;
}

.card-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.card {
  display: flex;
  border-radius: 16px;
  overflow: hidden;
  background-color: rgba(255, 255, 255, 0.05);
  box-shadow: 0 0 16px rgba(187, 134, 252, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  width: 90%;
}

.card:hover {
  transform: scale(1.02);
  box-shadow: 0 0 24px rgba(187, 134, 252, 0.25);
}

.card-img {
  width: 40%;
  height: 200px;
  object-fit: cover;
  filter: brightness(0.9);
}

.card-info {
  flex: 1;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: transparent;
}

.card-info h2 {
  font-size: 22px;
  color: #ffffff;
  margin-bottom: 10px;
}

.card-info p {
  font-size: 14px;
  color: #cccccc;
}

.card-info button {
  margin-top: 1rem;
  background-color: #ef4444;
  border: none;
  color: white;
  padding: 10px 16px;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.card-info button:hover {
  background-color: #dc2626;
}

.card-info button[disabled] {
  background-color: #6b7280;
  cursor: default;
}
</style>
