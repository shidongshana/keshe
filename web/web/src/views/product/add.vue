<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Editor from '@tinymce/tinymce-vue'
import { Plus } from '@element-plus/icons-vue'  // 添加图标导入
import { getAllCategories, getAllBrands, uploadOssFile, addProduct } from '@/api/auth'

// 步骤条相关
const active = ref(0)
const steps = [
  { title: '填写商品信息', icon: 'Document' },
  { title: '填写商品属性', icon: 'List' }
]

// 商品基本信息表单
const productForm = reactive({
  category_id: '',
  type_id: '',
  name: '',
  subtitle: '',
  brand_id: '',
  description: '',
  product_sn: '',
  price: 0,
  market_price: 0,
  stock: 0,
  unit: '',
  weight: 0,
  sort: 0,
  status: 1,  // 添加状态字段，默认为1
  album: [], // 商品相册
  image_url: '', // 添加主图URL字段
  detail: '' // 商品详情
})

// 商品属性表单可以删除
const attributeForm = reactive({
  // 不再需要属性相关的字段
})

// 表单校验规则
const rules = {
  type_id: [{ required: true, message: '请选择商品类型', trigger: 'change' }],
  category_id: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  brand_id: [{ required: true, message: '请选择商品品牌', trigger: 'change' }],
  price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入商品库存', trigger: 'blur' }]
}

// 处理自定义上传
const handleCustomUpload = async (options) => {
  try {
    const formData = new FormData()
    formData.append('file', options.file)
    
    const response = await uploadOssFile(formData)
    if (response.code === 200) {
      // 上传成功，将返回的URL添加到相册数组
      productForm.album.push(response.data)
      // 设置第一张图片为主图
      if (!productForm.image_url) {
        productForm.image_url = response.data
      }
      ElMessage.success('图片上传成功')
      options.onSuccess(response)
    } else {
      ElMessage.error('图片上传失败')
      options.onError('上传失败')
    }
  } catch (error) {
    console.error('图片上传错误:', error)
    ElMessage.error('图片上传出错')
    options.onError('上传出错')
  }
}

// 图片上传相关配置
const uploadConfig = {
  action: '',  // 使用空字符串，因为我们使用自定义上传
  multiple: true,
  'show-file-list': true,
  'list-type': "picture-card",
  'auto-upload': true,  // 改为true，允许自动上传
  'http-request': handleCustomUpload  // 使用自定义上传方法
}

