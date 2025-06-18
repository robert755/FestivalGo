<template>
  <div class="admin-container">
    <!-- Sidebar cu meniul admin și logout -->
    <aside class="sidebar">
      <h2 class="text-lg font-bold mb-4">Admin Menu</h2>
      <ul>
        <li><router-link to="/admin/add-festival">➕ Adaugă festival</router-link></li>
        <li><router-link to="/admin/festival/updatefestival">✏️ Actualizează un festival</router-link></li>
        <li><router-link to="/admin/questionmanager">📝 Formular</router-link></li>
      </ul>

      <!-- Buton Logout -->
      <button @click="logout" class="logout-btn">🚪 Logout</button>
    </aside>

    <!-- Conținut principal -->
    <main class="main-content">
      <h1 class="text-3xl font-bold mb-4">Hello Admin 👋</h1>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const logout = () => {
  // Șterge datele din localStorage
  localStorage.removeItem('userId')
  localStorage.removeItem('username')
  localStorage.removeItem('role')

  // Redirecționează către login
  router.push('/login')
}
</script>

<style scoped>
/* ===== Structură generală ===== */
.admin-container {
  display: flex;
  height: 100vh;
  font-family: 'Segoe UI', Roboto, sans-serif;
}

/* ===== Sidebar ===== */
.sidebar {
  width: 220px;
  background-color: #f0f4f8;
  padding: 1rem;
  border-right: 1px solid #ccc;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.sidebar h2 {
  font-size: 16px;
  margin-bottom: 1rem;
}

.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar li {
  margin-bottom: 1rem;
}

.sidebar a {
  text-decoration: none;
  color: #2563eb;
  font-weight: 500;
}

.sidebar a:hover {
  text-decoration: underline;
}

/* ===== Buton Logout ===== */
.logout-btn {
  margin-top: 2rem;
  padding: 10px 14px;
  background-color: #e53935;
  color: white;
  font-weight: 500;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  width: 100%;
  transition: background-color 0.2s;
}

.logout-btn:hover {
  background-color: #c62828;
}

/* ===== Conținut principal ===== */
.main-content {
  flex: 1;
  padding: 2rem;
  background-color: #ffffff;
}
</style>
