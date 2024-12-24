<template>
  <div class="resource-manage">
    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="菜单名称">
        <el-input v-model="searchForm.menuName" placeholder="请输入菜单名称" clearable />
      </el-form-item>
      <el-form-item label="菜单路径">
        <el-input v-model="searchForm.menuPath" placeholder="请输入菜单路径" clearable />
      </el-form-item>
      <el-form-item label="组件路径">
        <el-input v-model="searchForm.menuComponent" placeholder="请输入组件路径" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <div class="operation-area">
      <el-button type="primary" @click="openAddDialog">添加菜单</el-button>
    </div>

    <!-- 表格区域 -->
    <el-table :data="menuList" v-loading="loading" style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="序号" width="80" />
      <el-table-column prop="name" label="资源名称" />
      <el-table-column prop="path" label="资源路径" />
      <el-table-column prop="component" label="组件路径" />
      <el-table-column prop="perms" label="权限标识" />
      <el-table-column prop="type" label="类型" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.type === 0 ? '' : scope.row.type === 1 ? 'success' : 'warning'">
            {{ scope.row.type === 0 ? '目录' : scope.row.type === 1 ? '菜单' : '按钮' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="parent_id" label="父级菜单">
        <template #default="scope">
          {{ parentMenus.find(m => m.id === scope.row.parent_id)?.name || '顶级菜单' }}
        </template>
      </el-table-column>
      <el-table-column prop="created" label="创建时间" width="180" />
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
      <el-table-column label="操作" width="280" fixed="right">
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
          <el-button 
            size="small" 
            type="warning"
            @click="openRoleAssignDialog(scope.row)"
          >
            分配角色
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 添加菜单对话框 -->
    <el-dialog v-model="dialogVisible" title="添加菜单" width="500px">
      <el-form ref="menuFormRef" :model="menuForm" :rules="rules" label-width="100px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menuForm.name" />
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="menuForm.path" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="menuForm.component" />
        </el-form-item>
        <el-form-item label="权限标识" prop="perms">
          <el-input v-model="menuForm.perms" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="menuForm.icon" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="menuForm.type" placeholder="请选择菜单类型">
            <el-option :value="0" label="目录" />
            <el-option :value="1" label="菜单" />
            <el-option :value="2" label="按钮" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="menuForm.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="menuForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="父级菜单" prop="parent_id">
          <el-select v-model="menuForm.parent_id" placeholder="请选择父级菜单">
            <el-option :value="0" label="顶级菜单" />
            <el-option
              v-for="menu in parentMenus"
              :key="menu.id"
              :label="menu.name"
              :value="menu.id"
            />
          </el-select>
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
      >
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="editForm.path" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="editForm.component" />
        </el-form-item>
        <el-form-item label="权限标识" prop="perms">
          <el-input v-model="editForm.perms" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="editForm.icon" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="editForm.type" placeholder="请选择菜单类型">
            <el-option :value="0" label="目录" />
            <el-option :value="1" label="菜单" />
            <el-option :value="2" label="按钮" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="editForm.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="editForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdate(editFormRef)">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 角色分配对话框 -->
    <el-dialog 
      v-model="roleDialogVisible" 
      title="角色分配" 
      width="500px"
    >
      <el-form>
        <el-form-item label="选择角色">
          <div v-if="allRoles.length === 0">暂无可选角色</div>
          <el-checkbox-group v-model="selectedRoles">
            <el-checkbox 
              v-for="role in allRoles" 
              :key="role.id" 
              :label="role.id"
            >
              {{ role.name }}
              <el-tooltip :content="role.remark" placement="top">
                <el-icon class="el-icon-question"><QuestionFilled /></el-icon>
              </el-tooltip>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRoleAssign">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getAllMenus, 
  addMenu, 
  updateMenu, 
  updateMenuStatus,
  getMenuRoles,
  getRoleInfo as fetchRoleInfo,
  getRoleList,
  addMenuRole,
  deleteMenuRole
} from '@/api/auth'
import { QuestionFilled } from '@element-plus/icons-vue'

const menuList = ref([])
const dialogVisible = ref(false)
const loading = ref(true)
const editDialogVisible = ref(false)
const roleDialogVisible = ref(false)
const currentMenuId = ref(null)
const selectedRoles = ref([])

// 搜索表单
const searchForm = ref({
  menuName: '',
  menuPath: '',
  menuComponent: ''
})

// 分页参数
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 菜单表单数据
const menuForm = ref({
  name: '',
  path: '',
  component: '',
  icon: '',
  type: 0,
  orderNum: 0,
  status: 1,
  parent_id: 0
})

// 编辑表单���据
const editForm = ref({...menuForm.value})
const currentEditId = ref(null)

// 表单规则
const rules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入菜单路径', trigger: 'blur' }]
}

// 用于缓存角色信息
const roleCache = ref(new Map())

// 获取角色相关的方法
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

