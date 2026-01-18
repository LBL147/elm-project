<template>
  <div class="index-container">
    <el-container>
      <el-header class="mobile-header">
        <div class="header-content">
          <h1>饿了么</h1>
          <div class="header-actions">
            <el-button
              v-if="!isLoggedIn"
              @click="goToLogin"
              size="small"
              type="primary"
              plain
            >
              登录
            </el-button>
            <el-dropdown v-if="isLoggedIn" @command="handleCommand">
              <span class="user-info">
                <el-icon><User /></el-icon>
                {{ username }}
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="isAdmin" command="admin">管理员后台</el-dropdown-item>
                  <el-dropdown-item v-if="isBusiness" command="business">商家后台</el-dropdown-item>
                  <el-dropdown-item command="orderList">我的订单</el-dropdown-item>
                  <el-dropdown-item command="address">收货地址</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main class="mobile-main">
        <div class="search-section">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商家..."
            class="search-input"
            size="large"
            @keyup.enter="searchBusiness"
          >
            <template #append>
              <el-button @click="searchBusiness" icon="Search">搜索</el-button>
            </template>
          </el-input>
        </div>
        <div class="business-list">
          <div
            v-for="business in businessList"
            :key="business.businessId"
            class="business-card"
            @click="goToBusinessInfo(business.businessId)"
          >
            <div class="business-info">
              <h3>{{ business.businessName }}</h3>
              <p class="address">{{ business.businessAddress || '暂无地址' }}</p>
              <p class="explain">{{ business.businessExplain || '暂无介绍' }}</p>
              <div class="price-info">
                <span>起送：¥{{ business.starPrice }}</span>
                <span>配送：¥{{ business.deliveryPrice }}</span>
              </div>
            </div>
          </div>
          <div v-if="businessList.length === 0" class="empty-state">
            <el-empty description="暂无商家信息" />
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Auth } from '../utils/auth'
import { ElMessage } from 'element-plus'

export default {
  name: 'Index',
  setup() {
    const router = useRouter()
    const searchKeyword = ref('')
    const businessList = ref([])
    const user = computed(() => Auth.getUser())
    const username = computed(() => user.value.username)
    const isLoggedIn = computed(() => Auth.isLoggedIn())
    const isAdmin = computed(() => Auth.isAdmin())
    const isBusiness = computed(() => Auth.isBusiness())

    const loadBusinessList = async () => {
      try {
        const response = await axios.get('/api/admin/businesses')
        const result = response.data || {}
        businessList.value = result.success ? (result.data || []) : []
      } catch (error) {
        console.error('加载商家列表失败:', error)
        ElMessage.error('加载商家列表失败')
        businessList.value = []
      }
    }

    const searchBusiness = () => {
      if (searchKeyword.value.trim()) {
        // 这里可以实现搜索逻辑
        loadBusinessList()
      } else {
        loadBusinessList()
      }
    }

    const goToBusinessInfo = (businessId) => {
      router.push(`/business-info/${businessId}`)
    }

    const goToLogin = () => {
      router.push('/login')
    }

    const handleCommand = (command) => {
      if (command === 'logout') {
        Auth.clearUser()
        ElMessage.success('已退出登录')
        router.push('/')
      } else if (command === 'admin') {
        router.push('/admin')
      } else if (command === 'business') {
        router.push('/business')
      } else if (command === 'orderList') {
        router.push('/order-list')
      } else if (command === 'address') {
        router.push('/user-address')
      }
    }

    onMounted(() => {
      loadBusinessList()
    })

    return {
      searchKeyword,
      businessList,
      username,
      isLoggedIn,
      isAdmin,
      isBusiness,
      searchBusiness,
      goToBusinessInfo,
      goToLogin,
      handleCommand
    }
  }
}
</script>

<style scoped>
.index-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.mobile-header {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  padding: 15px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h1 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
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
  padding: 15px;
  padding-bottom: 80px;
}

.search-section {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
}

.business-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.business-card {
  background: white;
  border-radius: 12px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.business-card:active {
  transform: scale(0.98);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.12);
}

.business-info h3 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 18px;
  font-weight: bold;
}

.address {
  color: #666;
  font-size: 14px;
  margin: 5px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.explain {
  color: #999;
  font-size: 13px;
  margin: 5px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price-info {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
  font-size: 14px;
  color: #409eff;
  font-weight: 500;
}

.empty-state {
  padding: 40px 20px;
  text-align: center;
}

/* 移动端优化 */
@media (max-width: 768px) {
  .header-content h1 {
    font-size: 20px;
  }

  .mobile-main {
    padding: 10px;
  }

  .business-card {
    padding: 12px;
  }
}
</style>
