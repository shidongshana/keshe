import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/home',
    component: () => import('../views/Layout.vue'),
    redirect: '/home/index',
    children: [
      {
        path: 'index',
        name: 'Dashboard',
        component: () => import('../views/home/index.vue')
      }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 修改路由守卫
router.beforeEach(async (to, from, next) => {
  const credentials = localStorage.getItem('credentials')

  if (to.path === '/login' && credentials) {
    try {
      const response = await axios.get('http://localhost:8081/api/user/info', {
        headers: {
          'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
        }
      })
      if (response.data.code === 200) {
        next('/home')
      } else {
        next('/login')
      }
    } catch (error) {
      next('/login')
    }
    return
  }

  if (to.path !== '/login' && !credentials) {
    next('/login')
    return
  }

  next()
})

export default router