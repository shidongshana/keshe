<script setup>

</script>

<template>
  <div class="container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>订单列表</span>
          <div class="header-operations">
            <el-button type="primary">导出订单</el-button>
            <el-button type="success">批量发货</el-button>
          </div>
        </div>
      </template>
      <el-empty description="订单列表功能开发中...">
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

.header-operations {
  display: flex;
  gap: 10px;
}
</style>