<template>
  <div class="add-festival">
    <h2 class="text-2xl font-bold mb-4">Adaugă Festival</h2>
    <form @submit.prevent="submitFestival">
      <input v-model="name" placeholder="Nume festival" class="input" required />
      <input v-model="location" placeholder="Locație" class="input" required />
      <input v-model="startDate" type="date" class="input" required />
      <input v-model="endDate" type="date" class="input" required />
      <input v-model="description" placeholder="Descriere" class="input" required />

      <!-- Input pentru fișier (imagine) -->
      <input type="file" @change="handleImageUpload" class="input" accept="image/*" required />

      <label for="genre">Gen muzical:</label>
      <select v-model="genre" class="input">
        <option value="ROCK">Rock</option>
        <option value="EDM">EDM</option>
        <option value="URBAN">Urban</option>
        <option value="POP">Pop</option>
        <option value="FOLK">Folk</option>
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
const imageFile = ref(null)

// Preluarea fișierului selectat din input
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
  cursor: pointer;
}
</style>
