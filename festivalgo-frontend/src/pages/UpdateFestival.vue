<template>
  <div class="container">
    <h2>Actualizează un festival</h2>

    <!-- Lista de festivaluri -->
    <div class="festival-lista">
      <p><strong>Alege un festival:</strong></p>
      <div>
        <button
          v-for="fest in festivaluri"
          :key="fest.id"
          @click="selecteazaFestival(fest)"
          class="buton-festival"
        >
          {{ fest.name }}
        </button>
      </div>
    </div>

    <!-- Formularul și harta -->
    <div v-if="festival.id" class="formular">
      <h3>Modifică: {{ festival.name }}</h3>
      <form @submit.prevent="salveazaFestivalul">
        <label>Nume:</label>
        <input v-model="festival.name" />

        <label>Locație:</label>
        <input v-model="festival.location" />

        <label>Descriere:</label>
        <textarea v-model="festival.description"></textarea>

        <label>Data început:</label>
        <input type="date" v-model="festival.startDate" />

        <label>Data sfârșit:</label>
        <input type="date" v-model="festival.endDate" />

        <label>Gen muzical:</label>
        <select v-model="festival.genre">
          <option disabled value="">Alege genul</option>
          <option>ROCK</option>
          <option>EDM</option>
          <option>Hip-Hop</option>
          <option>POP</option>
          <option>Jazz</option>
        </select>

        <label>Link imagine:</label>
        <input v-model="festival.imagePath" />

        <div v-if="festival.imagePath" style="margin-top: 10px">
          <p><strong>Previzualizare:</strong></p>
          <img :src="festival.imagePath" alt="Imagine festival" width="200" />
        </div>

        <br />
        <button type="submit" class="buton-salveaza">💾 Salvează</button>
        <button type="button" class="buton-sterge" @click="stergeFestivalul">🗑️ Șterge</button>
      </form>

      <!-- Harta -->
      <h3>🗺️ Modifică harta festivalului</h3>
      <div id="map" class="map"></div>

      <!-- Selector emoji -->
      <div class="emoji-selector">
        <label>Alege emoji:</label>
        <select v-model="selectedEmoji">
          <option>🎤</option>
          <option>🍔</option>
          <option>🏕️</option>
          <option>🚻</option>
        </select>
        <button @click="salveazaPunctele" class="buton-salveaza">Salvează punctele</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import L from 'leaflet'
import { nextTick } from 'vue'

export default {
  name: 'UpdateFestival',
  data() {
    return {
      festivaluri: [],
      festival: {
        id: null,
        name: '',
        location: '',
        description: '',
        startDate: '',
        endDate: '',
        genre: '',
        imagePath: ''
      },
      map: null,
      selectedEmoji: '🎤',
      puncte: []
    }
  },
  mounted() {
    this.incarcaFestivaluri()
  },
  methods: {
    incarcaFestivaluri() {
      axios.get('http://localhost:8081/festivals')
        .then(r => this.festivaluri = r.data)
        .catch(e => console.log('Eroare la încărcare:', e))
    },
    selecteazaFestival(fest) {
      this.festival = { ...fest }
      nextTick(() => {
        this.incarcaHarta()
      })
    },
    incarcaHarta() {
      if (this.map) {
        this.map.remove()
      }
      this.map = L.map('map').setView([46.770439, 23.591423], 13)
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap'
      }).addTo(this.map)

      // adăugare puncte pe click
      this.map.on('click', e => {
        const { lat, lng } = e.latlng
        const emoji = this.selectedEmoji
        const label = this.getLabelForEmoji(emoji)

        L.marker([lat, lng], {
          icon: L.divIcon({
            className: 'emoji-icon',
            html: `<div style="text-align:center">${emoji}<br/><span class="emoji-label">${label}</span></div>`,
            iconSize: [30, 40]
          })
        }).addTo(this.map)

        this.puncte.push({ latitude: lat, longitude: lng, emoji, label })
      })
    },
    getLabelForEmoji(emoji) {
      switch (emoji) {
        case '🎤': return 'Scenă'
        case '🍔': return 'Foodcourt'
        case '🏕️': return 'Camping'
        case '🚻': return 'Toalete'
        default: return 'Punct'
      }
    },
    salveazaFestivalul() {
      axios.put(`http://localhost:8081/festivals/${this.festival.id}`, this.festival)
        .then(() => alert('✅ Festival actualizat'))
        .catch(e => console.log('Eroare la salvare:', e))
    },
    stergeFestivalul() {
      if (!confirm(`Sigur vrei să ștergi "${this.festival.name}"?`)) return
      axios.delete(`http://localhost:8081/festivals/${this.festival.id}`)
        .then(() => {
          alert('Festival șters')
          this.festival = {
            id: null, name: '', location: '', description: '',
            startDate: '', endDate: '', genre: '', imagePath: ''
          }
          this.incarcaFestivaluri()
        })
        .catch(e => console.log('Eroare la ștergere:', e))
    },
    salveazaPunctele() {
      axios.put(`http://localhost:8081/map-points/update/${this.festival.id}`, this.puncte)
        .then(() => alert('✅ Punctele au fost salvate!'))
        .catch(e => {
          console.log('Eroare la salvare puncte:', e)
          alert('Eroare la salvarea punctelor!')
        })
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: auto;
  padding: 2rem;
}
.festival-lista {
  margin-bottom: 1rem;
}
.buton-festival {
  margin: 5px;
  padding: 6px 12px;
  border: 1px solid #aaa;
  border-radius: 5px;
  background-color: white;
  cursor: pointer;
}
.buton-festival:hover {
  background-color: #e0f0ff;
}
input, textarea, select {
  display: block;
  width: 100%;
  margin-bottom: 10px;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}
.buton-salveaza, .buton-sterge {
  padding: 10px 15px;
  margin-top: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.buton-salveaza {
  background-color: #4caf50;
  color: white;
}
.buton-sterge {
  margin-left: 10px;
  background-color: #e53935;
  color: white;
}
.map {
  width: 100%;
  height: 400px;
  border: 2px solid #ccc;
  margin-top: 1rem;
}
.emoji-selector {
  margin-top: 1rem;
}
.emoji-icon {
  font-size: 22px;
  text-align: center;
}
.emoji-label {
  font-size: 11px;
  background-color: #ffffffcc;
  padding: 2px 4px;
  border-radius: 4px;
  display: inline-block;
  margin-top: 2px;
}
</style>
