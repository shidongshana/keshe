<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getAllMenus, 
  addMenu, 
  updateMenu, 
  updateMenuStatus,
  getMenuRoles,
  getRoleInfo as fetchRoleInfo
} from '@/api/auth'

const menuList = ref([])
const dialogVisible = ref(false)
const loading = ref(true)
const editDialogVisible = ref(false)

// 菜单表单数据
const menuForm = ref({
  name: '',
  path: '',
  component: '',
  icon: '',
  type: 0,
  orderNum: 0,
  status: 1
})

// 编辑表单数据
const editForm = ref({...menuForm.value})
const currentEditId = ref(null)

// 表单规则
const rules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入菜单路径', trigger: 'blur' }]
}

// 添加新的响应式变量
const roleCache = ref(new Map()) // 用于缓存角色信息

// 在 getMenus 函数后添加新的方法
const getRoleMenus = async (menuId) => {
  try {
    const res = await getMenuRoles(menuId)
    if (res.code === 200) {
      return res.data.menus
    }
    return []
  } catch (error) {
    console.error('获取角色菜单失败:', error)
    return []
  }
}

// 添加获取角色详情的方法
const fetchRoleDetails = async (roleId) => {
  if (roleCache.value.has(roleId)) {
    return roleCache.value.get(roleId)
  }
  
  try {
    const res = await fetchRoleInfo(roleId)
    if (res.code === 200) {
      const roleInfo = res.data.role
      roleCache.value.set(roleId, roleInfo)
      return roleInfo
    }
    return null
  } catch (error) {
    console.error('获取角色详情失败:', error)
    return null
  }
}

// 修改 getMenus 方法
const getMenus = async () => {
  try {
    loading.value = true
    const res = await getAllMenus()
    if (res.code === 200) {
      const menus = res.data.menus.filter(menu => menu.parent_id === 0)
      // 获取每个菜单的角色信息
      for (const menu of menus) {
        console.log('Processing menu:', menu)
        const roleIds = await getRoleMenus(menu.id)
        console.log('Role IDs:', roleIds)
        // 获取每个角色的详细信息
        const roleInfos = await Promise.all(
          roleIds.map(roleId => fetchRoleDetails(roleId))
        )
        console.log('Role Infos:', roleInfos)
        menu.roles = roleInfos.filter(role => role !== null)
      }
      menuList.value = menus
      console.log('Final menu list:', menuList.value)
    }
  } catch (error) {
    console.error('获取菜单列表失败:', error)
    ElMessage.error('获取菜单列表失败')
  } finally {
    loading.value = false
  }
}

// 添加菜单
const handleAdd = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const res = await addMenu({
          ...menuForm.value,
          parent_id: 0 // 确保是顶级菜单
        })
        if (res.code === 200) {
          ElMessage.success('添加成功')
          dialogVisible.value = false
          getMenus()
          menuForm.value = {
            name: '',
            path: '',
            component: '',
            icon: '',
            type: 0,
            orderNum: 0,
            status: 1
          }
        }
      } catch (error) {
        ElMessage.error('添加失败')
      }
    }
  })
}

// 编辑菜单
const handleEdit = (row) => {
  currentEditId.value = row.id
  editForm.value = {...row}
  editDialogVisible.value = true
}

// 更新菜单
const handleUpdate = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const res = await updateMenu({
          ...editForm.value,
          id: currentEditId.value,
          parent_id: 0
        })
        
        if (res.code === 200) {
          ElMessage.success('更新成功')
          editDialogVisible.value = false
          getMenus()
          editForm.value = {
            name: '',
            path: '',
            component: '',
            icon: '',
            type: 0,
            orderNum: 0,
            status: 1
          }
        } else {
          ElMessage.error(res.message || '更新失败')
        }
      } catch (error) {
        ElMessage.error('更新失败')
      }
    }
  })
}

// 替换原来的 handleDelete 方法为 handleStatusChange
const handleStatusChange = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const actionText = newStatus === 1 ? '启用' : '禁用'
  
  try {
    await ElMessageBox.confirm(`确认要${actionText}该菜单吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await updateMenuStatus(row.id, newStatus)
    if (res.code === 200) {
      ElMessage.success(`${actionText}成功`)
      getMenus()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText}失败`)
    }
  }
}

const editFormRef = ref(null)  // 添加表单引用

onMounted(() => {
  getMenus()
})
</script>

<template>
  <div class="menu-manage">
    <div class="header">
      <el-button type="primary" @click="dialogVisible = true">添加菜单</el-button>
    </div>

    <el-table :data="menuList" v-loading="loading" style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="菜单名称" />
      <el-table-column prop="path" label="路径" />
      <el-table-column prop="component" label="组件" />
      <el-table-column prop="icon" label="图标" />
      <el-table-column prop="orderNum" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roles" label="可访问角色" min-width="200">
        <template #default="scope">
          <el-tag 
            v-for="role in scope.row.roles" 
            :key="role.id"
            class="mx-1"
            size="small"
            :type="role.status === 1 ? 'success' : 'info'"
            :title="role.remark"
          >
            {{ role.name }}
          </el-tag>
          <span v-if="!scope.row.roles?.length">暂无角色</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button 
            size="small" 
            :type="scope.row.status === 1 ? 'danger' : 'success'"
            @click="handleStatusChange(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加菜单对话框 -->
    <el-dialog v-model="dialogVisible" title="添加菜单" width="500px">
      <el-form ref="menuFormRef" :model="menuForm" :rules="rules" label-width="100px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menuForm.name" />
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="menuForm.path" />
        </el-form-item>
        <el-form-item label="组件" prop="component">
          <el-input v-model="menuForm.component" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="menuForm.icon" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="menuForm.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="menuForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAdd(menuFormRef)">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑菜单对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑菜单" width="500px">
      <el-form 
        ref="editFormRef"
        :model="editForm" 
        :rules="rules" 
        label-width="100px"
        @submit.prevent="handleUpdate(editFormRef)"
      >
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="editForm.path" />
        </el-form-item>
        <el-form-item label="组件" prop="component">
          <el-input v-model="editForm.component" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="editForm.icon" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="editForm.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="editForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="editForm.type">
            <el-option :value="0" label="目录" />
            <el-option :value="1" label="菜单" />
            <el-option :value="2" label="按钮" />
          </el-select>
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
.menu-manage {
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

/* 添加新的样式 */
.mx-1 {
  margin: 0 4px;
}
</style>