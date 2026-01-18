<template>
  <div class="merchant-manager">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h2>商家管理</h2>
          <div class="search-bar">
            <el-input
              v-model="keyword"
              placeholder="输入商家名称或地址"
              clearable
              @keyup.enter="searchBusinesses"
            >
              <template #append>
                <el-button @click="searchBusinesses">搜索</el-button>
              </template>
            </el-input>
          </div>
          <div class="header-actions">
            <el-button @click="goToDashboard">返回首页</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <el-table :data="businessList" style="width: 100%" v-loading="loading">
          <el-table-column prop="businessId" label="ID" width="80" />
          <el-table-column prop="password" label="账号" width="120">
            <template #default="scope">
              {{ scope.row.password || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="businessName" label="名称" min-width="160" />
          <el-table-column prop="businessAddress" label="地址" min-width="200" />
          <el-table-column prop="businessExplain" label="介绍" min-width="200" />
          <el-table-column prop="starPrice" label="起送费" width="110">
            <template #default="scope">
              ¥{{ formatPrice(scope.row.starPrice) }}
            </template>
          </el-table-column>
          <el-table-column prop="deliveryPrice" label="配送费" width="110">
            <template #default="scope">
              ¥{{ formatPrice(scope.row.deliveryPrice) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160">
            <template #default="scope">
              <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="removeBusiness(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-dialog v-model="editDialogVisible" title="编辑商家" width="480px">
        <el-form :model="editForm" label-width="100px">
          <el-form-item label="商家名称">
            <el-input v-model="editForm.businessName" placeholder="请输入商家名称" />
          </el-form-item>
          <el-form-item label="商家地址">
            <el-input v-model="editForm.businessAddress" placeholder="请输入商家地址" />
          </el-form-item>
          <el-form-item label="商家介绍">
            <el-input v-model="editForm.businessExplain" placeholder="请输入商家介绍" />
          </el-form-item>
          <el-form-item label="起送费">
            <el-input-number v-model="editForm.starPrice" :min="0" :precision="2" />
          </el-form-item>
          <el-form-item label="配送费">
            <el-input-number v-model="editForm.deliveryPrice" :min="0" :precision="2" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="saveBusiness">保存</el-button>
        </template>
      </el-dialog>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'MerchantManager',
  setup() {
    const router = useRouter()
    const keyword = ref('')
    const businessList = ref([])
    const loading = ref(false)
    const editDialogVisible = ref(false)
    const saving = ref(false)
    const editForm = ref({
      businessId: null,
      businessName: '',
      businessAddress: '',
      businessExplain: '',
      starPrice: 0,
      deliveryPrice: 0
    })

    const formatPrice = (value) => {
      const numberValue = Number(value || 0)
      return numberValue.toFixed(2)
    }

    const loadBusinesses = async () => {
      loading.value = true
      try {
        const response = await axios.get('/api/admin/businesses')
        const result = response.data || {}
        businessList.value = result.success ? (result.data || []) : []
      } catch (error) {
        console.error('加载商家列表失败:', error)
        ElMessage.error('加载商家列表失败')
        businessList.value = []
      } finally {
        loading.value = false
      }
    }

    const searchBusinesses = async () => {
      const trimmed = keyword.value.trim()
      if (!trimmed) {
        loadBusinesses()
        return
      }

      loading.value = true
      try {
        const response = await axios.get('/api/admin/businesses', {
          params: { keyword: trimmed }
        })
        const result = response.data || {}
        businessList.value = result.success ? (result.data || []) : []
      } catch (error) {
        console.error('搜索商家失败:', error)
        ElMessage.error('搜索商家失败')
      } finally {
        loading.value = false
      }
    }

    const openEditDialog = (row) => {
      editForm.value = {
        businessId: row.businessId,
        businessName: row.businessName || '',
        businessAddress: row.businessAddress || '',
        businessExplain: row.businessExplain || '',
        starPrice: row.starPrice || 0,
        deliveryPrice: row.deliveryPrice || 0
      }
      editDialogVisible.value = true
    }

    const saveBusiness = async () => {
      if (!editForm.value.businessName) {
        ElMessage.warning('请输入商家名称')
        return
      }

      saving.value = true
      try {
        const response = await axios.put(`/api/business/${editForm.value.businessId}`, {
          businessName: editForm.value.businessName,
          businessAddress: editForm.value.businessAddress,
          businessExplain: editForm.value.businessExplain,
          starPrice: editForm.value.starPrice,
          deliveryPrice: editForm.value.deliveryPrice
        })
        const result = response.data || {}
        if (result.success) {
          ElMessage.success(result.message || '更新成功')
          editDialogVisible.value = false
          await loadBusinesses()
        } else {
          ElMessage.error(result.message || '更新失败')
        }
      } catch (error) {
        console.error('更新商家失败:', error)
        ElMessage.error('更新商家失败，请检查网络连接')
      } finally {
        saving.value = false
      }
    }

    const removeBusiness = async (row) => {
      try {
        await ElMessageBox.confirm(
          '确定要删除该商家及其所有食品吗？',
          '删除确认',
          { type: 'warning' }
        )
      } catch (error) {
        return
      }

      try {
        const response = await axios.delete(`/api/admin/businesses/${row.businessId}`)
        const result = response.data || {}
        if (result.success) {
          ElMessage.success(result.message || '删除成功')
          await loadBusinesses()
        } else {
          ElMessage.error(result.message || '删除失败')
        }
      } catch (error) {
        console.error('删除商家失败:', error)
        ElMessage.error('删除商家失败，请检查网络连接')
      }
    }

    onMounted(() => {
      loadBusinesses()
    })

    const goToDashboard = () => {
      router.push('/admin')
    }

    return {
      keyword,
      businessList,
      loading,
      editDialogVisible,
      editForm,
      saving,
      formatPrice,
      searchBusinesses,
      openEditDialog,
      saveBusiness,
      removeBusiness,
      goToDashboard
    }
  }
}
</script>

<style scoped>
.merchant-manager {
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
  gap: 20px;
}

.header-content h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
  white-space: nowrap;
}

.search-bar {
  flex: 1;
  max-width: 480px;
}

.header-actions {
  display: flex;
  justify-content: flex-end;
}
</style>
