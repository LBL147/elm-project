<template>
  <div class="login-container">
    <div class="login-card">
      <div class="logo-section">
        <h1>饿了么</h1>
        <p class="subtitle">后台管理系统</p>
      </div>
      <el-form :model="loginForm" class="login-form">
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="请输入管理员用户名或商家编号"
            size="large"
            prefix-icon="User"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="loginForm.userType" size="large" class="login-type">
            <el-radio-button label="admin">管理员</el-radio-button>
            <el-radio-button label="business">商家</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="handleLogin"
            :loading="loading"
            size="large"
            class="login-button"
          >
            登录
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="register-link">
            还没有账号？
            <el-link type="primary" @click="goToRegister">立即注册</el-link>
          </div>
        </el-form-item>
        <el-form-item>
          <div class="tips">
            <p>管理员账号：admin / 123456</p>
            <p>商家账号：1 / 123</p>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { Auth } from '../utils/auth'
import { ElMessage } from 'element-plus'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const loginForm = ref({
      username: '',
      password: '',
      userType: 'admin'
    })
    const loading = ref(false)

    const handleLogin = async () => {
      if (!loginForm.value.username || !loginForm.value.password) {
        ElMessage.warning('请输入用户名和密码')
        return
      }

      loading.value = true
      try {
        const loginUrl = loginForm.value.userType === 'admin' ? '/api/admin/login' : '/api/business/login'
        const response = await axios.post(loginUrl, {
          username: loginForm.value.username,
          password: loginForm.value.password
        })
        const result = response.data || {}
        if (result.success) {
          const data = result.data || {}
          // 保存用户信息
          const userInfo = {
            username: data.adminName || data.businessName || loginForm.value.username,
            token: data.token || '',
            type: data.type || loginForm.value.userType,
            userId: data.adminId != null
              ? String(data.adminId)
              : data.businessId != null
                ? String(data.businessId)
                : ''
          }
          Auth.setUser(userInfo)
          
          ElMessage.success('登录成功')
          
          // 根据用户类型跳转
          const redirect = route.query.redirect
            || (userInfo.type === 'admin' ? '/admin' : userInfo.type === 'business' ? '/business' : '/')
          router.push(redirect)
        } else {
          ElMessage.error('登录失败：' + (result.message || '用户名或密码错误'))
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败，请检查网络连接')
      } finally {
        loading.value = false
      }
    }

    const goToRegister = () => {
      router.push('/register')
    }

    return {
      loginForm,
      loading,
      handleLogin,
      goToRegister
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
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
  font-size: 48px;
  color: #409eff;
  margin: 0 0 10px 0;
  font-weight: bold;
}

.subtitle {
  color: #666;
  font-size: 14px;
  margin: 0;
}

.login-form {
  margin-top: 20px;
}

.login-type {
  width: 100%;
  display: flex;
  justify-content: center;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: bold;
}

.register-link {
  text-align: center;
  width: 100%;
  font-size: 14px;
  color: #666;
}

.tips {
  width: 100%;
  text-align: center;
  font-size: 12px;
  color: #999;
  margin-top: 10px;
}

.tips p {
  margin: 5px 0;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .login-card {
    padding: 30px 20px;
  }

  .logo-section h1 {
    font-size: 36px;
  }
}
</style>
