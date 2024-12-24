<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/auth'

const roleList = ref([])
const dialogVisible = ref(false)
const loading = ref(true)

// 新角色表单数据
const roleForm = ref({
  name: '',
  code: '',
  remark: ''
})



// 表单规则
const rules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

// 获取所有角色
const getRoles = async () => {
  try {
    loading.value = true
    const res = await getRoleList()
    if (res.code === 200) {
      roleList.value = res.data.roles
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

const roleFormRef = ref(null)

// 添加角色
const handleAdd = async (formEl) => {
  console.log('表单数据:', roleForm.value)
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const params = {
          name: roleForm.value.name,
          code: roleForm.value.code,
          remark: roleForm.value.remark
        }
        console.log('发送到后端的数据:', params)
        const res = await addRole(params)
        if (res.code === 200) {
          ElMessage.success('添加成功')
          dialogVisible.value = false
          getRoles()
          formEl.resetFields()
        } else {
          ElMessage.error(res.message || '添加失败')
        }
      } catch (error) {
        console.error('添加角色错误:', error)
        ElMessage.error('添加失败')
      }
    }
  })
}

// 删除角色
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认要删除该角色吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteRole(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      getRoles()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 编辑角色
const editForm = ref({...roleForm.value})
const editDialogVisible = ref(false)
const currentEditId = ref(null)

// 添加 editFormRef 引用
const editFormRef = ref(null)

const handleEdit = (row) => {
  currentEditId.value = row.id
  editForm.value = {...row}
  editDialogVisible.value = true
  // 等待 DOM 更新后重置表单验证状态
  nextTick(() => {
    if (editFormRef.value) {
      editFormRef.value.clearValidate()
    }
  })
}

const handleUpdate = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const params = {
          id: currentEditId.value,
          name: editForm.value.name,
          code: editForm.value.code,
          remark: editForm.value.remark
        }
        const res = await updateRole(params)
        if (res.code === 200) {
          ElMessage.success('更新成功')
          editDialogVisible.value = false
          getRoles()
        } else {
          ElMessage.error(res.message || '更新失败')
        }
      } catch (error) {
        console.error('更新角色错误:', error)
        ElMessage.error('更新失败')
      }
    }
  })
}

onMounted(() => {
  getRoles()
})
</script>

<template>
  <div class="role-manage">
    <div class="header">
      <el-button type="primary" @click="dialogVisible = true">添加角色</el-button>
    </div>

    <el-table :data="roleList" v-loading="loading" style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="ID" width="180" />
      <el-table-column prop="name" label="角色名称" />
      <el-table-column prop="code" label="角色编码" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="created" label="创建时间" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加角色对话框 -->
    <el-dialog v-model="dialogVisible" title="添加角色" width="500px">
      <el-form ref="roleFormRef" :model="roleForm" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="roleForm.code" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="roleForm.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAdd(roleFormRef)">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑角色对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑角色" width="500px">
      <el-form 
        ref="editFormRef"
        :model="editForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="editForm.code" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdate(editFormRef)">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.role-manage {
  padding: 20px;
}

.header {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>