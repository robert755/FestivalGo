<template>
    <div class="festival-map">
      <h2 class="text-2xl font-bold mb-4">Adaugă locații pe hartă 🗺️</h2>
      <div id="map" class="map"></div>
  
      <div class="emoji-selector mt-4">
        <label>Alege un emoji:</label>
        <select v-model="selectedEmoji" class="input">
          <option>🎤</option>
          <option>🍔</option>
          <option>🏕️</option>
          <option>🚻</option>
        </select>
        <button @click="submitMapPoints" class="btn">Salvează locațiile</button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue'
  import { useRoute } from 'vue-router'
  import L from 'leaflet'
  import axios from 'axios'
  
  const route = useRoute()
  const festivalId = route.params.id
  const selectedEmoji = ref('🎤')
  const mapPoints = ref([])
  let map
  
  const getLabelForEmoji = (emoji) => {
    switch (emoji) {
      case '🎤': return 'Scenă'
      case '🍔': return 'Foodcourt'
      case '🏕️': return 'Camping'
      case '🚻': return 'Toalete'
      default: return 'Punct'
    }
  }
  
  onMounted(() => {
    map = L.map('map').setView([46.770439, 23.591423], 13) // Cluj-Napoca
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a>'
    }).addTo(map)
  
    map.on('click', async (e) => {
      const { lat, lng } = e.latlng
      const emoji = selectedEmoji.value
      const label = getLabelForEmoji(emoji)
  
      L.marker([lat, lng], {
        icon: L.divIcon({
          className: 'emoji-icon',
          html: `<div style="text-align: center;">${emoji}<br/><span class="emoji-label">${label}</span></div>`,
          iconSize: [30, 40]
        })
      }).addTo(map)
  
      mapPoints.value.push({ lat, lng, emoji, label })
    })
  })
  
  const submitMapPoints = async () => {
    try {
      for (const point of mapPoints.value) {
        await axios.post(`http://localhost:8081/map-points/${festivalId}`, {
          festivalId: Number(festivalId),
          latitude: point.lat,
          longitude: point.lng,
          emoji: point.emoji,
          label: point.label
        })
      }
      alert('Marcajele au fost salvate cu succes!')
    } catch (err) {
      console.error(err)
      alert('Eroare la salvarea marcajelor')
    }
  }
  </script>
  
  <style scoped>
  .festival-map {
    max-width: 800px;
    margin: auto;
    padding: 2rem;
  }
  .map {
    height: 500px;
    width: 100%;
    border: 2px solid #ccc;
    margin-bottom: 1rem;
  }
  .input {
    display: inline-block;
    padding: 0.5rem;
    margin-right: 1rem;
    border: 1px solid #ccc;
    border-radius: 6px;
  }
  .btn {
    padding: 0.5rem 1rem;
    background-color: #2563eb;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
  }
  .emoji-icon {
    font-size: 24px;
    text-align: center;
    line-height: 24px;
  }
  .emoji-label {
    font-size: 12px;
    color: #333;
    background-color: #ffffffb8;
    padding: 2px 4px;
    border-radius: 4px;
    display: inline-block;
    margin-top: 2px;
  }
  </style>
  