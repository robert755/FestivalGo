<template>
  <div class="home-container">
    <!-- Overlay -->
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Sidebar -->
    <aside v-if="isSidebarVisible">
      <h2>FestivalGo</h2>
      <button @click="toggleSidebar">Închide</button>
      <nav>
        <button @click="goTo('/festivals')">Festivaluri</button>
        <button @click="goTo('/my-participations')"> Participările mele</button>
        <button @click="goTo('/chat')"> VibeTalk</button>
        <button class="logout" @click="logout">Logout</button>
        <div v-if="!preferredGenre" class="quiz-link" @click="goTo('/user/quiz-page')">
           Nu știi ce ți se potrivește? Fă testul!
        </div>
      </nav>
    </aside>

    <!-- Main content -->
    <main>
      <button class="menu-toggle" @click="toggleSidebar">☰</button>
      <h1>Salut, {{ username }}!</h1>
      <p class="slogan">Vibe. Muzică. Haos organizat. Tu alegi cum trăiești festivalul!</p>

      <section v-if="festivalulLunii">
        <h2> Festivalul lunii</h2>
        <div class="card highlight" @click="goTo(`/festival/${festivalulLunii.id}`)">
          <img :src="`http://localhost:8081/uploads/${festivalulLunii.imagePath}`" alt="Festival imagine" />
          <div class="card-text">
            <h3>{{ festivalulLunii.name }}</h3>
            <p>{{ festivalulLunii.description }}</p>
          </div>
        </div>
      </section>

      <section v-if="preferredGenre && recomandari.length">
        <h2>🎧 Recomandări pentru tine ({{ preferredGenre }})</h2>
        <div class="recommendations">
          <div class="card" v-for="festival in recomandari" :key="festival.id">
            <img :src="`http://localhost:8081/uploads/${festival.imagePath}`" />
            <div class="card-text">
              <h4>{{ festival.name }}</h4>
              <p>{{ festival.locatie }}</p>
              <button @click="goTo(`/festival/${festival.id}`)">Detalii</button>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import backgroundImage from '../assets/image/welcome-page.png'
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref(localStorage.getItem('username') || 'Utilizator')
const preferredGenre = ref(null)
const userId = localStorage.getItem('userId')
const festivals = ref([])
const recomandari = ref([])
const isSidebarVisible = ref(false)

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
  try {
    const userRes = await fetch(`http://localhost:8081/users/${userId}`)
    const userData = await userRes.json()
    preferredGenre.value = userData.preferredGenre

    const festRes = await fetch('http://localhost:8081/festivals')
    festivals.value = await festRes.json()

    if (preferredGenre.value) {
      recomandari.value = festivals.value.filter(f =>
        f.genre.toLowerCase().includes(preferredGenre.value.toLowerCase())
      )
    }
  } catch (err) {
    console.error('Eroare la încărcarea datelor:', err)
  }
})

const festivalulLunii = computed(() => {
  if (!festivals.value.length) return null
  const future = festivals.value.filter(f => new Date(f.startDate) >= new Date())
  return future.sort((a, b) => new Date(a.startDate) - new Date(b.startDate))[0] || null
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(to bottom, #0d0d0d, #1f0037);
  color: white;
  font-family: 'Segoe UI', sans-serif;
  padding: 20px;
  position: relative;
}

/* Overlay */
.overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 999;
}

/* Sidebar */
aside {
  position: fixed;
  top: 0;
  right: 0;
  width: 280px;
  height: 100vh;
  background-color: #1a1a1d;
  box-shadow: -2px 0 12px rgba(0, 0, 0, 0.8);
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

main {
  max-width: 1000px;
  margin: auto;
}

.menu-toggle {
  background: none;
  border: none;
  font-size: 28px;
  color: white;
  cursor: pointer;
  margin-bottom: 20px;
}

main h1 {
  font-size: 36px;
  text-align: center;
  margin-bottom: 8px;
}

.slogan {
  text-align: center;
  color: #d1d5db;
  margin-bottom: 30px;
}

section h2 {
  font-size: 24px;
  margin-bottom: 16px;
}

.card {
  background-color: rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  overflow: hidden;
  transition: transform 0.2s ease;
  margin-bottom: 24px;
}

.card:hover {
  transform: scale(1.02);
}

.card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.card-text {
  padding: 15px;
}

.card h3, .card h4 {
  font-size: 20px;
  margin: 0 0 8px;
}

.card p {
  color: #a1a1aa;
  font-size: 14px;
}

.card button {
  margin-top: 10px;
  background-color: #9f7aea;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
}

.card button:hover {
  background-color: #7c3aed;
}

.recommendations {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
}
</style>
