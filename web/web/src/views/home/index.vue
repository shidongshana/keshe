<template>
  <div class="dashboard">
    <!-- 顶部卡片统计 -->
    <div class="stat-cards">
      <el-card class="stat-card">
        <div class="stat-header">
          <span class="stat-title">今日订单数</span>
          <el-icon class="stat-icon"><Document /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-number">200</span>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-header">
          <span class="stat-title">今日销售总额</span>
          <el-icon class="stat-icon"><Money /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-number">¥ 5000.00</span>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-header">
          <span class="stat-title">累计销售总额</span>
          <el-icon class="stat-icon"><Coin /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-number">¥ 5000.00</span>
        </div>
      </el-card>
    </div>

    <!-- 订单统计 -->
    <div class="order-stats">
      <el-card class="order-card">
        <template #header>
          <div class="card-header">
            <span>待处理事务</span>
          </div>
        </template>
        <div class="order-items">
          <div class="order-item">
            <span>待付款订单</span>
            <el-tag>(10)</el-tag>
          </div>
          <div class="order-item">
            <span>待发货订单</span>
            <el-tag>(10)</el-tag>
          </div>
          <div class="order-item">
            <span>已发货订单</span>
            <el-tag>(10)</el-tag>
          </div>
          <div class="order-item">
            <span>已完成订单</span>
            <el-tag>(10)</el-tag>
          </div>
          <div class="order-item">
            <span>新品待审核</span>
            <el-tag>(10)</el-tag>
          </div>
          <div class="order-item">
            <span>特价待审核</span>
            <el-tag>(10)</el-tag>
          </div>
          <div class="order-item">
            <span>待处理退货订单</span>
            <el-tag>(10)</el-tag>
          </div>
          <div class="order-item">
            <span>广告位到期</span>
            <el-tag>(10)</el-tag>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 商品统计 -->
    <div class="product-stats">
      <el-card class="product-card">
        <div class="stat-grid">
          <div class="stat-item">
            <div class="stat-value">100</div>
            <div class="stat-label">已下架</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">400</div>
            <div class="stat-label">已上架</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">50</div>
            <div class="stat-label">库存紧张</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">500</div>
            <div class="stat-label">全部商品</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 销售统计图表 -->
    <div class="sales-chart">
      <el-card>
        <template #header>
          <div class="chart-header">
            <span>订单统计</span>
            <div class="chart-info">
              <div class="info-item">
                <span>本月订单总数</span>
                <span class="info-value">10000</span>
                <span class="trend up">+10% 同比上升</span>
              </div>
              <div class="info-item">
                <span>本周订单总数</span>
                <span class="info-value">1000</span>
                <span class="trend down">-10% 同比下降</span>
              </div>
              <div class="info-item">
                <span>本月销售总额</span>
                <span class="info-value">100000</span>
                <span class="trend up">+10% 同比上升</span>
              </div>
              <div class="info-item">
                <span>本周销售总额</span>
                <span class="info-value">50000</span>
                <span class="trend down">-10% 同比下降</span>
              </div>
            </div>
          </div>
        </template>
        <div ref="chartRef" style="height: 400px;"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Document, Money, Coin } from '@element-plus/icons-vue';
import * as echarts from 'echarts';

const chartRef = ref(null);

onMounted(() => {
  const chart = echarts.init(chartRef.value);
  
  // 模拟数据
  const xData = Array.from({ length: 30 }, (_, i) => `11-${i + 1}`);
  const yData1 = Array.from({ length: 30 }, () => Math.floor(Math.random() * 5000 + 3000));
  const yData2 = Array.from({ length: 30 }, () => Math.floor(Math.random() * 2000 + 1000));

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xData
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '订单金额',
        type: 'line',
        stack: 'Total',
        smooth: true,
        lineStyle: {
          width: 0
        },
        areaStyle: {
          opacity: 0.8,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgb(128, 255, 165)'
            },
            {
              offset: 1,
              color: 'rgb(1, 191, 236)'
            }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: yData1
      },
      {
        name: '订单数量',
        type: 'line',
        stack: 'Total',
        smooth: true,
        lineStyle: {
          width: 0
        },
        areaStyle: {
          opacity: 0.8,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgb(0, 221, 255)'
            },
            {
              offset: 1,
              color: 'rgb(77, 119, 255)'
            }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: yData2
      }
    ]
  };

  chart.setOption(option);

  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    chart.resize();
  });
});
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.stat-title {
  font-size: 16px;
  color: #606266;
}

.stat-icon {
  font-size: 24px;
  color: #409EFF;
}

.stat-content {
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.order-stats {
  margin-bottom: 20px;
}

.order-items {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.product-stats {
  margin-bottom: 20px;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  text-align: center;
}

.stat-item {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.stat-label {
  color: #606266;
}

.chart-header {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chart-info {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.trend {
  font-size: 12px;
}

.trend.up {
  color: #67C23A;
}

.trend.down {
  color: #F56C6C;
}
</style>