<template>
  <div class="welcome-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <h2>欢迎回来！</h2>
        </div>
      </template>
      <div class="welcome-content">
        <el-result
          icon="success"
          title="登录成功"
          sub-title="欢迎使用本系统"
        >
          <template #extra>
            <el-button type="primary" @click="handleLogout">退出登录</el-button>
          </template>
        </el-result>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const handleLogout = () => {
  // TODO: 这里添加退出登录接口调用
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.welcome-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.welcome-card {
  width: 600px;
}

.card-header {
  text-align: center;
}

.welcome-content {
  padding: 20px;
}
</style> 