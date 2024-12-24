<template>
  <el-container class="layout-container">
    <!-- 头部 -->
    <el-header class="header">
      <div class="logo">理工电商管理系统</div>
      <div class="header-right">
        <el-tooltip content="文档" placement="bottom">
          <el-button link><el-icon><Document /></el-icon></el-button>
        </el-tooltip>
        <el-tooltip content="全屏" placement="bottom">
          <el-button link><el-icon><FullScreen /></el-icon></el-button>
        </el-tooltip>
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="30" :src="userAvatar" />
            <span class="username">{{ username }}</span>
            <el-tag size="small" :type="roleType" class="role-tag">
              {{ roleName }}
            </el-tag>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu
          :router="true"
          :default-active="activeMenu"
          class="side-menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item v-if="isRouteAvailable('/home')" index="/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>

          <!-- 商品管理 -->
          <el-sub-menu index="2" v-if="isRouteAvailable('/product')">
            <template #title>
              <el-icon><Goods /></el-icon>
              <span>商品</span>
            </template>
            <el-menu-item v-if="isRouteAvailable('/product/list')" index="/product/list">商品列表</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/product/add')" index="/product/add">添加商品</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/product/category')" index="/product/category">商品分类</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/product/type')" index="/product/type">商品类型</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/product/brand')" index="/product/brand">品牌管理</el-menu-item>
          </el-sub-menu>

          <!-- 订单管理 -->
          <el-sub-menu index="3" v-if="isRouteAvailable('/order')">
            <template #title>
              <el-icon><ShoppingCart /></el-icon>
              <span>订单</span>
            </template>
            <el-menu-item v-if="isRouteAvailable('/order/list')" index="/order/list">订单列表</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/order/settings')" index="/order/settings">订单设置</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/order/return')" index="/order/return">退货申请处理</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/order/reason')" index="/order/reason">退货原因设置</el-menu-item>
          </el-sub-menu>

          <!-- 营销管理 -->
          <el-sub-menu index="4" v-if="isRouteAvailable('/marketing')">
            <template #title>
              <el-icon><PieChart /></el-icon>
              <span>营销</span>
            </template>
            <el-menu-item v-if="isRouteAvailable('/marketing/seckill')" index="/marketing/seckill">秒杀活动列表</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/marketing/coupon')" index="/marketing/coupon">优惠券列表</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/marketing/brand')" index="/marketing/brand">品牌推荐</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/marketing/new')" index="/marketing/new">新品推荐</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/marketing/popular')" index="/marketing/popular">人气推荐</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/marketing/topic')" index="/marketing/topic">专题推荐</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/marketing/ad')" index="/marketing/ad">广告列表</el-menu-item>
          </el-sub-menu>

          <!-- 权限管理 -->
          <el-sub-menu index="5" v-if="isRouteAvailable('/permission')">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>权限</span>
            </template>
            <el-menu-item v-if="isRouteAvailable('/permission/user')" index="/permission/user">用户列表</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/permission/role')" index="/permission/role">角色列表</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/permission/menu')" index="/permission/menu">菜单列表</el-menu-item>
            <el-menu-item v-if="isRouteAvailable('/permission/resource')" index="/permission/resource">资源列表</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <!-- 主要内容区 -->
      <el-main>
        <router-view v-slot="{ Component }">
          <component :is="Component" />
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserName, getUserInfo, getRoleMenus, getMenuById } from '@/api/auth'
import eventBus from '@/utils/eventBus'
import {
  House,
  Setting,
  Document,
  FullScreen,
  ShoppingCart,
  Goods,
  User,
  PieChart,
  Bell
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const username = ref('')
const userAvatar = ref('')
const activeMenu = computed(() => route.path)
const roleName = ref('')
const roleType = ref('')
const userRole = ref('')
const roleId = ref('')
const availableRoutes = ref([])

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const res = await getUserInfo()
    if (res.code === 200) {
      username.value = res.data.user.username
      userAvatar.value = res.data.user.avatar
      
      // 从role字符串中提取角色信息
      const roleStr = res.data.role
      if (roleStr.includes('name=')) {
        const nameMatch = roleStr.match(/name=([^,]+)/)
        const codeMatch = roleStr.match(/code=([^,]+)/)
        const idMatch = roleStr.match(/id=(\d+)/)
        
        roleName.value = nameMatch ? nameMatch[1] : '未知角色'
        userRole.value = codeMatch ? codeMatch[1] : ''
        roleId.value = idMatch ? idMatch[1] : ''
        console.log('当前用户角色:', roleName.value, userRole.value, roleId.value)
        
        // 根据角色code设置标签类型
        if (codeMatch) {
          switch (codeMatch[1]) {
            case 'admin':
              roleType.value = 'danger'
              break
            case 'manager':
              roleType.value = 'warning'
              break
            case 'user':
              roleType.value = 'info'
              break
            default:
              roleType.value = ''
          }
        }
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 监听用户信息更新事件
eventBus.on('updateUserInfo', () => {
  fetchUserInfo()
})

const fetchMenuPaths = async () => {
  try {
    console.log('当前角色ID:', roleId.value)
    
    if (!roleId.value) {
      console.warn('未获取到用户角色ID')
      return
    }

    const roleMenusRes = await getRoleMenus(roleId.value)
    console.log('角色菜单响应:', roleMenusRes)
    
    if (roleMenusRes.code === 200 && roleMenusRes.data.menus) {
      const menuPromises = roleMenusRes.data.menus.map(menu => 
        getMenuById(menu.menu_id)
      )
      
      const menuResults = await Promise.all(menuPromises)
      console.log('菜单详情结果:', menuResults)
      
      // 保存可访问的路径
      availableRoutes.value = menuResults
        .filter(res => res.code === 200 && res.data.menu.path)
        .map(res => res.data.menu.path)
      
      console.log('当前用户可访问的路径:', availableRoutes.value)

      // 检查当前路由是否可访问
      checkCurrentRoute()
    }
  } catch (error) {
    console.error('获取菜单路径失败:', error)
  }
}

// 添加检查当前路由的方法
const checkCurrentRoute = () => {
  const currentPath = route.path
  if (currentPath !== '/login' && !availableRoutes.value.includes(currentPath)) {
    ElMessage.error('您没有权限访问该页面')
    router.push('/home')
  }
}

// 添加路由监听
watch(
  () => route.path,
  (newPath) => {
    if (newPath !== '/login' && availableRoutes.value.length > 0) {
      if (!availableRoutes.value.includes(newPath)) {
        ElMessage.error('您没有权限访问该页面')
        router.push('/home')
      }
    }
  }
)

// 修改菜单显示逻辑
const isRouteAvailable = (path) => {
  return availableRoutes.value.includes(path)
}

onMounted(async () => {
  await fetchUserInfo()
  await fetchMenuPaths()
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  eventBus.off('updateUserInfo')
})

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      // 清除登录信息
      localStorage.removeItem('credentials')
      router.push('/login')
      ElMessage.success('退出成功')
    }).catch(() => {})
  }
}

// 添加计算属性来判断是否为普通用户
const isNormalUser = computed(() => userRole.value === 'user')
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.header {
  background-color: #ffffff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
  font-size: 14px;
}

.side-menu {
  height: 100%;
  border-right: none;
}

.el-menu-item, .el-sub-menu {
  .el-icon {
    margin-right: 8px;
  }
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}

.role-tag {
  margin-left: 8px;
}
</style> 