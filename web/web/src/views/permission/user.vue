<script setup>
import { ref, onMounted, computed } from 'vue'
import { test5, updateUser, updateUserStatus, getUserList } from '@/api/auth.js'
import { Search } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const searchQuery = ref('')
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const editForm = ref({
  id: '',
  username: '',
  email: '',
  city: '',
  status: 1
})

// 表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  city: [
    { required: true, message: '请输入城市', trigger: 'blur' }
  ]
}

// 获取用户列表
const fetchUsers = async () => {
  try {
    const response = await getUserList(currentPage.value, pageSize.value)
    if (response.code === 200) {
      tableData.value = response.data.records
      total.value = response.data.total
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 搜索用户
const handleSearch = () => {
  currentPage.value = 1
  fetchUsers()
}

// 打开编辑对话框
const handleEdit = (row) => {
  editForm.value = {
    id: row.id,
    username: row.username,
    email: row.email,
    city: row.city,
    status: row.status
  }
  dialogVisible.value = true
}

// 提交编辑表单
const editFormRef = ref(null)
const handleSubmit = async () => {
  if (!editFormRef.value) return
  
  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await updateUser(editForm.value)
        if (response.code === 200) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          fetchUsers() // 重新获取用户列表
        } else {
          ElMessage.error(response.message || '更新失败')
        }
      } catch (error) {
        console.error('更新用户信息失败:', error)
        ElMessage.error('更新失败，请重试')
      }
    }
  })
}

// 修改用户状态
const handleDelete = (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  ElMessageBox.confirm(
    `确定要${row.status === 1 ? '禁用' : '启用'}该用户吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const response = await updateUserStatus(row.id, newStatus)
      if (response.code === 200) {
        row.status = newStatus
        ElMessage.success(`已${newStatus === 1 ? '启用' : '禁用'}用户：${row.username}`)
      } else {
        ElMessage.error(response.message || '操作失败')
      }
    } catch (error) {
      console.error('更新用户状态失败:', error)
      ElMessage.error('操作失败，请重试')
    }
  }).catch(() => {
    // 用户取消操作，不做任何处理
  })
}

// 处理页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchUsers()
}

// 处理每页条数改变
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchUsers()
}

onMounted(() => {
  fetchUsers()
})
</script>

<template>
  <div class="container">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-input
          v-model="searchQuery"
          placeholder="输入关键字搜索"
          style="width: 200px"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <!-- 表格区域 -->
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="username" label="账号" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="city" label="城市" width="120" />
        <el-table-column prop="created" label="创建时间" width="180" />
        <el-table-column prop="updated" label="更新时间" width="180" />

        <!-- 状态��� -->
        <el-table-column label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 操作列 -->
        <el-table-column label="操作" align="center" width="180">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              :type="scope.row.status === 1 ? 'danger' : 'success'"
              link
              @click="handleDelete(scope.row)"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :current-page="currentPage"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>

      <!-- 编辑对话框 -->
      <el-dialog
        v-model="dialogVisible"
        title="编辑用户"
        width="500px"
      >
        <el-form
          ref="editFormRef"
          :model="editForm"
          :rules="rules"
          label-width="80px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="editForm.username" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="editForm.email" />
          </el-form-item>
          <el-form-item label="城市" prop="city">
            <el-input v-model="editForm.city" />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch
              v-model="editForm.status"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit">
              确定
            </el-button>
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<style scoped>
.container {
  padding: 20px;
}

.search-area {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table) {
  margin-top: 20px;
}

:deep(.el-button--link) {
  padding: 0 5px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>