<template>
  <div class="participari-container">
    <!-- Overlay -->
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Sidebar -->
    <aside v-if="isSidebarVisible">
      <h2>FestivalGo</h2>
      <button @click="toggleSidebar">Închide</button>
      <nav>
        <button @click="goTo('/welcome')">Acasă</button> 
        <button @click="goTo('/festivals')">Festivaluri</button>
        <button @click="goTo('/chat')">VibeTalk</button>
        <button class="logout" @click="logout">Logout</button>
        <div v-if="!preferredGenre" class="quiz-link" @click="goTo('/user/quiz-page')">
          Nu știi ce ți se potrivește? Fă testul!
        </div>
      </nav>
    </aside>

    <!-- Conținut principal -->
    <main>
      <button class="menu-toggle" @click="toggleSidebar">☰</button>

      <h1 class="titlu">Participările mele</h1>

      <div v-if="participari.length === 0" class="info">
        Nu ești înscris la niciun festival momentan.
      </div>

      <div class="card-container">
        <div class="card" v-for="p in participari" :key="p.id">
          <img :src="`http://localhost:8081/uploads/${p.festival.imagePath}`" alt="Festival" class="card-img" />

          <div class="card-info">
            <h2>{{ p.festival?.name || 'Festival șters' }}</h2>
            <p>{{ p.festival?.startDate }} – {{ p.festival?.endDate }}</p>

            <template v-if="p.status === 'PARTICIPA'">
              <button @click="anuleazaParticipare(p.id)">Te-ai răzgândit?</button>
              <button @click="cumparaBilet(p.festival.id, p.user.id)">Cumpără bilet</button>
            </template>

            <template v-else-if="p.status === 'CUMPARAT'">
              <button disabled>Bilet cumpărat</button>
            </template>

            <template v-else-if="p.status === 'ANULAT'">
              <button @click="cumparaBilet(p.festival.id, p.user.id)">Cumpără bilet</button>
            </template>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const participari = ref([])
const router = useRouter()
const isSidebarVisible = ref(false)
const preferredGenre = ref(null)
const userId = localStorage.getItem('userId')

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

onMounted(async () => {
  if (!userId) return

  try {
    const res = await axios.get(`http://localhost:8081/participari/user/${userId}`)
    participari.value = res.data

    const userRes = await axios.get(`http://localhost:8081/users/${userId}`)
    preferredGenre.value = userRes.data.preferredGenre
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
  min-height: 100vh;
  background: linear-gradient(to bottom, #0d0d0d, #1f0037);
  color: white;
  font-family: 'Segoe UI', sans-serif;
  position: relative;
  padding: 20px;
}

/* Sidebar și overlay */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 999;
}

aside {
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

aside h2 {
  font-size: 24px;
  color: #bb86fc;
  text-align: center;
}

aside nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

aside button {
  background-color: #2d2d30;
  color: white;
  border: none;
  padding: 12px;
  font-size: 16px;
  border-radius: 10px;
  cursor: pointer;
}

aside button:hover {
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

/* Conținut principal */
main {
  max-width: 1000px;
  margin: auto;
  padding-left: 300px;
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
