<template>
  <div class="festival-list">
    <h1 class="text-3xl font-bold mb-6">Festivaluri disponibile</h1>

    <!-- 🔍 Search bar -->
    <input
      v-model="searchQuery"
      placeholder="Caută festival..."
      class="input mb-4"
    />

    <!-- 📅 Date filter -->
    <div class="mb-4">
      <label>Perioada: </label>
      <input type="date" v-model="startDate" class="input mr-2" />
      <input type="date" v-model="endDate" class="input" />
    </div>

    <!-- 🧾 Lista festivaluri -->
    <div class="festival-cards grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div
        v-for="festival in filteredFestivals"
        :key="festival.id"
        class="festival-card border p-4 rounded shadow hover:shadow-lg cursor-pointer"
        @click="goToFestival(festival.id)"
      >
        <img :src="festival.imagePath" alt="Imagine" class="w-full h-48 object-cover rounded mb-2" />
        <h2 class="text-xl font-semibold">{{ festival.name }}</h2>
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

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8081/festivals')
    festivals.value = res.data
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
  max-width: 1000px;
  margin: auto;
  padding: 2rem;
}
.input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 100%;
  max-width: 300px;
}
.festival-card:hover {
  background-color: #f3f4f6;
}
</style>
