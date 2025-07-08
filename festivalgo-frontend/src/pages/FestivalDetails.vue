<template>
  <div class="festival-page-container">
    <!-- Overlay -->
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Sidebar -->
    <aside v-if="isSidebarVisible" class="main-sidebar">
      <h2>FestivalGo</h2>
      <button @click="toggleSidebar">Închide</button>
      <nav>
        <button @click="goTo('/welcome')">Acasă</button>
        <button @click="goTo('/festivals')">Festivaluri</button>
        <button @click="goTo('/my-participations')">Participările mele</button>
        <button @click="goTo('/chat')">VibeTalk</button>
        <button class="logout" @click="logout">Logout</button>
        <div v-if="!preferredGenre" class="quiz-link" @click="goTo('/user/quiz-page')">
          Nu știi ce ți se potrivește? Fă testul!
        </div>
      </nav>
    </aside>

    <!-- Conținut principal -->
    <main class="page-background">
      <button class="menu-toggle" @click="toggleSidebar">☰</button>

      <div class="festival-detail-page">
        <!-- Imagine festival -->
        <img :src="`http://localhost:8081/uploads/${festival.imagePath}`" alt="Imagine Festival" class="festival-img" />

        <!-- Titlu -->
        <h2>{{ festival.name }}</h2>

        <!-- Informații -->
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
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import L from 'leaflet'
import { loadStripe } from '@stripe/stripe-js'

const router = useRouter()
const route = useRoute()
const festival = ref({})
const mapPoints = ref([])
const stripePromise = loadStripe("pk_test_51RbH2906WexVnL31CkKYB9wIJlgJRVmBlLtBXZhRKXd7ou5Jr3zcqNn6IouZsrzjtC3gCKJnDXHs5CkFRVUC8uaW00eq0kr27X")

const isSidebarVisible = ref(false)
const preferredGenre = ref(null)

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value
}

const goTo = (path) => {
  router.push(path)
}

const logout = () => {
  localStorage.clear()
  router.push('/login')
}

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
  const userId = localStorage.getItem('userId')

  try {
    const res = await axios.get(`http://localhost:8081/festivals/${id}`)
    festival.value = res.data

    const userRes = await axios.get(`http://localhost:8081/users/${userId}`)
    preferredGenre.value = userRes.data.preferredGenre

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
.festival-page-container {
  display: flex;
  position: relative;
  min-height: 100vh;
  background: linear-gradient(to bottom, #0d0d0d, #1f0037);
  color: white;
  font-family: 'Segoe UI', sans-serif;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 999;
}

.main-sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 280px;
  height: 100vh;
  background-color: #1a1a1d;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.8);
  padding: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.main-sidebar h2 {
  font-size: 24px;
  color: #bb86fc;
  text-align: center;
}

.main-sidebar nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.main-sidebar button {
  background-color: #2d2d30;
  color: white;
  border: none;
  padding: 12px;
  font-size: 16px;
  border-radius: 10px;
  cursor: pointer;
}

.main-sidebar button:hover {
  background-color: #3f3f46;
}

.logout {
  color: #f87171;
}

.quiz-link {
  background-color: #9f7aea;
  color: white;
  padding: 10px;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
}

.menu-toggle {
  position: fixed;
  top: 20px;
  left: 20px;
  z-index: 1100;
  background: none;
  border: none;
  font-size: 28px;
  color: white;
  cursor: pointer;
}

.page-background {
  padding: 2rem;
  padding-left: 300px;
  width: 100%;
}

.festival-detail-page {
  max-width: 900px;
  margin: auto;
  padding: 2rem;
}

.festival-img {
  display: block;
  max-width: 50%; /* jumătate din lățimea containerului titlului */
  height: auto;
  margin: 0 auto 2rem auto; /* centrare + spațiu jos */
  border-radius: 16px;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.4);
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
</style>
<style>
.emoji-icon {
  font-size: 32px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.label {
  font-size: 20px;
  color: white;
  padding: 2px 4px;
  border-radius: 4px;
  margin-top: 2px;
  background-color: transparent;
  display: inline-block;
  text-shadow: 0 0 2px rgba(0, 0, 0, 0.5);
  
}
</style>
