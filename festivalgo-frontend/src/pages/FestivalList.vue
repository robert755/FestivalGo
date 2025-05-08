<template>
  <div class="festival-list">
    <h1 class="titlu">Festivaluri disponibile</h1>

    <!-- 🔍 Search bar -->
    <input
      v-model="searchQuery"
      placeholder="Caută festival..."
      class="input"
    />

    <!-- 📅 Date filter -->
    <div class="date-filter">
      <label>Perioada:</label>
      <input type="date" v-model="startDate" class="input" />
      <input type="date" v-model="endDate" class="input" />
    </div>

    <!-- 🧾 Lista festivaluri -->
    <div class="festivaluri">
      <div
        v-for="festival in filteredFestivals"
        :key="festival.id"
        class="card"
        @click="goToFestival(festival.id)"
      >
      <img :src="`http://localhost:8081/uploads/${festival.imagePath}`" alt="Imagine" class="imagine" />
        <div class="detalii">
          <h2 class="titlu-festival">{{ festival.name }}</h2>
          <p class="data">{{ festival.startDate }} – {{ festival.endDate }}</p>
        </div>
      </div>
    </div>
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

onMounted(async () => {
  try {
    // 1. Ia utilizatorul și genul preferat
    const userRes = await axios.get(`http://localhost:8081/users/${userId}`)
    const preferredGenre = userRes.data.preferredGenre

    // 2. Ia toate festivalurile
    const festRes = await axios.get('http://localhost:8081/festivals')
    const allFestivals = festRes.data

    // 3. Sortează: întâi cele potrivite cu preferința
    festivals.value = [
      ...allFestivals.filter(f => f.genre === preferredGenre),
      ...allFestivals.filter(f => f.genre !== preferredGenre)
    ]
  } catch (err) {
    console.error('Eroare la încărcarea festivalurilor:', err)
  }
})

const filteredFestivals = computed(() => {
  return festivals.value.filter(festival => {
    const matchesSearch = festival.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    const festivalStart = new Date(festival.startDate)
    const festivalEnd = new Date(festival.endDate)

    const matchesDate = (!startDate.value || new Date(startDate.value) <= festivalEnd) &&
                        (!endDate.value || new Date(endDate.value) >= festivalStart)

    return matchesSearch && matchesDate
  })
})

const goToFestival = (id) => {
  router.push(`/festival/${id}`)
}
</script>

<style scoped>
.festival-list {
  max-width: 700px;
  margin: 2rem auto;
  padding: 1rem;
  text-align: center;
}
.titlu {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 1.5rem;
}
.input {
  padding: 0.6rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 100%;
  max-width: 300px;
  margin: 0.5rem;
}
.date-filter {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}
.festivaluri {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}
.card {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  cursor: pointer;
  width: 100%;
  max-width: 300px;
  height: 400px;
  margin: auto;
  display: flex;
  flex-direction: column;
}
.imagine {
  width: 100%;
  height: 250px;
  object-fit: cover;
  object-position: left bottom; /* 🟢 AICI e cheia: focus pe stânga jos */
  border-radius: 0;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}


.detalii {
  padding: 1rem;
}
.titlu-festival {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 0.5rem;
}
.data {
  font-size: 14px;
  color: #555;
}
.card:hover {
  transform: scale(1.02);
  transition: transform 0.2s ease-in-out;
}
</style>