// 在图片上传前验证
const beforeUpload = (file) => {
  const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPGOrPNG) {
    ElMessage.error('只能上传 JPG 或 PNG 格式的图片!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 图片预览相关
const dialogImageUrl = ref('')
const dialogVisible = ref(false)

// 处理图片预览
const handlePreview = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url
  dialogVisible.value = true
}

// 修改图片移除处理
const handleRemove = (uploadFile) => {
  const index = productForm.album.indexOf(uploadFile.url)
  if (index > -1) {
    productForm.album.splice(index, 1)
    // 如果删除的是主图，则设置新的主图
    if (uploadFile.url === productForm.image_url) {
      productForm.image_url = productForm.album[0] || ''
    }
  }
}

// 处理超出限制
const handleExceed = (files, uploadFiles) => {
  ElMessage.warning(
    `最多选择 5 张图片，本次选择了 ${files.length} 张，共选择了 ${files.length + uploadFiles.length} 张`
  )
}

// 富文本编辑器配置
const editorConfig = {
  // 基础配置
  language: 'zh_CN',
  language_url: '/tinymce/langs/zh_CN.js',
  base_url: '/tinymce',
  height: 500,
  resize: true,  // 允许调整高度
  branding: false,  // ��除 TinyMCE 品牌标识
  statusbar: true,  // 显示状态栏
  toolbar_sticky: true,
  
  // 精����插件列表，只保留基本功能
  plugins: [
    'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
    'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
    'insertdatetime', 'media', 'table', 'help', 'wordcount', 'emoticons'
  ],
  
  // 简化工具栏配置
  toolbar: [
    'undo redo | formatselect | fontselect fontsizeselect |',
    'bold italic underline strikethrough | forecolor backcolor | alignleft aligncenter alignright alignjustify |',
    'bullist numlist | outdent indent | link image media table |',
    'removeformat | preview fullscreen'
  ],
  
  // 菜单栏配置
  menubar: 'file edit view insert format tools table help',
  menu: {
    file: { title: '文件', items: 'newdocument preview' },
    edit: { title: '编辑', items: 'undo redo | cut copy paste | selectall searchreplace' },
    view: { title: '查看', items: 'code | visualaid visualblocks | preview fullscreen' },
    insert: { title: '插入', items: 'image link media table | charmap emoticons' },
    format: { title: '格式', items: 'bold italic underline strikethrough | formats | removeformat' },
    tools: { title: '工具', items: 'code wordcount' },
    table: { title: '表格', items: 'inserttable tableprops deletetable | cell row column' }
  },
  
  // 字体配置
  font_formats: `
    微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;
    宋体=simsun,serif;
    黑体=SimHei,sans-serif;
    Arial=arial,helvetica,sans-serif;
    Times New Roman=times new roman,times;
  `,
  
  // 字号配置
  fontsize_formats: '12px 14px 16px 18px 24px 36px',
  
  // 内容样式
  content_style: `
    body { 
      font-family: Microsoft YaHei, sans-serif;
      font-size: 14px;
      line-height: 1.6;
      color: #333;
      max-width: 100%;
    }
    img { max-width: 100%; height: auto; }
    table { width: 100%; border-collapse: collapse; }
    table td, table th { border: 1px solid #ddd; padding: 8px; }
  `
}

// 下一步
const nextStep = () => {
  if (active.value < steps.length - 1) {
    active.value++
  }
}

// 上��步
const prevStep = () => {
  if (active.value > 0) {
    active.value--
  }
}

// 修改提交表单方法
const submitForm = async () => {
  const formData = {
    category_id: productForm.category_id,
    type_id: productForm.type_id,
    name: productForm.name,
    subtitle: productForm.subtitle,
    brand_id: productForm.brand_id,
    description: productForm.detail,
    product_sn: productForm.product_sn,
    price: productForm.price,
    market_price: productForm.market_price,
    stock: productForm.stock,
    unit: productForm.unit,
    weight: productForm.weight,
    sort: productForm.sort,
    status: productForm.status,
    image_url: productForm.image_url,
    detail: productForm.detail
  }

  try {
    const response = await addProduct(formData)
    if (response.code === 200) {
      ElMessage.success('商品添加成功')
      // 可以在这里添加成功后的处理，比如清空表单或跳转
    } else {
      ElMessage.error(response.message || '添加失败')
    }
  } catch (error) {
    console.error('添加商品失败:', error)
    ElMessage.error('添加失败，请重试')
  }
}

// 添加类型列表数据
const typeOptions = ref([])

// 获取类型数据
const fetchTypes = async () => {
  try {
    const response = await getAllCategories() // 这里使用的是types接口
    if (response.code === 200) {
      typeOptions.value = response.data.map(item => ({
        value: item.id,
        label: item.name,
        category_id: item.category_id // 保存category_id用于联动
      }))
    }
  } catch (error) {
    console.error('获取类型列表失败:', error)
  }
}

// 监听类型选择变化
const handleTypeChange = (typeId) => {
  // 找到选中的类型
  const selectedType = typeOptions.value.find(type => type.value === typeId)
  if (selectedType) {
    // 自动设置对应的分类ID
    productForm.category_id = selectedType.category_id
  }
}

// 添加品牌列表数据
const brandOptions = ref([])

// 获取品牌数据
const fetchBrands = async () => {
  try {
    const response = await getAllBrands()
    if (response.code === 200) {
      brandOptions.value = response.data.map(item => ({
        value: item.id,
        label: item.name
      }))
    }
  } catch (error) {
    console.error('获取品牌列表失败:', error)
  }
}

// 监听富文本编辑器内容变化
const handleEditorChange = (content) => {
  productForm.description = content // 将富文本内容同步到description
}

// 在组件挂载时获取分类和品牌数
onMounted(() => {
  fetchTypes()
  fetchBrands()
})
</script>

<template>
  <div class="add-product-container">
    <!-- 步骤条 -->
    <div class="steps-container">
      <div class="step-item" :class="{ active: active >= 0, completed: active > 0 }">
        <div class="step-number">1</div>
        <div class="step-title">填写商品信息</div>
      </div>
      <div class="step-line" :class="{ active: active > 0 }"></div>
      <div class="step-item" :class="{ active: active >= 1 }">
        <div class="step-number">2</div>
        <div class="step-title">填写商品属性</div>
      </div>
    </div>

    <!-- 第一步：基本信息表单 -->
    <div class="form-container" v-if="active === 0">
      <el-form 
        :model="productForm"
        :rules="rules"
        label-width="120px"
        class="product-form"
      >
        <el-form-item label="商品类型" prop="type_id">
          <el-select 
            v-model="productForm.type_id" 
            placeholder="请选择商品类型"
            @change="handleTypeChange"
          >
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="商品分类" prop="category_id">
          <el-input v-model="productForm.category_id" disabled />
        </el-form-item>

        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productForm.name" />
        </el-form-item>

        <el-form-item label="副标题" prop="subtitle">
          <el-input v-model="productForm.subtitle" />
        </el-form-item>

        <el-form-item label="商品品牌" prop="brand_id">
          <el-select v-model="productForm.brand_id" placeholder="请选择商品品牌">
            <el-option
              v-for="item in brandOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="商品货号" prop="product_sn">
          <el-input v-model="productForm.product_sn" />
        </el-form-item>

        <el-form-item label="商品售价" prop="price">
          <el-input-number v-model="productForm.price" :precision="2" :step="0.1" />
        </el-form-item>

        <el-form-item label="市场价" prop="market_price">
          <el-input-number v-model="productForm.market_price" :precision="2" :step="0.1" />
        </el-form-item>

        <el-form-item label="商品库存" prop="stock">
          <el-input-number v-model="productForm.stock" :min="0" />
        </el-form-item>

        <el-form-item label="计量单位" prop="unit">
          <el-input v-model="productForm.unit" />
        </el-form-item>

        <el-form-item label="商品重量" prop="weight">
          <el-input-number v-model="productForm.weight" :precision="2" :step="0.1" />
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="productForm.sort" :min="0" />
        </el-form-item>

        <el-form-item label="商品状态" prop="status">
          <el-switch
            v-model="productForm.status"
            :active-value="1"
            :inactive-value="0"
            active-text="上架"
            inactive-text="下架"
          />
        </el-form-item>
      </el-form>
    </div>

    <!-- 第二步：修改为只包含图片上传和富文本编辑器 -->
    <div class="form-container" v-if="active === 1">
      <el-form label-width="120px">
        <!-- 商品相册 -->
        <el-form-item label="商品相册">
          <el-upload
            v-bind="uploadConfig"
            :before-upload="beforeUpload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
            :limit="5"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="el-upload__tip">
                只能上传 jpg/png 文件，且不超过 2MB
              </div>
            </template>
          </el-upload>
          <!-- 图片预览对话框 -->
          <el-dialog v-model="dialogVisible" title="预览">
            <img w-full :src="dialogImageUrl" alt="Preview Image" style="max-width: 100%;" />
          </el-dialog>
        </el-form-item>

        <!-- 商品详情 -->
        <el-form-item label="商品详情">
          <Editor
            v-model="productForm.detail"
            :init="editorConfig"
            :disabled="false"
            api-key="no-api-key"
            tinymce-script-src="/tinymce/tinymce.min.js"
            @change="handleEditorChange"
          />
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="operation-container">
      <el-button @click="prevStep" v-if="active > 0">上一步</el-button>
      <el-button type="primary" @click="nextStep" v-if="active < steps.length - 1">下一步</el-button>
      <el-button type="primary" @click="submitForm" v-if="active === steps.length - 1">提交</el-button>
    </div>
  </div>
</template>

<style scoped>
.add-product-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.form-container {
  margin: 30px auto;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  max-width: 1200px;
}

.product-form {
  max-width: 800px;
  margin: 0 auto;
}

.operation-container {
  margin: 30px auto;
  display: flex;
  justify-content: center;
  gap: 20px;
  max-width: 1200px;
}

:deep(.el-form-item__content) {
  flex-wrap: nowrap;
}

/* 添加编辑器相关样式 */
:deep(.tox-tinymce) {
  border: 1px solid #DCDFE6;
  border-radius: 4px;
}

:deep(.tox-statusbar) {
  border-top: 1px solid #DCDFE6;
}

/* 添加图片上传相关样式 */
:deep(.el-upload--picture-card) {
  width: 150px;
  height: 150px;
  line-height: 150px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 150px;
  height: 150px;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  margin-top: 8px;
}

/* 预览对话框样式 */
:deep(.el-dialog__body) {
  text-align: center;
  padding: 20px;
}

/* 步骤条样式 */
.steps-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.step-number {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #dcdfe6;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 8px;
  transition: all 0.3s;
}

.step-title {
  font-size: 14px;
  color: #909399;
  transition: all 0.3s;
}

.step-line {
  width: 100px;
  height: 2px;
  background-color: #dcdfe6;
  margin: 0 20px;
  margin-bottom: 30px;
  transition: all 0.3s;
}

.step-item.active .step-number {
  background-color: #409eff;
}

.step-item.active .step-title {
  color: #409eff;
}

.step-item.completed .step-number {
  background-color: #67c23a;
}

.step-line.active {
  background-color: #67c23a;
}
</style>