// 获取菜单列表
const getMenus = async () => {
  try {
    loading.value = true
    const res = await getAllMenus()
    if (res.code === 200) {
      // 过滤搜索条件
      let filteredMenus = res.data.menus
      
      // 获取每个菜单的角���信息
      for (const menu of filteredMenus) {
        const roleIds = await getRoleMenus(menu.id)
        const roleInfos = await Promise.all(
          roleIds.map(roleId => fetchRoleDetails(roleId))
        )
        menu.roles = roleInfos.filter(role => role !== null)
      }

      if (searchForm.value.menuName) {
        filteredMenus = filteredMenus.filter(menu => 
          menu.name.toLowerCase().includes(searchForm.value.menuName.toLowerCase())
        )
      }
      if (searchForm.value.menuPath) {
        filteredMenus = filteredMenus.filter(menu => 
          menu.path.toLowerCase().includes(searchForm.value.menuPath.toLowerCase())
        )
      }
      if (searchForm.value.menuComponent) {
        filteredMenus = filteredMenus.filter(menu => 
          menu.component && menu.component.toLowerCase().includes(searchForm.value.menuComponent.toLowerCase())
        )
      }
      
      pagination.value.total = filteredMenus.length
      const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
      const end = start + pagination.value.pageSize
      menuList.value = filteredMenus.slice(start, end)
    }
  } catch (error) {
    console.error('获取菜单列表失败:', error)
    ElMessage.error('获取菜单列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  pagination.value.currentPage = 1
  getMenus()
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    menuName: '',
    menuPath: '',
    menuComponent: ''
  }
  pagination.value.currentPage = 1
  getMenus()
}

// 处理分页变化
const handlePageChange = (page) => {
  pagination.value.currentPage = page
  getMenus()
}

// 处理每页条数变化
const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  getMenus()
}

// 添加菜单
const handleAdd = async (formEl) => {
  if (!formEl) return
  
  // 获取父级菜单列表
  try {
    const res = await getAllMenus()
    if (res.code === 200) {
      // 只有目录和菜单可以作为父级
      parentMenus.value = res.data.menus.filter(menu => menu.type !== 2)
    }
  } catch (error) {
    console.error('获取父级菜单失败:', error)
    return
  }

  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const res = await addMenu(menuForm.value)
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
            status: 1,
            parent_id: 0
          }
        }
      } catch (error) {
        ElMessage.error('添加失败')
      }
    }
  })
}

// 打开添加对话框
const openAddDialog = () => {
  dialogVisible.value = true
  getParentMenus()
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
          id: currentEditId.value
        })
        if (res.code === 200) {
          ElMessage.success('更新成功')
          editDialogVisible.value = false
          getMenus()
        }
      } catch (error) {
        ElMessage.error('更新失败')
      }
    }
  })
}

// 处理状态变更
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

// 获取可选的父级菜单
const parentMenus = ref([])

const getParentMenus = async () => {
  try {
    const res = await getAllMenus()
    if (res.code === 200) {
      // 只有目录和菜单可以作为父级
      parentMenus.value = res.data.menus.filter(menu => menu.type !== 2)
    }
  } catch (error) {
    console.error('获取父级菜单失败:', error)
  }
}

// 添加获取所有角色的方法
const allRoles = ref([])

const getAllRoles = async () => {
  try {
    const res = await getRoleList()
    console.log('获取角色列表响应:', res)
    if (res.code === 200) {
      allRoles.value = res.data.roles
      console.log('设置角色列表:', allRoles.value)
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
  }
}

// 添加打开角色分配对话框的方法
const openRoleAssignDialog = async (row) => {
  currentMenuId.value = row.id
  selectedRoles.value = row.roles ? row.roles.map(role => role.id) : []
  console.log('当前选中的角色:', selectedRoles.value)
  await getAllRoles()
  console.log('可选��角色列表:', allRoles.value)
  roleDialogVisible.value = true
}

// 添加保存角色分配的方法
const handleRoleAssign = async () => {
  try {
    const oldRoles = menuList.value.find(menu => menu.id === currentMenuId.value)?.roles || []
    const oldRoleIds = oldRoles.map(role => role.id)
    
    // 找出需要添加和删除的角色
    const rolesToAdd = selectedRoles.value.filter(id => !oldRoleIds.includes(id))
    const rolesToDelete = oldRoleIds.filter(id => !selectedRoles.value.includes(id))
    
    console.log('当前菜单ID:', currentMenuId.value)
    console.log('选中的角色:', selectedRoles.value)
    console.log('原有角色:', oldRoleIds)
    console.log('需要添加的角色:', rolesToAdd)
    console.log('需要删除的角色:', rolesToDelete)
    
    // 执行添加操作
    for (const roleId of rolesToAdd) {
      try {
        console.log('发送添加角色请求:', {
          url: '/api/menu/role-menu/add',
          data: {
            roleId,
            menuId: currentMenuId.value
          }
        })
        const res = await addMenuRole(roleId, currentMenuId.value)
        console.log('添加角色响应:', res)
        if (res.code !== 200) {
          throw new Error(res.message || '添加角色失败')
        }
      } catch (error) {
        console.error('添加角色失败:', error)
        throw error
      }
    }
    
    // 执行删除操作
    for (const roleId of rolesToDelete) {
      try {
        console.log('发送删除角色请求:', {
          url: '/api/menu/role-menu/delete',
          data: {
            roleId,
            menuId: currentMenuId.value
          }
        })
        const res = await deleteMenuRole(roleId, currentMenuId.value)
        console.log('删除角色响应:', res)
        if (res.code !== 200) {
          throw new Error(res.message || '删除角色失败')
        }
      } catch (error) {
        console.error('删除角色失败:', error)
        throw error
      }
    }
    
    ElMessage.success('角色分配成功')
    roleDialogVisible.value = false
    getMenus() // 刷新菜单列表
  } catch (error) {
    console.error('角色分配失败:', error)
    ElMessage.error(error.message || '角色分配失败')
  }
}

onMounted(() => {
  getMenus()
  getAllRoles()
})
</script>

<style scoped>
.resource-manage {
  padding: 20px;
}

.search-form {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.operation-area {
  margin: 20px 0;
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

.mx-1 {
  margin: 0 4px;
}

.el-checkbox {
  margin-right: 15px;
  margin-bottom: 10px;
}

.el-icon-question {
  margin-left: 4px;
  font-size: 14px;
  color: #909399;
  cursor: help;
}

.el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 10px 0;
}
</style>