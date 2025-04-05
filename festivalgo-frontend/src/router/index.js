import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../pages/LoginPage.vue'
import RegisterPage from '../pages/RegisterPage.vue'
import FestivalList from '../pages/FestivalList.vue'
import AdminPage from '../pages/AdminPage.vue'
import Add_Festival from '../pages/Add_Festival.vue'
import FestivalMap from '../pages/FestivalMap.vue' // ⬅️ nou

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  { path: '/festivals', component: FestivalList, meta: { requiresAuth: true, role: 'USER' } },
  { path: '/admin', component: AdminPage, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/add-festival', component: Add_Festival, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/festival-map/:id', component: FestivalMap, props: true, meta: { requiresAuth: true, role: 'ADMIN' } } // ⬅️ adăugat
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 🔒 Protecție pe bază de rol
router.beforeEach((to, from, next) => {
  const requiresAuth = to.meta.requiresAuth
  const requiredRole = to.meta.role
  const userRole = localStorage.getItem('role')

  if (requiresAuth && userRole !== requiredRole) {
    return next('/login')
  }

  next()
})

export default router
