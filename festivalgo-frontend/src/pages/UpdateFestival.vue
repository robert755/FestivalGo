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
          <option>URBAN</option>
          <option>POP</option>
          <option>FOLK</option>
          <option>JAZZ</option>
          <option>INDIE</option>
        </select>

        <label>Preț bilet (RON):</label>
        <input type="number" v-model="festival.price" />

        <label>Imagine nouă (opțional):</label>
        <input type="file" @change="handleImageUpload" accept="image/*" />

        <div v-if="festival.imagePath" style="margin-top: 10px">
          <p><strong>Previzualizare:</strong></p>
          <img :src="`http://localhost:8081/uploads/${festival.imagePath}`" alt="Imagine festival" width="200" />
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
        price: 0,
        imagePath: ''
      },
      imageFile: null,
      map: null,
      selectedEmoji: '🎤',
      puncte: []
    }
  },
  mounted() {
    this.incarcaFestivaluri()
  },
  methods: {
    handleImageUpload(event) {
      this.imageFile = event.target.files[0]
    },
    incarcaFestivaluri() {
      axios.get('http://localhost:8081/festivals')
        .then(r => this.festivaluri = r.data)
        .catch(e => console.log('Eroare la încărcare:', e))
    },
    selecteazaFestival(fest) {
      this.festival = { ...fest }
      this.imageFile = null
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
      const formData = new FormData()
      formData.append('name', this.festival.name)
      formData.append('location', this.festival.location)
      formData.append('description', this.festival.description)
      formData.append('startDate', this.festival.startDate)
      formData.append('endDate', this.festival.endDate)
      formData.append('genre', this.festival.genre)
      formData.append('price', this.festival.price)
      if (this.imageFile) {
        formData.append('image', this.imageFile)
      }

      axios.put(`http://localhost:8081/festivals/${this.festival.id}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
        .then(() => alert('✅ Festival actualizat'))
        .catch(e => {
          console.log('Eroare la salvare:', e)
          alert('Eroare la actualizare festival!')
        })
    },
    stergeFestivalul() {
      if (!confirm(`Sigur vrei să ștergi "${this.festival.name}"?`)) return
      axios.delete(`http://localhost:8081/festivals/${this.festival.id}`)
        .then(() => {
          alert('Festival șters')
          this.festival = {
            id: null, name: '', location: '', description: '',
            startDate: '', endDate: '', genre: '', price: 0, imagePath: ''
          }
          this.incarcaFestivaluri()
        })
        .catch(e => console.log('Eroare la ștergere:', e))
    },
    salveazaPunctele() {
      axios.put(`http://localhost:8081/map-points/update/${this.festival.id}`, this.puncte)
        .then(() => alert('✅ Punctele au fost salvate!'))
        .catch(e => {
          console.log('Eroare la salvare puncte!', e)
          alert('Eroare la salvarea punctelor!')
        })
    }
  }
}
</script>
<style scoped>
/* ===== Layout General ===== */
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px 16px;
  font-family: 'Segoe UI', Roboto, sans-serif;
  background-color: #f5f5f5;
  color: #333;
}

/* ===== Titluri ===== */
h2 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  text-align: center;
}

h3 {
  font-size: 18px;
  font-weight: 500;
  margin: 20px 0 10px;
}

/* ===== Lista Festivaluri ===== */
.festival-lista {
  margin-bottom: 24px;
}

.buton-festival {
  margin: 6px 6px 6px 0;
  padding: 8px 14px;
  border: 1px solid #ccc;
  border-radius: 6px;
  background-color: #fff;
  cursor: pointer;
  transition: background-color 0.2s;
}

.buton-festival:hover {
  background-color: #e0f0ff;
}

/* ===== Formularul ===== */
.formular {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

input,
textarea,
select {
  width: 100%;
  padding: 10px 12px;
  font-size: 14px;
  margin-bottom: 14px;
  border: none;
  border-bottom: 2px solid #ccc;
  background-color: transparent;
  transition: border-color 0.2s;
  outline: none;
}

input:focus,
textarea:focus,
select:focus {
  border-color: #4285f4;
}

/* ===== Butoane de acțiune ===== */
.buton-salveaza,
.buton-sterge {
  padding: 10px 16px;
  font-size: 14px;
  margin-top: 8px;
  margin-right: 10px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.buton-salveaza {
  background-color: #34a853;
  color: white;
}

.buton-salveaza:hover {
  background-color: #1e8e3e;
}

.buton-sterge {
  background-color: #ea4335;
  color: white;
}

.buton-sterge:hover {
  background-color: #c5221f;
}

/* ===== Imagine previzualizare ===== */
img {
  border-radius: 6px;
  margin-top: 10px;
}

/* ===== Hartă Leaflet ===== */
.map {
  width: 100%;
  height: 400px;
  margin-top: 16px;
  border-radius: 8px;
  border: 2px solid #ccc;
}

/* ===== Selector emoji și puncte ===== */
.emoji-selector {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.emoji-icon {
  font-size: 22px;
  text-align: center;
}

.emoji-label {
  font-size: 11px;
  background-color: #ffffffcc;
  padding: 2px 6px;
  border-radius: 4px;
  display: inline-block;
  margin-top: 2px;
}
</style>
