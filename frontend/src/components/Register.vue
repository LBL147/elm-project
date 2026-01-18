<template>
  <div class="register-container">
    <div class="register-card">
      <div class="logo-section">
        <h1>注册</h1>
        <p class="subtitle">创建商家账号</p>
      </div>
      <el-form :model="registerForm" class="register-form">
        <el-form-item>
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleRegister"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="handleRegister"
            :loading="loading"
            size="large"
            class="register-button"
          >
            注册
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="login-link">
            已有账号？
            <el-link type="primary" @click="goToLogin">立即登录</el-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'Register',
  setup() {
    const router = useRouter()
    const registerForm = ref({
      username: '',
      password: '',
      confirmPassword: ''
    })
    const loading = ref(false)

    const handleRegister = async () => {
      if (!registerForm.value.username || !registerForm.value.password) {
        ElMessage.warning('请输入用户名和密码')
        return
      }

      if (registerForm.value.password !== registerForm.value.confirmPassword) {
        ElMessage.warning('两次输入的密码不一致')
        return
      }

      if (registerForm.value.password.length < 6) {
        ElMessage.warning('密码长度至少6位')
        return
      }

      loading.value = true
      try {
        const response = await axios.post('/api/user/register', {
          username: registerForm.value.username,
          password: registerForm.value.password
        })
        if (response.data.success) {
          ElMessage.success('注册成功！')
          router.push('/login')
        } else {
          ElMessage.error('注册失败：' + (response.data.message || '未知错误'))
        }
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败，请检查网络连接')
      } finally {
        loading.value = false
      }
    }

    const goToLogin = () => {
      router.push('/login')
    }

    return {
      registerForm,
      loading,
      handleRegister,
      goToLogin
    }
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 400px;
  background: white;
  border-radius: 16px;
  padding: 40px 30px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo-section h1 {
  font-size: 36px;
  color: #409eff;
  margin: 0 0 10px 0;
  font-weight: bold;
}

.subtitle {
  color: #666;
  font-size: 14px;
  margin: 0;
}

.register-form {
  margin-top: 20px;
}

.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: bold;
}

.login-link {
  text-align: center;
  width: 100%;
  font-size: 14px;
  color: #666;
}

@media (max-width: 768px) {
  .register-card {
    padding: 30px 20px;
  }

  .logo-section h1 {
    font-size: 28px;
  }
}
</style>

