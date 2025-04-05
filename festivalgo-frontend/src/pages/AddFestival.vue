<template>
    <div class="add-festival">
      <h2 class="text-2xl font-bold mb-4">Adaugă Festival</h2>
  
      <form @submit.prevent="submitFestival">
        <input v-model="name" placeholder="Nume festival" class="input" required />
        <input v-model="location" placeholder="Locație" class="input" required />
        <input v-model="date" type="date" class="input" required />
        
        <button type="submit" class="btn">Creează festival</button>
      </form>
  
      <div v-if="festivalId" class="mt-8">
        <h3 class="text-xl font-semibold mb-2">Adaugă emoji-uri pe hartă 🗺️</h3>
        <div class="map-container" @click="placeEmoji($event)">
          <img :src="mapPlaceholder" alt="Harta festivalului" class="map-image" />


          <span
            v-for="point in mapPoints"
            :key="point.id"
            class="emoji"
            :style="{ left: point.x + 'px', top: point.y + 'px' }"
          >
            {{ point.emoji }}
          </span>
        </div>
  
        <div class="emoji-selector mt-4">
          <label>Alege un emoji:</label>
          <select v-model="selectedEmoji" class="input">
            <option>🎤</option>
            <option>🍔</option>
            <option>🏕️</option>
            <option>🚻</option>
          </select>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import axios from 'axios'
  import mapPlaceholder from '../assets/map-placeholder.png'


  const name = ref('')
  const location = ref('')
  const date = ref('')
  const selectedEmoji = ref('🎤')
  const mapPoints = ref([])
  const festivalId = ref(null)
  
  const submitFestival = async () => {
    try {
      const res = await axios.post('http://localhost:8081/festivals/post', {
        name: name.value,
        location: location.value,
        date: date.value
      })
      festivalId.value = res.data.id
      alert('Festival creat! Acum adaugă emoji-uri pe hartă.')
    } catch (err) {
      console.error(err)
      alert('Eroare la creare festival!')
    }
  }
  
  const placeEmoji = async (event) => {
    if (!festivalId.value) return
  
    const rect = event.target.getBoundingClientRect()
    const x = event.clientX - rect.left
    const y = event.clientY - rect.top
    const emoji = selectedEmoji.value
  
    try {
      await axios.post(`http://localhost:8081/map-points/${festivalId.value}`, {
        festivalId: festivalId.value,
        x,
        y,
        emoji
      })
      mapPoints.value.push({ x, y, emoji, id: Date.now() })
    } catch (err) {
      console.error(err)
      alert('Eroare la adăugarea punctului pe hartă.')
    }
  }
  </script>
  
  <style scoped>
  .add-festival {
    max-width: 600px;
    margin: auto;
    padding: 2rem;
  }
  .input {
    display: block;
    width: 100%;
    padding: 0.5rem;
    margin-bottom: 1rem;
    border: 1px solid #ccc;
    border-radius: 6px;
  }
  .btn {
    padding: 0.5rem 1rem;
    background-color: #2563eb;
    color: white;
    border: none;
    border-radius: 6px;
  }
  .map-container {
    position: relative;
    width: 100%;
    max-width: 600px;
    border: 1px solid #ccc;
  }
  .map-image {
    width: 100%;
    height: auto;
  }
  .emoji {
    position: absolute;
    font-size: 24px;
    transform: translate(-50%, -50%);
  }
  </style>
  