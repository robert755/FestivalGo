<template>
    <div class="admin-container">
      <aside class="sidebar">
        <h2 class="text-lg font-bold mb-4">Admin Menu</h2>
        <ul>
          <li><router-link to="/admin/add-festival">➕ Adaugă festival</router-link></li>
          <li><router-link to="/admin/festival/updatefestival">✏️ Actualizează un festival</router-link></li>
          <li><router-link to="admin/questionmanager">Formular</router-link></li>

          
        </ul>
      </aside>
  
      <main class="main-content">
        <h1 class="text-3xl font-bold mb-4">Hello Admin 👋</h1>
        <router-view /> 
      </main>
    </div>
  </template>
  
  <script setup>
  
  </script>
  
  <style scoped>
  .admin-container {
    display: flex;
    height: 100vh;
  }
  .sidebar {
    width: 220px;
    background-color: #f0f4f8;
    padding: 1rem;
    border-right: 1px solid #ccc;
  }
  .main-content {
    flex: 1;
    padding: 2rem;
  }
  .sidebar ul {
    list-style: none;
    padding: 0;
  }
  .sidebar li {
    margin-bottom: 1rem;
  }
  .sidebar a {
    text-decoration: none;
    color: #2563eb;
  }
  .sidebar a:hover {
    text-decoration: underline;
  }
  </style>
  