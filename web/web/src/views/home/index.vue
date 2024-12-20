<template>
  <div class="dashboard">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <h2>欢迎使用后台管理系统</h2>
        </div>
      </template>
      <div class="system-info">
        <h3>系统信息</h3>
        <el-descriptions :column="3" border size="large">
          <el-descriptions-item label="核心框架">Spring Boot</el-descriptions-item>
          <el-descriptions-item label="持久层框架">MyBatis</el-descriptions-item>
          <el-descriptions-item label="前端框架">Vue 3</el-descriptions-item>
          <el-descriptions-item label="UI框架">Element Plus</el-descriptions-item>
          <el-descriptions-item label="构建工具">Vite</el-descriptions-item>
          <el-descriptions-item label="作者">ZHB</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <el-table :data="users" style="width: 100%">
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="username" label="用户名" width="100"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="150"></el-table-column>
      <el-table-column prop="city" label="城市" width="100"></el-table-column>
      <el-table-column prop="created" label="创建时间" width="180"></el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? 'Active' : 'Inactive' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="avatar" label="头像" width="100">
        <template #default="scope">
          <img :src="scope.row.avatar" alt="avatar" style="width: 50px; height: 50px; border-radius: 50%;">
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { test5 } from '@/api/auth.js'; // 请根据实际路径修改

const users = ref([]);

onMounted(() => {
  test5().then(response => {
    if (response.code === 200) {
      users.value = response.data;
    } else {
      console.error('Failed to fetch data:', response.message);
    }
  }).catch(error => {
    console.error('Error:', error);
  });
});
</script>

<style scoped>
.dashboard {
  min-height: 100%;
}

.welcome-card, .user-table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.system-info {
  margin-top: 20px;
}

h3 {
  margin-bottom: 20px;
  font-weight: 500;
  color: #303133;
}

:deep(.el-descriptions) {
  width: 100%;
}

:deep(.el-descriptions__body) {
  background-color: #fff;
}

:deep(.el-descriptions__label) {
  width: 120px;
  color: #606266;
}

:deep(.el-descriptions__content) {
  color: #303133;
}
</style>