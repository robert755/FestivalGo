<template>
  <div class="participari-container">
    <h1 class="titlu">Participările mele</h1>

    <div v-if="participari.length === 0" class="info">
      Nu ești înscris la niciun festival momentan.
    </div>

    <div v-else class="festivaluri-boxuri">
      <div
        class="box"
        v-for="p in participari"
        :key="p.id"
        :style="{ backgroundImage: p.festival ? `url(${p.festival.imagePath})` : 'none' }"
      >
        <div class="info-box">
          <div class="text-stanga">
            <h2 class="nume">{{ p.festival?.name || 'Festival șters' }}</h2>
            <p class="perioada">
              {{ p.festival?.startDate || '' }} - {{ p.festival?.endDate || '' }}
            </p>
          </div>
          <button class="btn-anuleaza" @click="anuleazaParticipare(p.id)">
            Te-ai răzgândit?
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const participari = ref([])

onMounted(async () => {
  const userId = localStorage.getItem('userId')
  if (!userId) return

  try {
    const res = await axios.get(`http://localhost:8081/participari/user/${userId}`)
    participari.value = res.data
  } catch (err) {
    console.error('Eroare la încărcarea participărilor:', err)
  }
})

const anuleazaParticipare = async (id) => {
  if (!confirm("Ești sigur că vrei să anulezi această participare?")) return

  try {
    await axios.put(`http://localhost:8081/participari/anuleaza/${id}`)
    participari.value = participari.value.filter(p => p.id !== id)
  } catch (err) {
    console.error('Eroare la anularea participării:', err)
  }
}
</script>

<style scoped>
.participari-container {
  padding: 2rem;
  max-width: 1000px;
  margin: auto;
}
.titlu {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
}
.info {
  font-size: 16px;
  color: gray;
  margin-top: 2rem;
}
.festivaluri-boxuri {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* CARD */
.box {
  position: relative;
  height: 180px;
  border-radius: 12px;
  background-size: cover;
  background-position: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

/* Overlay info */
.info-box {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: linear-gradient(to top, rgba(0,0,0,0.7), transparent);
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding: 1rem;
  box-sizing: border-box;
  height: 100%;
  color: white;
}

/* Text colț stânga jos */
.text-stanga {
  max-width: 70%;
}
.nume {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
}
.perioada {
  font-size: 14px;
}

/* Buton colț dreapta jos */
.btn-anuleaza {
  background-color: rgba(239, 68, 68, 0.9);
  border: none;
  color: white;
  padding: 8px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}
.btn-anuleaza:hover {
  background-color: rgba(220, 38, 38, 0.95);
}
</style>
