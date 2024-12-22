<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getCategoryList,
  createCategory,
  updateCategory,
  deleteCategory,
  getCategoryTotal
} from '@/api/auth'

// 数据相关
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

// 表单相关
const formRef = ref(null)
const form = ref({
  name: '',
  description: '',
  parent_id: 0,
  status: 1
})
const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
}

// 添加排序相关的响应式数据
const sortBy = ref('')
const sortOrder = ref('')

// 获取分类列表
const fetchData = async () => {
  loading.value = true
  try {
    // 先获取总数
    const totalRes = await getCategoryTotal()
    if (totalRes.code === 200) {
      total.value = totalRes.data
    }

    // 再获取列表数据，添加排序参数
    const res = await getCategoryList(
      currentPage.value, 
      pageSize.value, 
      sortBy.value, 
      sortOrder.value
    )
    if (res.code === 200) {
      tableData.value = res.data
    } else {
      ElMessage.error(res.message || '获取分类列表失败')
    }
  } catch (error) {
    console.error('获取分类列表出错:', error)
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

// 处理分页变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

// 打开新增对话框
const handleAdd = () => {
  dialogTitle.value = '新增分类'
  form.value = {
    name: '',
    description: '',
    parent_id: 0,
    status: 1
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = (row) => {
  dialogTitle.value = '编辑分类'
  form.value = { ...row }
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let res
        if (form.value.id) {
          // 编辑
          res = await updateCategory(form.value.id, form.value)
        } else {
          // 新增
          res = await createCategory(form.value)
        }
        
        if (res.code === 200) {
          ElMessage.success(form.value.id ? '更新成功' : '添加成功')
          dialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.message || (form.value.id ? '更新失败' : '添加失败'))
        }
      } catch (error) {
        console.error('提交表单出错:', error)
        ElMessage.error(form.value.id ? '更新失败' : '添加失败')
      }
    }
  })
}

// 删除分类
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteCategory(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error('删除分类出错:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 添加排序处理函数
const handleSort = ({ prop, order }) => {
  sortBy.value = prop
  sortOrder.value = order === 'ascending' ? 'asc' : order === 'descending' ? 'desc' : ''
  fetchData()
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="category-container">
    <!-- 顶部操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">新增分类</el-button>
    </div>

    <!-- 表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      style="width: 100%"
      @sort-change="handleSort"
    >
      <el-table-column 
        prop="id" 
        label="ID" 
        width="80" 
        sortable="custom"
      />
      <el-table-column 
        prop="name" 
        label="分类名称"
        sortable="custom"
      />
      <el-table-column 
        prop="description" 
        label="描述"
      />
      <el-table-column 
        prop="parent_id" 
        label="父级ID" 
        width="80"
        sortable="custom"
      >
        <template #default="scope">
          {{ scope.row.parent_id || '无' }}
        </template>
      </el-table-column>
      <el-table-column 
        prop="created" 
        label="创建时间" 
        width="180"
        sortable="custom"
      >
        <template #default="scope">
          {{ scope.row.created ? new Date(scope.row.created).toLocaleString() : '无' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="父级分类">
          <el-input-number
            v-model="form.parent_id"
            :min="0"
            controls-position="right"
          />
          <span class="form-tip">0 表示顶级分类</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.category-container {
  padding: 20px;
}

.operation-bar {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.form-tip {
  margin-left: 8px;
  color: #909399;
  font-size: 12px;
}
</style>