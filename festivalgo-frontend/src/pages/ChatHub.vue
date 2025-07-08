<template>
  <div class="chat-hub-container">
    <!-- Overlay -->
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Sidebar principal -->
    <aside v-if="isSidebarVisible" class="main-sidebar">
      <h2>FestivalGo</h2>
      <button @click="toggleSidebar">Închide</button>
      <nav>
        <button @click="goTo('/welcome')">Acasă</button> 
        <button @click="goTo('/festivals')">Festivaluri</button>
        <button @click="goTo('/my-participations')">Participările mele</button>
        <button class="logout" @click="logout">Logout</button>
        <div v-if="!preferredGenre" class="quiz-link" @click="goTo('/user/quiz-page')">
          Nu știi ce ți se potrivește? Fă testul!
        </div>
      </nav>

      <!-- Zona grupuri chat -->
      <div class="header">
        <h3>💬 Grupurile mele</h3>
        <button @click="showCreateGroup = !showCreateGroup">
          {{ showCreateGroup ? 'Închide' : 'Creează grup' }}
        </button>
      </div>

      <CreateGroup v-if="showCreateGroup" @grupCreat="reloadGroups" />
      <AddMember v-if="selectedGroup" :groupId="selectedGroup.id" />

      <ul class="group-list">
        <li
          v-for="group in groups"
          :key="group.id"
          :class="{ active: selectedGroup && group.id === selectedGroup.id }"
          @click="selectGroup(group)"
        >
          {{ group.name }}
        </li>
      </ul>
    </aside>

    <!-- Buton meniu ☰ -->
    <button class="menu-toggle" @click="toggleSidebar">☰</button>

    <!-- Panel de chat -->
    <div class="chat-panel" v-if="selectedGroup">
      <GroupChat :groupId="selectedGroup.id" :groupName="selectedGroup.name" />
    </div>
    <div class="chat-panel" v-else>
      <p>Selectează un grup pentru a începe conversația.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import GroupChat from './GroupChat.vue'
import CreateGroup from './CreateGroup.vue'
import AddMember from './AddMember.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const groups = ref([])
const selectedGroup = ref(null)
const showCreateGroup = ref(false)
const isSidebarVisible = ref(false)

const userId = localStorage.getItem('userId')
const preferredGenre = ref(null)

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value
}

const goTo = (path) => {
  router.push(path)
}

const logout = () => {
  localStorage.clear()
  router.push('/login')
}

const fetchGroups = async () => {
  try {
    const response = await axios.get(`http://localhost:8081/chat/groups/user/${userId}`)
    groups.value = response.data
  } catch (err) {
    console.error('Eroare la grupuri:', err)
  }
}

const selectGroup = (group) => {
  selectedGroup.value = group
}

const reloadGroups = () => {
  fetchGroups()
  showCreateGroup.value = false
}

onMounted(async () => {
  fetchGroups()
  try {
    const res = await axios.get(`http://localhost:8081/users/${userId}`)
    preferredGenre.value = res.data.preferredGenre
  } catch (err) {
    console.error('Eroare la user:', err)
  }
})
</script>

<style scoped>
.chat-hub-container {
  display: flex;
  height: 100vh;
  background-color: #1c1c1e;
  font-family: 'Segoe UI', sans-serif;
  color: white;
  position: relative;
}

.menu-toggle {
  position: fixed;
  top: 20px;
  left: 20px;
  z-index: 1100;
  background: none;
  border: none;
  font-size: 28px;
  color: white;
  cursor: pointer;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 999;
}

.main-sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 280px;
  height: 100vh;
  background-color: #2a2a2d;
  padding: 16px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow-y: auto;
}

.main-sidebar h2 {
  font-size: 24px;
  color: #bb86fc;
  text-align: center;
}

.main-sidebar nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.main-sidebar button {
  background-color: #2d2d30;
  color: white;
  border: none;
  padding: 10px;
  font-size: 15px;
  border-radius: 8px;
  cursor: pointer;
}

.main-sidebar button:hover {
  background-color: #3f3f46;
}

.logout {
  color: #f87171;
}

.quiz-link {
  background-color: #9f7aea;
  color: white;
  padding: 10px;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
}

/* Grupuri */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.header h3 {
  font-size: 18px;
  text-shadow: 0 0 6px #b300ff;
}

.header button {
  background-color: #b300ff;
  color: white;
  padding: 6px 10px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}

.header button:hover {
  background-color: #9900cc;
}

.group-list {
  list-style: none;
  padding: 0;
  margin: 10px 0;
}

.group-list li {
  background-color: #3a3a3d;
  padding: 10px;
  margin-bottom: 6px;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s ease;
}

.group-list li:hover {
  background-color: #5a00b0;
}

.group-list li.active {
  background-color: #b300ff;
  font-weight: bold;
}

.chat-panel {
  flex: 1;
  margin-left: 280px;
  background-color: #1f1f22;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.chat-panel p {
  color: #999;
  font-size: 18px;
}
</style>
