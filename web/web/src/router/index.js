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
  {
    path: "/product",
    component: () => import('../views/Layout.vue'),
    redirect: '/product/list',
    children: [
      {
        path: 'list',
        name: 'list',
        component: () => import('../views/product/list.vue')
      },
      {
        path: 'add',
        name: 'add',
        component: () => import('../views/product/add.vue')
      },
      {
        path: 'category',
        name: 'category',
        component: () => import('../views/product/category.vue')
      },
      {
        path: 'type',
        name: 'type',
        component: () => import('../views/product/type.vue')
      },
      {
        path: 'brand',
        name: 'brand',
        component: () => import('../views/product/brand.vue')
      },
    ]
  },
  {
    path: '/order',
    component: () => import('../views/Layout.vue'),
    redirect: '/order/list',
    children: [
      {
        path: 'list',
        name: 'orderList',
        component:() =>import('../views/order/list.vue')
      },
      {
        path: 'settings',
        name: 'orderSetting',
        component:() =>import('../views/order/setting.vue')
      },
      {
        path: 'return',
        name: 'orderReturn',
        component:() =>import('../views/order/return.vue')
      },
      {
        path: 'reason',
        name: 'orderReason',
        component:() =>import('../views/order/reason.vue')
      },
    ]
  },
  {
    path: '/marketing',
    component: () => import('../views/Layout.vue'),
    redirect: '/marketing/seckill',
    children: [
      {
        path: 'seckill',
        name: 'marketingSeckill',
        component:() =>import('../views/marketing/seckill.vue')
      },
      {
        path: 'coupon',
        name: 'marketingCoupon',
        component:() =>import('../views/marketing/coupon.vue')
      },
      {
        path: 'brand',
        name: 'marketingBrand',
        component:() =>import('../views/marketing/brand.vue')
      },
      {
        path: 'new',
        name: 'new',
        component:() =>import('../views/marketing/new.vue')
      },
      {
        path: 'popular',
        name: 'popular',
        component:() =>import('../views/marketing/popular.vue')
      },
      {
        path: 'topic',
        name: 'topic',
        component:() =>import('../views/marketing/topic.vue')
      },
      {
        path: 'ad',
        name: 'ad',
        component:() =>import('../views/marketing/ad.vue')
      },
    ]
  },
  {
    path: '/permission',
    name: 'permission',
    redirect: '/permission/user',
    component: () => import('../views/Layout.vue'),
    children: [
      {
        path: 'user',
        name: 'permissionUser',
        component: () => import('../views/permission/user.vue'),
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('../views/permission/role.vue'),
      },
      {
        path: 'menu',
        name: 'menu',
        component: () => import('../views/permission/menu.vue'),
      },
      {
        path: 'resource',
        name: 'resource',
        component: () => import('../views/permission/resource.vue'),
      },
    ]
  }
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