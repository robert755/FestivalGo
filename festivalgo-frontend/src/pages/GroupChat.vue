<template>
  <div class="group-chat-container">
    <h2 class="title">{{ groupName }}</h2>

    <div class="messages">
      <div v-for="msg in messages" :key="msg.id" class="message">
        <strong>{{ msg.sender.username }}:</strong> {{ msg.text }}
      </div>
    </div>

    <div class="send-box">
      <input v-model="newMessage" placeholder="Scrie un mesaj..." />
      <button @click="sendMessage">Trimite</button>
    </div>

    <div class="emoji-links">
      <span @click="goToBooking" title="Cazare pe Booking">🏠</span>
      <span @click="goToCFR" title="Trenuri CFR">🚆</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const props = defineProps(['groupId', 'groupName'])
const userId = localStorage.getItem("userId")
const messages = ref([])
const newMessage = ref("")

const fetchMessages = async () => {
  try {
    const res = await axios.get(`http://localhost:8081/chat/groups/${props.groupId}/messages`)
    messages.value = res.data
  } catch (err) {
    console.error("Eroare mesaje:", err)
  }
}

const sendMessage = async () => {
  if (!newMessage.value.trim()) return
  try {
    await axios.post(`http://localhost:8081/chat/groups/${props.groupId}/message`, null, {
      params: {
        senderId: userId,
        text: newMessage.value
      }
    })
    newMessage.value = ""
    await fetchMessages()
  } catch (err) {
    console.error("Eroare trimitere mesaj:", err)
  }
}

const goToBooking = () => {
  window.open('https://www.booking.com', '_blank')
}

const goToCFR = () => {
  window.open('https://www.cfrcalatori.ro', '_blank')
}

onMounted(() => {
  fetchMessages()
  setInterval(fetchMessages, 5000)
})
</script>

<style scoped>
.group-chat-container {
  display: flex;
  flex-direction: column;
  width: 75%;
  height: 90vh;
  margin: 0 auto;
  background-color: #1c1c1e;
  padding: 20px 40px;
  color: white;
  border-radius: 12px;
  box-shadow: 0 0 20px rgba(255, 0, 255, 0.08);
  font-family: 'Segoe UI', sans-serif;
}


.title {
  text-align: center;
  font-size: 28px;
  color: #e5e5e5;
  margin-bottom: 15px;
  text-shadow: 0 0 8px #b300ff;
}

.messages {
  flex: 1;
  overflow-y: auto;
  background-color: #2a2a2d;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 15px;
}

.message {
  background-color: #3c3c42;
  padding: 10px;
  border-radius: 8px;
  margin-bottom: 8px;
  color: #eaeaea;
}

.send-box {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.send-box input {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 8px;
  background-color: #444;
  color: white;
  font-size: 16px;
  outline: none;
}

.send-box input::placeholder {
  color: #999;
}

.send-box button {
  padding: 12px 20px;
  background-color: #b300ff;
  border: none;
  border-radius: 8px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}

.send-box button:hover {
  background-color: #9900cc;
}

.emoji-links {
  display: flex;
  gap: 25px;
  justify-content: center;
  font-size: 28px;
  cursor: pointer;
  margin-top: 5px;
}

.emoji-links span:hover {
  transform: scale(1.3);
  transition: 0.2s ease;
  text-shadow: 0 0 10px #ff00cc;
}
</style>
