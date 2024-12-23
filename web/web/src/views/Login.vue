<template>
  <div class="login-container">
    <el-card class="login-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" @keyup.enter="handleLogin">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="密码">
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <div class="captcha-container">
                <el-input 
                  v-model="loginForm.captcha" 
                  placeholder="请输入验证码"
                  style="width: 200px"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
                <img 
                  :src="captchaImage" 
                  @click="refreshCaptcha" 
                  class="captcha-img"
                  alt="验证码"
                />
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" @keyup.enter="handleRegister">
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" placeholder="用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="registerForm.email" placeholder="邮箱">
                <template #prefix>
                  <el-icon><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="city">
              <el-input v-model="registerForm.city" placeholder="城市">
                <template #prefix>
                  <el-icon><Location /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="密码">
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码">
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <div class="captcha-container">
                <el-input 
                  v-model="registerForm.captcha" 
                  placeholder="请输入验证码"
                  style="width: 200px"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
                <img 
                  :src="captchaImage" 
                  @click="refreshCaptcha" 
                  class="captcha-img"
                  alt="验证码"
                />
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleRegister" style="width: 100%">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { User, Lock, Key, Message, Location } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { login, register, generateCaptcha, validateCaptcha } from '@/api/auth'

const router = useRouter()
const activeTab = ref('login')
const loginFormRef = ref()
const registerFormRef = ref()
const captchaImage = ref('')

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: String(''),
  captcha: ''
})

// 注册表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  city: '',
  captcha: ''
})

// 验证码相关
const refreshCaptcha = async () => {
  try {
    const res = await generateCaptcha()
    // 将二进制数据转换为Base64 URL
    const blob = new Blob([res], { type: 'image/jpeg' })
    captchaImage.value = URL.createObjectURL(blob)
  } catch (error) {
    console.error('获取验证码失败:', error)
  }
}

// 组件卸载时清理URL对象
onUnmounted(() => {
  if (captchaImage.value) {
    URL.revokeObjectURL(captchaImage.value)
  }
})

// 验证验证码
const verifyCaptcha = async (captcha) => {
  try {
    const res = await validateCaptcha(captcha)
    // 直接返回后端的布尔值结果
    return res === true
  } catch (error) {
    console.error('验证码验证失败:', error)
    return false
  }
}

onMounted(() => {
  refreshCaptcha()
  document.addEventListener('keydown', handleKeyDown)
})

onUnmounted(() => {
  if (captchaImage.value) {
    URL.revokeObjectURL(captchaImage.value)
  }
  document.removeEventListener('keydown', handleKeyDown)
})

// 处理键盘事件
const handleKeyDown = (e) => {
  if (e.key === 'Enter') {
    if (activeTab.value === 'login') {
      handleLogin()
    } else {
      handleRegister()
    }
  }
}

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {

      const captchaValid = await verifyCaptcha(loginForm.captcha)
      if (!captchaValid) {
        ElMessage.error('验证码错误')
        refreshCaptcha()
        loginForm.captcha = ''
        return
      }

      try {
        const res = await login(loginForm.username, loginForm.password)
        if (res.code === 200) {
          console.log('登录成功，准备跳转...')
          localStorage.setItem('credentials', JSON.stringify({
            token: res.data
          }))
          ElMessage.success('登录成功')
          console.log('即将跳转到：/home')
          await router.push('/home')
          console.log('跳转完成')
        } else {
          ElMessage.error(res.message || '登录失败')
          refreshCaptcha()
        }
      } catch (error) {
        console.error('登录错误:', error)
        ElMessage.error('登录失败，请检查网络连接')
        refreshCaptcha()
      }
    }
  })
}

// 注册处理
const handleRegister = async () => {
  if (!registerFormRef.value) return
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      // 先验证验证码
      const captchaValid = await verifyCaptcha(registerForm.captcha)
      if (!captchaValid) {
        ElMessage.error('验证码错误')
        refreshCaptcha()
        registerForm.captcha = ''  // 清空验证码输入
        return
      }

      try {
        const res = await register(
          registerForm.username, 
          registerForm.password, 
          registerForm.email,
          registerForm.city
        )
        if (res.code === 200) {
          ElMessage.success('注册成功')
          activeTab.value = 'login'
          refreshCaptcha()
        } else {
          ElMessage.error(res.message || '注册失败')
          refreshCaptcha()
        }
      } catch (error) {
        console.error('注册错误:', error)
        ElMessage.error('注册失败，请检查网络连接')
        refreshCaptcha()
      }
    }
  })
}

// 表单验证规则
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

const registerRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  city: [
    { required: true, message: '请输入所在城市', trigger: 'blur' }
  ]
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('@/assets/background.png') no-repeat center center fixed;
  background-size: cover;
}

.login-card {
  width: 400px;
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.captcha-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.captcha-img {
  height: 40px;
  cursor: pointer;
}
</style> 