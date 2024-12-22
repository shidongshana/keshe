<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getTypeList,
  createType,
  updateType,
  deleteType,
  getTypeTotal
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

// 排序相关
const sortBy = ref('')
const sortOrder = ref('')

// 表单相关
const formRef = ref(null)
const form = ref({
  name: '',
  category_id: 0,
  description: '',
  status: 1
})
const rules = {
  name: [{ required: true, message: '请输入类型名称', trigger: 'blur' }],
  category_id: [{ required: true, message: '请选择所属分类', trigger: 'blur' }]
}

// 获取类型列表
const fetchData = async () => {
  loading.value = true
  try {
    const totalRes = await getTypeTotal()
    if (totalRes.code === 200) {
      total.value = totalRes.data
    }

    const res = await getTypeList(
      currentPage.value, 
      pageSize.value, 
      sortBy.value, 
      sortOrder.value
    )
    if (res.code === 200) {
      tableData.value = res.data
    } else {
      ElMessage.error(res.message || '获取类型列表失败')
    }
  } catch (error) {
    console.error('获取类型列表出错:', error)
    ElMessage.error('获取类型列表失败')
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

// 处理排序
const handleSort = ({ prop, order }) => {
  sortBy.value = prop
  sortOrder.value = order === 'ascending' ? 'asc' : order === 'descending' ? 'desc' : ''
  fetchData()
}

// 打开新增对话框
const handleAdd = () => {
  dialogTitle.value = '新增类型'
  form.value = {
    name: '',
    category_id: 0,
    description: '',
    status: 1
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = (row) => {
  dialogTitle.value = '编辑类型'
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
          res = await updateType(form.value.id, form.value)
        } else {
          res = await createType(form.value)
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

// 删除类型
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该类型吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteType(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error('删除类型出错:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="type-container">
    <!-- 顶部操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">新增类型</el-button>
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
        label="类型名称"
        sortable="custom"
      />
      <el-table-column 
        prop="category_id" 
        label="所属分类ID" 
        width="100"
        sortable="custom"
      />
      <el-table-column 
        prop="description" 
        label="描述"
      />
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

    <!-- 新增/编辑对话框 -->
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
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="所属分类" prop="category_id">
          <el-input-number
            v-model="form.category_id"
            :min="0"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
          />
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
.type-container {
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
</style>