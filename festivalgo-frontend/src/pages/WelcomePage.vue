<template>
  <div class="container">
    <!-- Meniul lateral -->
    <aside class="sidebar">
      <h2 class="logo">🎧 FestivalGo</h2>
      <nav class="menu">
        <button class="menu-button" @click="goTo('/festivals')">🎪 Festivaluri</button>
        <button class="menu-button" @click="goTo('/my-participations')">✅ Participările mele</button>
        <button class="menu-button logout" @click="logout">🚪 Logout</button>
      </nav>
    </aside>

    <!-- Conținutul principal -->
    <main class="main">
      <h1 class="welcome">Salut, {{ username }}!</h1>
      <p>Bine ai venit în aplicația FestivalGo.</p>
      <p>Folosește meniul din stânga pentru a explora aplicația.</p>
    </main>

    <!-- Mesaj animat în colțul dreapta jos -->
    <transition name="fade">
      <div
        v-if="!preferredGenre"
        class="floating-quiz-box"
        @click="goTo('/user/quiz-page')"
      >
        🎧 Nu știi ce ți se potrivește?<br />
        Fă testul!
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const preferredGenre = ref(null)
const userId = localStorage.getItem('userId')

onMounted(() => {
  username.value = localStorage.getItem('username') || 'Utilizator'

  fetch(`/users/${userId}`)
    .then((res) => res.json())
    .then((data) => {
      preferredGenre.value = data.preferredGenre
    })
    .catch((err) => console.error('Eroare la preluarea utilizatorului:', err))
})

const goTo = (path) => {
  router.push(path)
}

const logout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.container {
  display: flex;
  min-height: 100vh;
  background-color: #f0f0f0;
}

/* Sidebar */
.sidebar {
  width: 220px;
  background-color: #ffffff;
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.logo {
  font-size: 22px;
  font-weight: bold;
  color: #2563eb;
  margin-bottom: 30px;
}

.menu {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.menu-button {
  background-color: #eeeeee;
  border: none;
  padding: 10px;
  text-align: left;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
}

.menu-button:hover {
  background-color: #dddddd;
}

.logout {
  margin-top: auto;
  color: red;
}

/* Main content */
.main {
  flex: 1;
  padding: 40px;
  background-color: #f9f9f9;
}

.welcome {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 10px;
}

/* Floating quiz box */
.floating-quiz-box {
  position: fixed;
  bottom: 30px;
  right: 30px;
  background-color: #ede9fe;
  color: #6b21a8;
  border: 2px solid #a855f7;
  border-radius: 10px;
  padding: 12px 16px;
  font-size: 0.95rem;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  z-index: 999;
  animation: slideIn 0.6s ease-out;
  transition: opacity 0.3s ease;
}

.floating-quiz-box:hover {
  background-color: #f3e8ff;
}

/* fade-in / fade-out */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.6s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Slide in animation */
@keyframes slideIn {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
