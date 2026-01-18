<template>
  <div class="business-info">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <el-button @click="goBack" icon="ArrowLeft" circle plain />
          <h2>商家信息</h2>
          <div style="width: 40px;"></div>
        </div>
      </el-header>
      <el-main class="main">
        <el-card class="info-card">
          <el-form :model="form" label-width="100px">
            <el-form-item label="商家编号">
              <el-input v-model="form.businessId" disabled />
            </el-form-item>
            <el-form-item label="商家名称">
              <el-input v-model="form.businessName" placeholder="请输入商家名称" />
            </el-form-item>
            <el-form-item label="商家地址">
              <el-input v-model="form.businessAddress" placeholder="请输入商家地址" />
            </el-form-item>
            <el-form-item label="商家介绍">
              <el-input v-model="form.businessExplain" placeholder="请输入商家介绍" />
            </el-form-item>
            <el-form-item label="起送费">
              <el-input-number v-model="form.starPrice" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item label="配送费">
              <el-input-number v-model="form.deliveryPrice" :min="0" :precision="2" />
            </el-form-item>
          </el-form>
          <div class="actions">
            <el-button type="primary" :loading="saving" @click="saveBusiness">保存修改</el-button>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { Auth } from '../utils/auth'
import { ElMessage } from 'element-plus'

export default {
  name: 'BusinessInfo',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const form = ref({
      businessId: '',
      businessName: '',
      businessAddress: '',
      businessExplain: '',
      starPrice: 0,
      deliveryPrice: 0
    })
    const saving = ref(false)

    const loadBusiness = async () => {
      const businessId = route.params.id || Auth.getUser().userId
      if (!businessId) {
        ElMessage.warning('商家信息不存在')
        return
      }
      try {
        const response = await axios.get(`/api/business/${businessId}`)
        const result = response.data || {}
        if (result.success) {
          const data = result.data || {}
          form.value = {
            businessId: data.businessId || businessId,
            businessName: data.businessName || '',
            businessAddress: data.businessAddress || '',
            businessExplain: data.businessExplain || '',
            starPrice: data.starPrice || 0,
            deliveryPrice: data.deliveryPrice || 0
          }
        } else {
          ElMessage.error(result.message || '加载商家信息失败')
        }
      } catch (error) {
        console.error('加载商家信息失败:', error)
        ElMessage.error('加载商家信息失败')
      }
    }

    const saveBusiness = async () => {
      if (!form.value.businessName) {
        ElMessage.warning('请输入商家名称')
        return
      }
      if (!form.value.businessId) {
        ElMessage.warning('商家编号无效')
        return
      }

      saving.value = true
      try {
        const response = await axios.put(`/api/business/${form.value.businessId}`, {
          businessName: form.value.businessName,
          businessAddress: form.value.businessAddress,
          businessExplain: form.value.businessExplain,
          starPrice: form.value.starPrice,
          deliveryPrice: form.value.deliveryPrice
        })
        const result = response.data || {}
        if (result.success) {
          ElMessage.success(result.message || '保存成功')
        } else {
          ElMessage.error(result.message || '保存失败')
        }
      } catch (error) {
        console.error('保存商家信息失败:', error)
        ElMessage.error('保存商家信息失败，请检查网络连接')
      } finally {
        saving.value = false
      }
    }

    const goBack = () => {
      router.back()
    }

    onMounted(() => {
      if (!Auth.isLoggedIn()) {
        router.push('/login')
        return
      }
      loadBusiness()
    })

    return {
      form,
      saving,
      saveBusiness,
      goBack
    }
  }
}
</script>

<style scoped>
.business-info {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
  background: #ffffff;
  border-bottom: 1px solid #ebeef5;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
}

.header-content h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.main {
  padding: 20px;
}

.info-card {
  max-width: 800px;
  margin: 0 auto;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}
</style>
