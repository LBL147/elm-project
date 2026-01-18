<template>
  <div class="admin-dashboard">
    <el-container>
      <el-header class="mobile-header">
        <div class="header-content">
          <h2>管理员后台</h2>
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><User /></el-icon>
              {{ username }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="mobile-main">
        <el-card class="function-card" @click="goToBusinessList">
          <div class="card-content">
            <el-icon class="card-icon" size="40"><Shop /></el-icon>
            <h3>商家管理</h3>
            <p>查看和管理所有商家</p>
          </div>
        </el-card>
        <el-card class="function-card" @click="openAddBusinessDialog">
          <div class="card-content">
            <el-icon class="card-icon" size="40"><Plus /></el-icon>
            <h3>新建商家</h3>
            <p>添加新的商家账户</p>
          </div>
        </el-card>
      </el-main>
      <el-dialog v-model="addDialogVisible" title="新建商家" width="480px">
        <el-form :model="addForm" label-width="100px">
          <el-form-item label="商家名称">
            <el-input v-model="addForm.businessName" placeholder="请输入商家名称" />
          </el-form-item>
          <el-form-item label="商家地址">
            <el-input v-model="addForm.businessAddress" placeholder="请输入商家地址" />
          </el-form-item>
          <el-form-item label="商家介绍">
            <el-input v-model="addForm.businessExplain" placeholder="请输入商家介绍" />
          </el-form-item>
          <el-form-item label="起送费">
            <el-input-number v-model="addForm.starPrice" :min="0" :precision="2" />
          </el-form-item>
          <el-form-item label="配送费">
            <el-input-number v-model="addForm.deliveryPrice" :min="0" :precision="2" />
          </el-form-item>
          <el-form-item label="初始密码">
            <el-input v-model="addForm.password" placeholder="留空则默认 123" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="saveBusiness">保存</el-button>
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
  name: 'AdminDashboard',
  setup() {
    const router = useRouter()
    const username = ref(Auth.getUser().username)
    const addDialogVisible = ref(false)
    const saving = ref(false)
    const addForm = ref({
      businessName: '',
      businessAddress: '',
      businessExplain: '',
      starPrice: 0,
      deliveryPrice: 0,
      password: ''
    })

    const goToBusinessList = () => {
      router.push('/merchant-manager')
    }

    const openAddBusinessDialog = () => {
      addForm.value = {
        businessName: '',
        businessAddress: '',
        businessExplain: '',
        starPrice: 0,
        deliveryPrice: 0,
        password: ''
      }
      addDialogVisible.value = true
    }

    const saveBusiness = async () => {
      if (!addForm.value.businessName) {
        ElMessage.warning('请输入商家名称')
        return
      }

      saving.value = true
      try {
        const payload = {
          businessName: addForm.value.businessName,
          businessAddress: addForm.value.businessAddress,
          businessExplain: addForm.value.businessExplain,
          starPrice: addForm.value.starPrice,
          deliveryPrice: addForm.value.deliveryPrice
        }
        if (addForm.value.password) {
          payload.password = addForm.value.password
        }
        const response = await axios.post('/api/admin/businesses', payload)
        const result = response.data || {}
        if (result.success) {
          const data = result.data || {}
          ElMessage.success(`新建成功，商家编号：${data.businessId || ''}，初始密码：${data.defaultPassword || '123'}`)
          addDialogVisible.value = false
          router.push('/merchant-manager')
        } else {
          ElMessage.error(result.message || '新建商家失败')
        }
      } catch (error) {
        console.error('新建商家失败:', error)
        ElMessage.error('新建商家失败，请检查网络连接')
      } finally {
        saving.value = false
      }
    }

    const handleCommand = (command) => {
      if (command === 'logout') {
        Auth.clearUser()
        ElMessage.success('已退出登录')
        router.push('/login')
      }
    }

    onMounted(() => {
      if (!Auth.isAdmin()) {
        router.push('/')
      }
    })

    return {
      username,
      goToBusinessList,
      openAddBusinessDialog,
      handleCommand,
      addDialogVisible,
      addForm,
      saveBusiness,
      saving
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.mobile-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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


