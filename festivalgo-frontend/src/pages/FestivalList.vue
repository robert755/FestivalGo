<template>
  <div class="explorer-container">
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Sidebar -->
    <aside v-if="isSidebarVisible" class="main-sidebar">
      <h2>FestivalGo</h2>
      <button @click="toggleSidebar">Închide</button>
      <nav>
        <button @click="goTo('/welcome')">Acasă</button>
        <button @click="goTo('/my-participations')">Participările mele</button>
        <button @click="goTo('/chat')">VibeTalk</button>
        <button class="logout" @click="logout">Logout</button>
        <div v-if="!preferredGenre" class="quiz-link" @click="goTo('/user/quiz-page')">
          Nu știi ce ți se potrivește? Fă testul!
        </div>
      </nav>
    </aside>

    <!-- Conținut principal -->
    <main class="festival-page">
      <button class="menu-toggle" @click="toggleSidebar">☰</button>

      <h1 class="title">🎶 Festival Explorer</h1>

      <input v-model="searchQuery" placeholder="Caută festival..." class="search" />

      <div class="date-filter">
        <input type="date" v-model="startDate" class="date-input" />
        <input type="date" v-model="endDate" class="date-input" />
      </div>

      <div v-for="vibe in vibes" :key="vibe" class="vibe-section">
        <h2 class="vibe-title">{{ vibeLabels[vibe] }}</h2>
        <div v-if="festivalsByVibe[vibe].length > 0" class="festival-row">
          <div
            v-for="festival in festivalsByVibe[vibe]"
            :key="festival.id"
            class="tile"
            @click="goToFestival(festival.id)"
          >
            <img :src="`http://localhost:8081/uploads/${festival.imagePath}`" class="tile-img" alt="Festival" />
            <div class="tile-info">
              <h3>{{ festival.name }}</h3>
              <p>{{ formatDate(festival.startDate) }} – {{ formatDate(festival.endDate) }}</p>
            </div>
          </div>
        </div>
        <p v-else class="no-results">Niciun festival în această perioadă.</p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const festivals = ref([])
const searchQuery = ref('')
const startDate = ref('')
const endDate = ref('')
const userId = localStorage.getItem('userId')
const preferredGenre = ref(null)
const isSidebarVisible = ref(false)

const vibes = ['relax', 'energetic', 'underground']

const vibeLabels = {
  relax: ' Relax & Sunset Vibes',
  energetic: ' Energetic & Party Vibes',
  underground: 'Underground & Indie'
}

const vibeMap = {
  JAZZ: 'relax',
  ROCK: 'energetic',
  EDM: 'energetic',
  POP: 'energetic',
  URBAN: 'underground',
  FOLK: 'relax',
  INDIE: 'underground'
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
  try {
    const resUser = await axios.get(`http://localhost:8081/users/${userId}`)
    preferredGenre.value = resUser.data.preferredGenre

    const resF = await axios.get('http://localhost:8081/festivals')
    const allFestivals = resF.data

    festivals.value = [
      ...allFestivals.filter(f => f.genre === preferredGenre.value),
      ...allFestivals.filter(f => f.genre !== preferredGenre.value)
    ]
  } catch (err) {
    console.error('Eroare la încărcare festivaluri sau user:', err)
  }
})

const festivalsByVibe = computed(() => {
  const grouped = {
    relax: [],
    energetic: [],
    underground: []
  }

  const today = new Date()
  today.setHours(0, 0, 0, 0)

  for (const fest of festivals.value) {
    const festStart = new Date(fest.startDate)
    const festEnd = new Date(fest.endDate)

    const isInFuture = festEnd >= today
    const matchesSearch = fest.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesDate =
      isInFuture &&
      (!startDate.value || new Date(startDate.value) <= festEnd) &&
      (!endDate.value || new Date(endDate.value) >= festStart)

    const vibe = vibeMap[fest.genre] || 'relax'

    if (matchesSearch && matchesDate) {
      grouped[vibe].push(fest)
    }
  }

  return grouped
})

const goToFestival = (id) => {
  router.push(`/festival/${id}`)
}

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
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.explorer-container {
  display: flex;
  position: relative;
  min-height: 100vh;
  background: linear-gradient(to bottom, #0d0d0d, #1f0037);
  color: white;
  font-family: 'Segoe UI', sans-serif;
  overflow-x: hidden;
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

.festival-page {
  padding: 2rem;
  padding-left: 300px;
  width: calc(100vw - 300px);
  max-width: 100%;
  overflow-x: hidden;
}

.title {
  font-size: 36px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 2rem;
  color: #bb86fc;
  text-shadow: 0 0 10px rgba(187, 134, 252, 0.5);
}

.search {
  display: block;
  margin: 0 auto 1.5rem;
  padding: 0.75rem 1rem;
  border-radius: 12px;
  border: none;
  background-color: #2c2c3c;
  color: white;
  max-width: 400px;
  width: 100%;
  font-size: 15px;
  box-shadow: 0 0 8px rgba(187, 134, 252, 0.1);
}

.date-filter {
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
  margin-bottom: 2.5rem;
}

.date-input {
  padding: 0.7rem 1rem;
  border-radius: 12px;
  border: none;
  background-color: #2c2c3c;
  color: white;
  font-size: 15px;
  box-shadow: inset 0 0 5px rgba(255, 255, 255, 0.05);
}

.vibe-section {
  margin-bottom: 3rem;
}

.vibe-title {
  font-size: 24px;
  margin-bottom: 1rem;
  color: #c084fc;
  text-shadow: 0 0 6px #a855f7aa;
}

.festival-row {
  display: flex;
  gap: 1rem;
  overflow-x: auto;
  padding-bottom: 1rem;
  scrollbar-width: thin;
}

.festival-row::-webkit-scrollbar {
  height: 8px;
}

.festival-row::-webkit-scrollbar-thumb {
  background: #7f5af055;
  border-radius: 4px;
}

.tile {
  min-width: 220px;
  background-color: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 0 12px rgba(127, 90, 240, 0.2);
  flex-shrink: 0;
}

.tile:hover {
  transform: scale(1.05);
  box-shadow: 0 0 24px rgba(187, 134, 252, 0.4);
}

.tile-img {
  width: 100%;
  height: 140px;
  object-fit: cover;
  filter: brightness(0.9);
}

.tile-info {
  padding: 0.8rem;
}

.tile-info h3 {
  font-size: 16px;
  margin-bottom: 0.4rem;
  color: #ffffff;
}

.tile-info p {
  font-size: 13px;
  color: #cccccc;
}

.no-results {
  color: #888;
  text-align: center;
  font-style: italic;
  margin-top: 1rem;
}
</style>
