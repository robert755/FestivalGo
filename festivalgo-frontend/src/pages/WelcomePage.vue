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
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  const username = ref('')
  
  onMounted(() => {
    username.value = localStorage.getItem('username') || 'Utilizator'
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
    box-shadow: 2px 0 5px rgba(0,0,0,0.1);
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
  </style>
  