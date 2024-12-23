<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Editor from '@tinymce/tinymce-vue'
import { Plus } from '@element-plus/icons-vue'  // 添加图标导入

// 步骤条相关
const active = ref(0)
const steps = [
  { title: '填写商品信息', icon: 'Document' },
  { title: '填写商品属性', icon: 'List' }
]

// 商品基本信息表单
const productForm = reactive({
  category_id: '',
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
  album: [], // 商品相册
  detail: '' // 商品详情
})

// 商品属性表单
const attributeForm = reactive({
  attribute_type: '',
  specs: []  // 用于存储规格数据
})

// 表单校验规则
const rules = {
  category_id: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  brand_id: [{ required: true, message: '请选择商品品牌', trigger: 'change' }],
  price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入商品库存', trigger: 'blur' }]
}

// 图片上传相关配置
const uploadConfig = {
  action: '#',  // 临时设置，实际使用时替换为真实接口
  multiple: true,
  'show-file-list': true,
  'list-type': "picture-card",
  'auto-upload': false  // 禁用自动上传
}

// 图片预览相关
const dialogImageUrl = ref('')
const dialogVisible = ref(false)

// 处理图片预览
const handlePreview = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url
  dialogVisible.value = true
}

// 处理图片移除
const handleRemove = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles)
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
  branding: false,  // 去除 TinyMCE 品牌标识
  statusbar: true,  // 显示状态栏
  toolbar_sticky: true,
  
  // 精简插件列表，只保留基本功能
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

// 上一步
const prevStep = () => {
  if (active.value > 0) {
    active.value--
  }
}

// 添加商品属性
const addAttribute = () => {
  attributeForm.attributes.push({
    name: '',
    value: ''
  })
}

// 删除商品属性
const removeAttribute = (index) => {
  attributeForm.attributes.splice(index, 1)
}

// 提交表单
const submitForm = () => {
  // TODO: 实现提交逻辑
  ElMessage.success('提交成功')
}

// 添加规格
const addSpec = () => {
  attributeForm.specs.push({
    price: 0,
    promotion_price: 0,
    stock: 0,
    warning_stock: 0,
    sku: ''
  })
}

// 删除规格
const removeSpec = (index) => {
  attributeForm.specs.splice(index, 1)
}

// 批量设置价格
const batchSetPrice = () => {
  ElMessageBox.prompt('请输入价格', '批量设置价格', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d+(\.\d{0,2})?$/,
    inputErrorMessage: '请输入正确的价格格式'
  }).then(({ value }) => {
    attributeForm.specs.forEach(spec => {
      spec.price = Number(value)
    })
    ElMessage.success('批量设置价格成功')
  }).catch(() => {})
}

// 批量设置库存
const batchSetStock = () => {
  ElMessageBox.prompt('请输入库存数量', '批量设置库存', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d+$/,
    inputErrorMessage: '请输入正确的库存数量'
  }).then(({ value }) => {
    attributeForm.specs.forEach(spec => {
      spec.stock = Number(value)
    })
    ElMessage.success('批量设置库存成功')
  }).catch(() => {})
}
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
        <el-form-item label="商品分类" prop="category_id">
          <el-select v-model="productForm.category_id" placeholder="请选择商品分类">
            <!-- TODO: 添加分类选项 -->
          </el-select>
        </el-form-item>

        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productForm.name" />
        </el-form-item>

        <el-form-item label="副标题" prop="subtitle">
          <el-input v-model="productForm.subtitle" />
        </el-form-item>

        <el-form-item label="商品品牌" prop="brand_id">
          <el-select v-model="productForm.brand_id" placeholder="请选择商品品牌">
            <!-- TODO: 添加品牌选项 -->
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
      </el-form>
    </div>

    <!-- 第二步：商品属性、相册和详情 -->
    <div class="form-container" v-if="active === 1">
      <el-form :model="attributeForm" label-width="120px">
        <!-- 商品规格部分 -->
        <el-form-item label="属性类型">
          <el-select v-model="attributeForm.attribute_type" placeholder="请选择属性类型">
            <el-option label="规格" value="specification" />
            <el-option label="参数" value="parameter" />
          </el-select>
        </el-form-item>

        <!-- 商品规格表格 -->
        <div class="spec-table">
          <el-table :data="attributeForm.specs" border style="width: 100%">
            <el-table-column label="销售价格" width="180">
              <template #default="scope">
                <el-input-number 
                  v-model="scope.row.price" 
                  :precision="2" 
                  :step="0.1"
                  placeholder="销售价格"
                />
              </template>
            </el-table-column>
            <el-table-column label="促销价格" width="180">
              <template #default="scope">
                <el-input-number 
                  v-model="scope.row.promotion_price" 
                  :precision="2" 
                  :step="0.1"
                  placeholder="促销价格"
                />
              </template>
            </el-table-column>
            <el-table-column label="商品库存" width="180">
              <template #default="scope">
                <el-input-number 
                  v-model="scope.row.stock" 
                  :min="0"
                  placeholder="商品库存"
                />
              </template>
            </el-table-column>
            <el-table-column label="库存预警值" width="180">
              <template #default="scope">
                <el-input-number 
                  v-model="scope.row.warning_stock" 
                  :min="0"
                  placeholder="库存预警值"
                />
              </template>
            </el-table-column>
            <el-table-column label="SKU编号" width="180">
              <template #default="scope">
                <el-input 
                  v-model="scope.row.sku" 
                  placeholder="SKU编号"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="removeSpec(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 操作按钮组 -->
          <div class="spec-operations">
            <el-button type="primary" @click="addSpec">添加规格</el-button>
            <el-button @click="batchSetPrice">批量设置价格</el-button>
            <el-button @click="batchSetStock">批量设置库存</el-button>
          </div>
        </div>

        <!-- 商品相册 -->
        <el-form-item label="商品相册">
          <el-upload
            v-bind="uploadConfig"
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

.attributes-list {
  margin: 20px 0;
}

.attribute-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.remove-btn {
  margin-left: 10px;
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

.spec-table {
  margin: 20px auto;
  max-width: 1200px;
}

.spec-operations {
  margin: 20px auto;
  display: flex;
  justify-content: center;
  gap: 10px;
}

:deep(.el-input-number) {
  width: 100%;
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

/* 激活状态 */
.step-item.active .step-number {
  background-color: #409eff;
}

.step-item.active .step-title {
  color: #409eff;
}

/* 完成状态 */
.step-item.completed .step-number {
  background-color: #67c23a;
}

.step-line.active {
  background-color: #67c23a;
}
</style>