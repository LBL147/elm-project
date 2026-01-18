import { createRouter, createWebHistory } from 'vue-router'
import Index from '../components/Index.vue'
import MerchantManager from '../components/MerchantManager.vue'
import BusinessInfo from '../components/BusinessInfo.vue'
import FoodManagement from '../components/FoodManagement.vue'
import Order from '../components/Order.vue'
import Payment from '../components/Payment.vue'
import UserAddress from '../components/UserAddress.vue'
import AddUserAddress from '../components/AddUserAddress.vue'
import EditUserAddress from '../components/EditUserAddress.vue'
import OrderList from '../components/OrderList.vue'
import Login from '../components/Login.vue'
import Register from '../components/Register.vue'
import AdminDashboard from '../components/AdminDashboard.vue'
import BusinessDashboard from '../components/BusinessDashboard.vue'
import { Auth } from '../utils/auth'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false }
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/business',
    name: 'BusinessDashboard',
    component: BusinessDashboard,
    meta: { requiresAuth: true, requiresBusiness: true }
  },
  {
    path: '/merchant-manager',
    name: 'MerchantManager',
    component: MerchantManager,
    meta: { requiresAuth: true, requiresAdmin: true },
    alias: '/business-list'
  },
  {
    path: '/business-info/:id',
    name: 'BusinessInfo',
    component: BusinessInfo,
    meta: { requiresAuth: true }
  },
  {
    path: '/food-management',
    name: 'FoodManagement',
    component: FoodManagement,
    meta: { requiresAuth: true, requiresBusiness: true }
  },
  {
    path: '/order',
    name: 'Order',
    component: Order,
    meta: { requiresAuth: true }
  },
  {
    path: '/payment',
    name: 'Payment',
    component: Payment,
    meta: { requiresAuth: true }
  },
  {
    path: '/user-address',
    name: 'UserAddress',
    component: UserAddress,
    meta: { requiresAuth: true }
  },
  {
    path: '/add-user-address',
    name: 'AddUserAddress',
    component: AddUserAddress,
    meta: { requiresAuth: true }
  },
  {
    path: '/edit-user-address/:id',
    name: 'EditUserAddress',
    component: EditUserAddress,
    meta: { requiresAuth: true }
  },
  {
    path: '/order-list',
    name: 'OrderList',
    component: OrderList,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin)
  const requiresBusiness = to.matched.some(record => record.meta.requiresBusiness)

  if (requiresAuth && !Auth.isLoggedIn()) {
    // 需要登录但未登录，跳转到登录页
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (requiresAdmin && !Auth.isAdmin()) {
    // 需要管理员权限但不是管理员
    next({ name: 'Index' })
  } else if (requiresBusiness && !Auth.isBusiness()) {
    // 需要商家权限但不是商家
    next({ name: 'Index' })
  } else {
    next()
  }
})

export default router
