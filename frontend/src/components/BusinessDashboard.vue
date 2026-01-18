<template>
  <div class="business-dashboard">
    <el-container>
      <el-header class="mobile-header">
        <div class="header-content">
          <h2>商家后台</h2>
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><User /></el-icon>
              {{ username }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="info">商家信息</el-dropdown-item>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="mobile-main">
        <el-card class="function-card" @click="goToBusinessInfo">
          <div class="card-content">
            <el-icon class="card-icon" size="40"><InfoFilled /></el-icon>
            <div>
              <h3>查看商家信息</h3>
              <p>查看和编辑商家详细信息</p>
            </div>
          </div>
        </el-card>
        <el-card class="function-card" @click="goToFoodManagement">
          <div class="card-content">
            <el-icon class="card-icon" size="40"><Food /></el-icon>
            <div>
              <h3>食品管理</h3>
              <p>管理商家的食品列表</p>
            </div>
          </div>
        </el-card>
        <el-card class="function-card" @click="goToChangePassword">
          <div class="card-content">
            <el-icon class="card-icon" size="40"><Lock /></el-icon>
            <div>
              <h3>修改密码</h3>
              <p>更改登录密码</p>
            </div>
          </div>
        </el-card>
      </el-main>
      <el-dialog v-model="passwordDialogVisible" title="修改密码" width="420px">
        <el-form :model="passwordForm" label-width="100px">
          <el-form-item label="旧密码">
            <el-input v-model="passwordForm.oldPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="passwordForm.newPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="passwordDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="submitPasswordChange">确定</el-button>
        </template>
      </el-dialog>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Auth } from '../utils/auth'
import { ElMessage } from 'element-plus'
import axios from 'axios'

export default {
  name: 'BusinessDashboard',
  setup() {
    const router = useRouter()
    const username = ref(Auth.getUser().username)
    const businessId = ref(Auth.getUser().userId)
    const passwordDialogVisible = ref(false)
    const saving = ref(false)
    const passwordForm = ref({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    const goToBusinessInfo = () => {
      if (businessId.value) {
        router.push(`/business-info/${businessId.value}`)
      } else {
        ElMessage.warning('商家信息不存在')
      }
    }

    const goToFoodManagement = () => {
      if (businessId.value) {
        router.push('/food-management')
      } else {
        ElMessage.warning('商家信息不存在')
      }
    }

    const goToChangePassword = () => {
      passwordForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      passwordDialogVisible.value = true
    }

    const submitPasswordChange = async () => {
      if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) {
        ElMessage.warning('请输入旧密码和新密码')
        return
      }
      if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        ElMessage.warning('新密码与确认密码不一致')
        return
      }
      if (!businessId.value) {
        ElMessage.warning('商家信息不存在')
        return
      }

      saving.value = true
      try {
        const response = await axios.post('/api/business/updatePassword', {
          businessId: Number(businessId.value),
          oldPassword: passwordForm.value.oldPassword,
          newPassword: passwordForm.value.newPassword,
          confirmPassword: passwordForm.value.confirmPassword
        })
        const result = response.data || {}
        if (result.success) {
          ElMessage.success(result.message || '修改密码成功')
          passwordDialogVisible.value = false
        } else {
          ElMessage.error(result.message || '修改密码失败')
        }
      } catch (error) {
        console.error('修改密码失败:', error)
        ElMessage.error('修改密码失败，请检查网络连接')
      } finally {
        saving.value = false
      }
    }

    const handleCommand = (command) => {
      if (command === 'logout') {
        Auth.clearUser()
        ElMessage.success('已退出登录')
        router.push('/login')
      } else if (command === 'info') {
        goToBusinessInfo()
      } else if (command === 'password') {
        goToChangePassword()
      }
    }

    onMounted(() => {
      if (!Auth.isBusiness()) {
        router.push('/')
      }
    })

    return {
      username,
      goToBusinessInfo,
      goToFoodManagement,
      goToChangePassword,
      handleCommand,
      passwordDialogVisible,
      passwordForm,
      submitPasswordChange,
      saving
    }
  }
}
</script>

<style scoped>
.business-dashboard {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.mobile-header {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  padding: 15px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h2 {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
}

.user-info {
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.mobile-main {
  padding: 20px;
}

.function-card {
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 12px;
}

.function-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.card-icon {
  color: #409eff;
}

.card-content h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
  color: #333;
}

.card-content p {
  margin: 0;
  font-size: 14px;
  color: #666;
}
</style>


