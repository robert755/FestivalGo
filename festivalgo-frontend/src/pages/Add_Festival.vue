<template>
  <div class="add-festival">
    <h2 class="text-2xl font-bold mb-4">Adaugă Festival</h2>
    <form @submit.prevent="submitFestival">
      <input v-model="name" placeholder="Nume festival" class="input" required />
      <input v-model="location" placeholder="Locație" class="input" required />
      <input v-model="startDate" type="date" class="input" required />
      <input v-model="endDate" type="date" class="input" required />
      <input v-model="description" placeholder="Descriere" class="input" required />
      
      <input v-model="price" type="number" placeholder="Preț bilet (RON)" class="input" required /> <!-- ✅ -->

      <input type="file" @change="handleImageUpload" class="input" accept="image/*" required />

      <label for="genre">Gen muzical:</label>
      <select v-model="genre" class="input">
        <option value="ROCK">Rock</option>
        <option value="EDM">EDM</option>
        <option value="URBAN">Urban</option>
        <option value="POP">Pop</option>
        <option value="FOLK">Folk</option>
        <option value="JAZZ">Jazz</option>
        <option value="INDIE">Indie</option>
      </select>

      <button type="submit" class="btn">Creează festival</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const name = ref('')
const location = ref('')
const startDate = ref('')
const endDate = ref('')
const description = ref('')
const genre = ref('ROCK')
const price = ref('') // ✅
const imageFile = ref(null)

const handleImageUpload = (event) => {
  imageFile.value = event.target.files[0]
}

const submitFestival = async () => {
  try {
    const formData = new FormData()
    formData.append('name', name.value)
    formData.append('location', location.value)
    formData.append('startDate', startDate.value)
    formData.append('endDate', endDate.value)
    formData.append('description', description.value)
    formData.append('genre', genre.value)
    formData.append('price', price.value) // ✅
    formData.append('image', imageFile.value)

    const res = await axios.post('http://localhost:8081/festivals/post', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    const festivalId = res.data.id
    alert('Festival creat! Acum adaugă harta.')
    router.push(`/festival-map/${festivalId}`)
  } catch (err) {
    console.error(err)
    alert('Eroare la creare festival!')
  }
}
</script>

<style scoped>
/* ===== Container general ===== */
.add-festival {
  max-width: 600px;
  margin: 0 auto;
  padding: 32px 20px;
  background-color: #f9f9f9;
  font-family: 'Segoe UI', Roboto, sans-serif;
  color: #333;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

/* ===== Titlu ===== */
h2 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  text-align: center;
}

/* ===== Inputuri ===== */
.input {
  width: 100%;
  padding: 10px 12px;
  font-size: 14px;
  margin-bottom: 16px;
  border: none;
  border-bottom: 2px solid #ccc;
  background-color: transparent;
  transition: border-color 0.2s;
}

.input:focus {
  outline: none;
  border-color: #4285f4;
}

/* ===== Label pentru gen muzical ===== */
label {
  font-size: 14px;
  margin-bottom: 4px;
  display: block;
  color: #444;
}

/* ===== Buton ===== */
.btn {
  width: 100%;
  padding: 12px;
  background-color: #4285f4;
  color: white;
  font-size: 15px;
  font-weight: 500;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.25s;
}

.btn:hover {
  background-color: #3367d6;
}
</style>

