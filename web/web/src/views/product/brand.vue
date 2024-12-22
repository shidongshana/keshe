<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBrandList, searchBrands, addBrand, updateBrand, deleteBrand } from '@/api/auth'

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const queryParams = ref({
  page: 1,
  limit: 5,
  name: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref({
  id: null,
  name: '',
  logo: '',
  description: '',
  status: 1
})

// 获取品牌列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getBrandList(queryParams.value)
    if (res.code === 200) {
      tableData.value = res.data.items
      total.value = res.data.total
    }
  } catch (error) {
    // 保留错误处理，但移除console输出
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = async () => {
  if (!queryParams.value.name) {
    getList()
    return
  }
  loading.value = true
  try {
    const res = await searchBrands(queryParams.value)
    if (res.code === 200) {
      tableData.value = res.data.items
      total.value = res.data.total
    }
  } catch (error) {
    // 保留错误处理，但移除console输出
  } finally {
    loading.value = false
  }
}

// 重置
const handleReset = () => {
  queryParams.value = {
    page: 1,
    limit: 5,
    name: ''
  }
  getList()
}

// 添加/编辑
const handleAdd = () => {
  dialogTitle.value = '添加品牌'
  form.value = {
    id: null,
    name: '',
    logo: '',
    description: '',
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑品牌'
  form.value = JSON.parse(JSON.stringify(row))
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  const api = form.value.id ? updateBrand : addBrand
  try {
    const res = await api(form.value)
    if (res.code === 200) {
      ElMessage.success(res.msg)
      dialogVisible.value = false
      getList()
    }
  } catch (error) {
    // 保留错误处理，但移除console输出
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认要删除该品牌吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteBrand(row.id)
      if (res.code === 200) {
        ElMessage.success(res.msg)
        getList()
      }
    } catch (error) {
      // 保留错误处理，但移除console输出
    }
  })
}

// 分页
const handleSizeChange = (val) => {
  queryParams.value.limit = val
  getList()
}

const handleCurrentChange = (val) => {
  queryParams.value.page = val
  getList()
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <!-- 搜索工具栏 -->
    <div class="search-box">
      <el-form :inline="true" :model="queryParams">
        <el-form-item>
          <el-input
            v-model="queryParams.name"
            placeholder="品牌名称"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="品牌名称" />
      <el-table-column prop="logo" label="品牌Logo">
        <template #default="{ row }">
          <el-image
            v-if="row.logo"
            :src="row.logo"
            style="width: 50px; height: 50px"
          />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="danger" link @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.limit"
        :page-sizes="[5, 10, 20, 30]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="dialogVisible = false"
    >
      <el-form
        ref="formRef"
        :model="form"
        label-width="100px"
      >
        <el-form-item label="品牌名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入品牌名称" />
        </el-form-item>
        <el-form-item label="品牌Logo" prop="logo">
          <el-input v-model="form.logo" placeholder="请输入品牌Logo地址" />
        </el-form-item>
        <el-form-item label="品牌描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入品牌描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.app-container {
  padding: 20px;
}

.search-box {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>