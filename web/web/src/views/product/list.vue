<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductList, searchProducts, updateProduct, offShelfProduct, getProductImages } from '@/api/auth'
import { Picture } from '@element-plus/icons-vue'

const tableData = ref([])
const searchName = ref('')
const loading = ref(false)
const dialogVisible = ref(false)
const currentProduct = ref({})
const previewVisible = ref(false)
const previewImages = ref([])
const currentPreviewIndex = ref(0)
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 获取商品列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getProductList({
      page: pagination.value.currentPage,
      limit: pagination.value.pageSize
    })
    
    if (res.code === 200) {
      tableData.value = res.data.items
      pagination.value.total = res.data.total
      
      for (const product of tableData.value) {
        try {
          const imageRes = await getProductImages(product.id)
          if (imageRes.code === 200 && imageRes.data.length > 0) {
            const mainImage = imageRes.data.find(img => img.is_main)
            product.main_image = mainImage ? mainImage.image_url : imageRes.data[0].image_url
          }
        } catch (error) {
          console.error('获取商品图片失败:', error)
        }
      }
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索商品
const handleSearch = async () => {
  loading.value = true
  try {
    const res = await searchProducts({
      name: searchName.value,
      page: pagination.value.currentPage,
      limit: pagination.value.pageSize
    })
    if (res.code === 200) {
      tableData.value = res.data.items
      pagination.value.total = res.data.total
      for (const product of tableData.value) {
        try {
          const imageRes = await getProductImages(product.id)
          if (imageRes.code === 200 && imageRes.data.length > 0) {
            const mainImage = imageRes.data.find(img => img.is_main)
            product.main_image = mainImage ? mainImage.image_url : imageRes.data[0].image_url
          }
        } catch (error) {
          console.error('获取商品图片失败:', error)
        }
      }
    }
  } catch (error) {
    console.error('搜索商品失败:', error)
    ElMessage.error('搜索商品失败')
  } finally {
    loading.value = false
  }
}

// 编辑商品
const handleEdit = (row) => {
  currentProduct.value = { ...row }
  dialogVisible.value = true
}

// 下架商品
const handleOffShelf = async (row) => {
  try {
    await ElMessageBox.confirm('确定要下架该商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await offShelfProduct(row.id)
    if (res.code === 200) {
      ElMessage.success('下架成功')
      getList() // 刷新列表
    } else {
      ElMessage.error(res.message || '下架失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('下架商品失败:', error)
      ElMessage.error('下架商品失败')
    }
  }
}

// 保存编辑
const handleSave = async () => {
  try {
    const res = await updateProduct(currentProduct.value)
    if (res.code === 200) {
      ElMessage.success('修改成功')
      dialogVisible.value = false
      getList() // 刷新列表
    }
  } catch (error) {
    console.error('修改商品失败:', error)
    ElMessage.error('修改商品失败')
  }
}

// 查看商品图片
const handleViewImages = async (row) => {
  try {
    const res = await getProductImages(row.id)
    if (res.code === 200 && res.data.length > 0) {
      previewImages.value = res.data.map(item => ({
        url: item.image_url,
        isMain: item.is_main
      }))
      // 将主图放在第一位
      previewImages.value.sort((a, b) => (b.isMain - a.isMain))
      currentPreviewIndex.value = 0
      previewVisible.value = true
    } else {
      ElMessage.info('暂无商品图片')
    }
  } catch (error) {
    console.error('获取商品图片失败:', error)
    ElMessage.error('获取商品图片失败')
  }
}

// 添加处理分页变化的方法
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  if (searchName.value) {
    handleSearch()
  } else {
    getList()
  }
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  if (searchName.value) {
    handleSearch()
  } else {
    getList()
  }
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="searchName"
        placeholder="请输入商品名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter="handleSearch"
      />
      <el-button class="filter-item" type="primary" @click="handleSearch">
        搜索
      </el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="tableData"
      style="width: 100%"
      border
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="商品图片" width="120">
        <template #default="scope">
          <el-image
            style="width: 80px; height: 80px"
            :src="scope.row.main_image"
            :preview-src-list="[]"
            fit="cover"
            @click="handleViewImages(scope.row)"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="price" label="价格">
        <template #default="scope">
          ¥{{ scope.row.price }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="created" label="创建时间" width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            :disabled="scope.row.status === 0"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            v-if="scope.row.status === 1"
            size="small"
            type="danger"
            @click="handleOffShelf(scope.row)"
          >
            下架
          </el-button>
          <el-button
            v-else
            size="small"
            type="info"
            disabled
          >
            已下架
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加分页组件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑商品"
      width="50%"
    >
      <el-form :model="currentProduct" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="currentProduct.name" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="currentProduct.price" :precision="2" :step="0.1" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="currentProduct.stock" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentProduct.status">
            <el-option :value="1" label="上架" />
            <el-option :value="0" label="下架" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="currentProduct.description"
            type="textarea"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="商品图片"
      width="65%"
      class="preview-dialog"
    >
      <div class="image-preview">
        <div class="preview-container">
          <!-- 主预览区域 -->
          <div class="main-preview">
            <el-image
              :src="previewImages[currentPreviewIndex]?.url"
              fit="contain"
              class="preview-image"
            />
          </div>
          <!-- 缩略图列表 -->
          <div class="thumbnail-list">
            <div
              v-for="(image, index) in previewImages"
              :key="index"
              class="thumbnail-item"
              :class="{ active: currentPreviewIndex === index }"
              @click="currentPreviewIndex = index"
            >
              <el-image
                :src="image.url"
                fit="cover"
              />
              <div v-if="image.isMain" class="main-tag">主图</div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.filter-container {
  padding-bottom: 10px;
}
.filter-item {
  margin-right: 10px;
}
.dialog-footer {
  padding-top: 20px;
  text-align: right;
}
.image-preview {
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-container {
  position: relative;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #409EFF;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

/* 修改轮播图指示器的位置 */
:deep(.el-carousel__indicators) {
  bottom: -20px;
}

:deep(.el-carousel__container) {
  margin-bottom: 20px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

/* 添加新的预览样式 */
.preview-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.preview-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.main-preview {
  width: 100%;
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.preview-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding: 10px 0;
}

.thumbnail-item {
  position: relative;
  width: 80px;
  height: 80px;
  border: 2px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  overflow: hidden;
  flex-shrink: 0;
  transition: all 0.3s;
}

.thumbnail-item:hover {
  border-color: #409EFF;
}

.thumbnail-item.active {
  border-color: #409EFF;
}

.thumbnail-item .el-image {
  width: 100%;
  height: 100%;
}

.main-tag {
  position: absolute;
  top: 4px;
  right: 4px;
  background-color: #409EFF;
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

/* 自��义滚动条样式 */
.thumbnail-list::-webkit-scrollbar {
  height: 6px;
}

.thumbnail-list::-webkit-scrollbar-thumb {
  background-color: #909399;
  border-radius: 3px;
}

.thumbnail-list::-webkit-scrollbar-track {
  background-color: #f5f7fa;
  border-radius: 3px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>