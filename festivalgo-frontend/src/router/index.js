import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../pages/LoginPage.vue'
import FestivalList from '../pages/FestivalList.vue'
import RegisterPage from '../pages/RegisterPage.vue' // 🆕 import

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage }, // 🆕 adăugat
  { path: '/festivals', component: FestivalList }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
