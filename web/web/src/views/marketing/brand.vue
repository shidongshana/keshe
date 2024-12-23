<script setup>

</script>

<template>
  <div class="container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>品牌推荐</span>
          <el-button type="primary">添加推荐</el-button>
        </div>
      </template>
      <el-empty description="品牌推荐功能开发中...">
        <el-button type="primary">敬请期待</el-button>
      </el-empty>
    </el-card>
  </div>
</template>

<style scoped>
.container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>