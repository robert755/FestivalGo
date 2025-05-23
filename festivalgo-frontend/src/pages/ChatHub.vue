<template>
  <div class="chat-hub-container">
    <div class="sidebar">
      <div class="header">
        <h3>💬 VibeTalk</h3>
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
    </div>

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

const groups = ref([])
const selectedGroup = ref(null)
const showCreateGroup = ref(false)
const userId = localStorage.getItem("userId")

const fetchGroups = async () => {
  try {
    const response = await axios.get(`http://localhost:8081/chat/groups/user/${userId}`)
    groups.value = response.data
  } catch (err) {
    console.error("Eroare la grupuri:", err)
  }
}

const selectGroup = (group) => {
  selectedGroup.value = group
}

const reloadGroups = () => {
  fetchGroups()
  showCreateGroup.value = false
}

onMounted(() => {
  fetchGroups()
})
</script>

<style scoped>
.chat-hub-container {
  display: flex;
  height: 100vh; 
  width: 100vw;  
  background-color: #1c1c1e;
  font-family: 'Segoe UI', sans-serif;
  color: white;
  overflow: hidden;
}


.sidebar {
  width: 280px;
  background-color: #2a2a2d;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  border-right: 1px solid #333;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
}

.header h3 {
  font-size: 20px;
  text-shadow: 0 0 6px #b300ff;
}

.header button {
  background-color: #b300ff;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.header button:hover {
  background-color: #9900cc;
}

.group-list {
  list-style: none;
  padding: 0;
  margin-top: 10px;
}

.group-list li {
  background-color: #3a3a3d;
  padding: 12px;
  margin-bottom: 8px;
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
  background-color: #1f1f22;
  padding: 20px;
  overflow-y: auto;
  border-left: 1px solid #333;
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
