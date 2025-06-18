<template>
  <div class="page-background">
    <div class="festival-detail-page">

      <!-- Imagine -->
      <img :src="`http://localhost:8081/uploads/${festival.imagePath}`" alt="Imagine Festival" class="festival-img" />

      <!-- Nume -->
      <h2>{{ festival.name }}</h2>

      <!-- Info festival -->
      <div class="info-box">
        <p><strong>📍 Locație:</strong> {{ festival.location }}</p>
        <p><strong>📅 Perioadă:</strong> {{ formatDate(festival.startDate) }} – {{ formatDate(festival.endDate) }}</p>
        <p><strong>📝 Descriere:</strong> {{ festival.description }}</p>
        <p><strong>💰 Preț bilet:</strong> {{ festival.price }} RON</p>
      </div>

      <!-- Hartă -->
      <div id="map" class="map"></div>

      <!-- Butoane -->
      <button class="btn" @click="participaLaFestival">🎟 Participă la acest festival</button>
      <button class="btn" @click="cumparaBiletRedirect">💳 Cumpără bilet</button>

    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import L from 'leaflet'
import { loadStripe } from '@stripe/stripe-js'

const route = useRoute()
const festival = ref({})
const mapPoints = ref([])
const stripePromise = loadStripe("pk_test_51RbH2906WexVnL31CkKYB9wIJlgJRVmBlLtBXZhRKXd7ou5Jr3zcqNn6IouZsrzjtC3gCKJnDXHs5CkFRVUC8uaW00eq0kr27X") // înlocuiește cu cheia ta Stripe publică reală

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('ro-RO', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  })
}

onMounted(async () => {
  const id = route.params.id

  try {
    const res = await axios.get(`http://localhost:8081/festivals/${id}`)
    festival.value = res.data

    const points = await axios.get(`http://localhost:8081/map-points/${id}`)
    mapPoints.value = points.data

    const map = L.map('map')
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: 'Map data © OpenStreetMap'
    }).addTo(map)

    mapPoints.value.forEach(point => {
      L.marker([point.latitude, point.longitude], {
        icon: L.divIcon({
          className: 'emoji-icon',
          html: `${point.emoji}<div class='label'>${point.label}</div>`
        })
      }).addTo(map)
    })

    if (mapPoints.value.length === 1) {
      map.setView([mapPoints.value[0].latitude, mapPoints.value[0].longitude], 16)
    } else if (mapPoints.value.length > 1) {
      const bounds = L.latLngBounds(mapPoints.value.map(p => [p.latitude, p.longitude]))
      map.fitBounds(bounds, { padding: [30, 30] })
    } else {
      map.setView([46.770439, 23.591423], 13)
    }

  } catch (err) {
    console.error('Eroare la încărcarea datelor:', err)
  }
})

const participaLaFestival = async () => {
  const userId = localStorage.getItem('userId')
  const festivalId = route.params.id

  if (!userId) {
    alert('Trebuie să fii logat pentru a participa.')
    return
  }

  try {
    await axios.post(`http://localhost:8081/participari/adauga?userId=${userId}&festivalId=${festivalId}`)
    alert('Participarea a fost înregistrată cu succes!')
  } catch (err) {
    console.error('Eroare la participare:', err)
    alert('A apărut o eroare. Încearcă din nou.')
  }
}

const cumparaBiletRedirect = async () => {
  const userId = localStorage.getItem('userId')
  const festivalId = route.params.id

  if (!userId) {
    alert('Trebuie să fii logat pentru a cumpăra bilet.')
    return
  }

  try {
    const response = await axios.post('http://localhost:8081/api/payment/checkout-session', null, {
      params: {
        userId,
        festivalId,
        successUrl: 'http://localhost:5173/payment-success'
      }
    })

    // Salvează pentru confirmarea statusului după plată
    localStorage.setItem('pendingUserId', userId)
    localStorage.setItem('pendingFestivalId', festivalId)

    const stripe = await stripePromise
    window.location.href = response.data.url

  } catch (err) {
    console.error('Eroare la inițierea plății:', err)
    alert('A apărut o problemă. Încearcă din nou.')
  }
}
</script>

<style scoped>
.page-background {
  background: linear-gradient(to bottom, #0d0d0d, #1f0037);
  min-height: 100vh;
  width: 100%;
  padding: 2rem 0;
}

.festival-detail-page {
  max-width: 900px;
  margin: auto;
  padding: 2rem;
  color: white;
  font-family: 'Segoe UI', sans-serif;
}

.festival-img {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 16px;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.4);
  margin-bottom: 1.5rem;
}

h2 {
  text-align: center;
  font-size: 36px;
  font-weight: bold;
  color: #bb86fc;
  text-shadow: 0 0 10px rgba(187, 134, 252, 0.4);
  margin-bottom: 2rem;
}

.info-box p {
  font-size: 15px;
  margin-bottom: 1rem;
  color: #ccc;
}

strong {
  color: #fff;
}

.map {
  height: 400px;
  width: 100%;
  border-radius: 12px;
  margin-top: 2rem;
  box-shadow: 0 0 12px rgba(255, 255, 255, 0.05);
}

.btn {
  background-color: #ef4444;
  color: white;
  padding: 14px 24px;
  font-weight: bold;
  border-radius: 10px;
  border: none;
  display: block;
  margin: 1.2rem auto;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.btn:hover {
  background-color: #dc2626;
}

.emoji-icon {
  font-size: 32px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.label {
  font-size: 12px;
  color: white;
  background-color: black !important;
  padding: 4px 6px;
  border-radius: 6px;
  margin-top: 4px;
  box-shadow: 0 0 3px rgba(255, 255, 255, 0.25);
  display: inline-block;
}
</style>
