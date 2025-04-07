<template>
    <div class="festival-detail-page">
      <h2 class="text-2xl font-bold mb-4">{{ festival.name }}</h2>
      <img :src="festival.imagePath" alt="Imagine festival" class="w-full max-h-96 object-cover mb-4 rounded" />
      <p class="mb-2"><strong>Locație:</strong> {{ festival.location }}</p>
      <p class="mb-2"><strong>Perioadă:</strong> {{ festival.startDate }} - {{ festival.endDate }}</p>
      <p class="mb-4"><strong>Descriere:</strong> {{ festival.description }}</p>
  
      <div id="map" class="map"></div>
  
      <button class="btn mt-4" @click="participaLaFestival">Participă la acest festival</button>
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue'
  import { useRoute } from 'vue-router'
  import axios from 'axios'
  import L from 'leaflet'
  
  const route = useRoute()
  const festival = ref({})
  const mapPoints = ref([])
  
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
  
      // Adaugă marker-ele pe hartă
      mapPoints.value.forEach(point => {
        L.marker([point.latitude, point.longitude], {
          icon: L.divIcon({
            className: 'emoji-icon',
            html: `${point.emoji}<div class='label'>${point.label}</div>`
          })
        }).addTo(map)
      })
  
      // 🔍 Centrează harta în funcție de puncte
      if (mapPoints.value.length === 1) {
        map.setView([mapPoints.value[0].latitude, mapPoints.value[0].longitude], 16)
      } else if (mapPoints.value.length > 1) {
        const bounds = L.latLngBounds(
          mapPoints.value.map(p => [p.latitude, p.longitude])
        )
        map.fitBounds(bounds, { padding: [30, 30] })
      } else {
        // fallback: Cluj
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
      await axios.post(`http://localhost:8081/participari/adauga?userId=${userId}&festivalId=${festivalId}`, {
        userId,
        festivalId
      })
      alert('Participarea a fost înregistrată cu succes!')
    } catch (err) {
      console.error('Eroare la participare:', err)
      alert('A apărut o eroare. Încearcă din nou.')
    }
  }
  </script>
  
  <style scoped>
  .festival-detail-page {
    max-width: 800px;
    margin: auto;
    padding: 2rem;
  }
  .map {
    height: 400px;
    width: 100%;
    border: 2px solid #ccc;
    margin-top: 1rem;
  }
  .btn {
    background-color: #2563eb;
    color: white;
    padding: 0.5rem 1rem;
    border-radius: 6px;
    border: none;
  }
  .emoji-icon {
    font-size: 24px;
    text-align: center;
  }
  .label {
    font-size: 12px;
    color: black;
    background: white;
    border-radius: 4px;
    padding: 2px;
    margin-top: 2px;
  }
  </style>
