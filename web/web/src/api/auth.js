import request from './request'

// 获取验证码
export function generateCaptcha() {
  return request({
    url: '/api/captcha/generate',
    method: 'get',
    responseType: 'blob',
    withCredentials: true
  })
}

// 验证验证码
export function validateCaptcha(captcha) {
  const params = new URLSearchParams()
  params.append('captcha', captcha)

  return request({
    url: '/api/captcha/validate',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    data: params,
    withCredentials: true
  })
}

export function login(username, password) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    params: {
      username,
      password: String(password)
    },
    withCredentials: true
  })
}

export function register(username, password, email, city) {
  return request({
    url: '/api/user/register',
    method: 'post',
    params: {
      username,
      password,
      email,
      city
    }
  })
}

// 获取用户名
export function getUserName() {
  return request({
    url: '/api/user/name',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function getUserInfo() {
  return request({
    url: '/api/user/info',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function test5() {
    return request({
        url: '/api/test/test5',
        method: 'get',
      headers: {
        'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
      }
    })
}

// 添加商品相关的API方法
export function getProductList(params) {
  return request({
    url: '/api/product/list',
    method: 'get',
    params: params,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function searchProducts(params) {
  return request({
    url: '/api/product/search',
    method: 'get',
    params: params,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function updateProduct(product) {
  return request({
    url: '/api/product/update',
    method: 'post',
    data: product,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function offShelfProduct(id) {
  return request({
    url: '/api/product/delete',
    method: 'post',
    params: { id },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 获取商品图片
export function getProductImages(productId) {
  return request({
    url: '/api/product/images',
    method: 'get',
    params: { product_id: productId },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 品牌管理相关的API方法
export function getBrandList(params) {
  return request({
    url: '/api/brand/list',
    method: 'get',
    params: params,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function searchBrands(params) {
  return request({
    url: '/api/brand/search',
    method: 'get',
    params: params,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function getBrandDetail(id) {
  return request({
    url: '/api/brand/detail',
    method: 'get',
    params: { id },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function addBrand(brand) {
  return request({
    url: '/api/brand/add',
    method: 'post',
    data: brand,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function updateBrand(brand) {
  return request({
    url: '/api/brand/update',
    method: 'post',
    data: brand,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function deleteBrand(id) {
  return request({
    url: '/api/brand/delete',
    method: 'post',
    params: { id },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 分类相关接口
export const getCategoryList = (page = 1, pageSize = 10, sortBy = '', sortOrder = '') => {
  return request({
    url: '/api/categories/page',
    method: 'get',
    params: { 
      page, 
      pageSize,
      sortBy,
      sortOrder 
    },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export const createCategory = (data) => {
  return request({
    url: '/api/categories',
    method: 'post',
    data,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  }).then(response => {
    console.log('创建分类返回值:', response)
    return response
  })
}

export const updateCategory = (id, data) => {
  return request({
    url: `/api/categories/${id}`,
    method: 'put',
    data,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  }).then(response => {
    console.log('更新分类返回值:', response)
    return response
  })
}

export const deleteCategory = (id) => {
  return request({
    url: `/api/categories/${id}`,
    method: 'delete',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  }).then(response => {
    console.log('删除分类返回值:', response)
    return response
  })
}

export const getCategoryChildren = (parentId) => {
  return request({
    url: `/api/categories/children/${parentId}`,
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  }).then(response => {
    console.log('获取子分类返回值:', response)
    return response
  })
}

// 获取分类总数
export const getCategoryTotal = () => {
  return request({
    url: '/api/categories/total',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  }).then(response => {
    return response
  })
}

// 类型相关接口
export const getTypeList = (page = 1, pageSize = 10, sortBy = '', sortOrder = '') => {
  return request({
    url: '/api/types/page',
    method: 'get',
    params: { 
      page, 
      pageSize,
      sortBy,
      sortOrder 
    },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export const createType = (data) => {
  return request({
    url: '/api/types',
    method: 'post',
    data,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export const updateType = (id, data) => {
  return request({
    url: `/api/types/${id}`,
    method: 'put',
    data,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export const deleteType = (id) => {
  return request({
    url: `/api/types/${id}`,
    method: 'delete',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export const getTypeTotal = () => {
  return request({
    url: '/api/types/total',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 更新用户信息
export const updateUser = (data) => {
  return request({
    url: '/api/user/update',
    method: 'post',
    data
  })
}

// 更新用户状态
export const updateUserStatus = (userId, status) => {
  return request({
    url: '/api/user/updateStatus',
    method: 'post',
    data: {
      userId,
      status
    }
  })
}

// 获取用户列表（分页）
export const getUserList = (page = 1, pageSize = 10) => {
  return request({
    url: '/api/user/page',
    method: 'get',
    params: {
      page,
      pageSize
    }
  })
}

// 在文件末尾添加以下角色管理相关的接口

// 获取所有角色
export const getRoleList = () => {
  return request({
    url: '/api/role/all',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 添加角色
export const addRole = (roleData) => {
  return request({
    url: '/api/role/add',
    method: 'post',
    data: roleData,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 更新角色
export const updateRole = (roleData) => {
  return request({
    url: '/api/role/update',
    method: 'put',
    data: roleData,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 删除角色
export const deleteRole = (id) => {
  return request({
    url: `/api/role/${id}`,
    method: 'delete',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 获取所有菜单（不限制parent_id）
export const getAllMenus = () => {
  return request({
    url: '/api/menu/all',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 添加菜单
export const addMenu = (menuData) => {
  return request({
    url: '/api/menu/add',
    method: 'post',
    data: menuData,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 更新菜单
export const updateMenu = (menuData) => {
  return request({
    url: '/api/menu/update',
    method: 'put',
    data: menuData,
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 删除菜单
export const deleteMenu = (id) => {
  return request({
    url: `/api/menu/${id}`,
    method: 'delete',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 更新菜单状态
export const updateMenuStatus = (id, status) => {
  return request({
    url: '/api/menu/status',
    method: 'put',
    data: { id, status },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 修改上传头像到OSS的方法
export function uploadOssFile(data) {
  return request({
    url: '/api/fileoss/uploadOssFile',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  }).then(response => {
    console.log('OSS upload response:', response)
    return response
  })
}

// 修改更新用户头像的方法
export function updateUserAvatar(avatar) {
  return request({
    url: '/api/user/updateAvatar',
    method: 'post',
    params: { avatar },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 获取所有分类
export function getAllCategories() {
  return request({
    url: '/api/types',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 获取所有品牌
export function getAllBrands() {
  return request({
    url: '/api/brand',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 添加商品
export function addProduct(data) {
  return request({
    url: '/api/product/add',
    method: 'post',
    params: data,  // 使用params因为后端使用@RequestParam
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 添加商品图片
export function addProductImage(product_id, image_url, is_main) {
  return request({
    url: '/api/product/addImage',
    method: 'post',
    params: {
      product_id,
      image_url,
      is_main
    },
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

// 添加检查用户状态的方法
export function checkUserStatus(username) {
  return request({
    url: '/api/user/status',
    method: 'get',
    params: { username }
  })
}