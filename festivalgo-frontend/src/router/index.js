import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../pages/LoginPage.vue'
import RegisterPage from '../pages/RegisterPage.vue'
import WelcomePage from '../pages/WelcomePage.vue'
import FestivalList from '../pages/FestivalList.vue'
import AdminPage from '../pages/AdminPage.vue'
import Add_Festival from '../pages/Add_Festival.vue'
import FestivalMap from '../pages/FestivalMap.vue' // ⬅️ nou
import FestivalDetails from '../pages/FestivalDetails.vue'
import UserParticipations from '../pages/UserParticipations.vue'
import UpdateFestival from '../pages/UpdateFestival.vue'
import QuestionAdmin from '../pages/QuestionAdmin.vue'
import QuizPage from '../pages/QuizPage.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  {path: '/welcome',component:WelcomePage,meta: { requiresAuth: true, role: 'USER' }},
  { path: '/festivals', component: FestivalList, meta: { requiresAuth: true, role: 'USER' } },
  { path: '/festival/:id', component: FestivalDetails, meta: { requiresAuth: true, role: 'USER' } },
  {path: '/my-participations',component: UserParticipations,meta: { requiresAuth: true, role: 'USER' }},
  { path: '/admin', component: AdminPage, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/add-festival', component: Add_Festival, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/festival-map/:id', component: FestivalMap, props: true, meta: { requiresAuth: true, role: 'ADMIN' } }, // ⬅️ adăugat
  {path: '/admin/festival/updatefestival',component:UpdateFestival,props:true,meta: {requiresAuth:true,role:'ADMIN'}},
  {path:'/admin/questionmanager',component:QuestionAdmin,props:true,meta:{requiresAuth:true,role:'ADMIN'}},
  {path:'/user/quiz-page',component:QuizPage,props:true,meta:{requiresAuth:true,role:"USER"}}
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
