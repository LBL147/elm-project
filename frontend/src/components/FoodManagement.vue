<template>
  <div class="food-management">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h2>食品管理</h2>
          <div class="header-actions">
            <el-button type="primary" @click="openAddDialog">新增食品</el-button>
            <el-button @click="goToDashboard">返回首页</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <el-table :data="foodList" style="width: 100%" v-loading="loading">
          <el-table-column prop="foodId" label="食品ID" width="90" />
          <el-table-column label="图片" width="120">
            <template #default="scope">
              <el-image
                v-if="resolveImage(scope.row)"
                :src="resolveImage(scope.row)"
                :preview-src-list="[resolveImage(scope.row)]"
                fit="cover"
                style="width: 60px; height: 60px"
              >
                <template #error>
                  <span class="image-placeholder">无</span>
                </template>
              </el-image>
              <span v-else class="image-placeholder">无</span>
            </template>
          </el-table-column>
          <el-table-column prop="foodName" label="名称" min-width="160" />
          <el-table-column prop="foodExplain" label="介绍" min-width="200" />
          <el-table-column prop="foodPrice" label="价格" width="120">
            <template #default="scope">
              ¥{{ Number(scope.row.foodPrice || 0).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160">
            <template #default="scope">
              <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="removeFood(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="420px">
        <el-form :model="foodForm" label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="foodForm.foodName" placeholder="请输入食品名称" />
          </el-form-item>
          <el-form-item label="介绍">
            <el-input v-model="foodForm.foodExplain" placeholder="请输入食品介绍" />
          </el-form-item>
          <el-form-item label="价格">
            <el-input-number v-model="foodForm.foodPrice" :min="0" :precision="2" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="saveFood">确定</el-button>
        </template>
      </el-dialog>
    </el-container>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Auth } from '../utils/auth'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'FoodManagement',
  setup() {
    const router = useRouter()
    const businessId = ref(Auth.getUser().userId)
    const foodList = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogMode = ref('add')
    const editingFoodId = ref(null)
    const saving = ref(false)
    const foodForm = ref({
      foodName: '',
      foodExplain: '',
      foodPrice: 0
    })

    const dialogTitle = computed(() => (dialogMode.value === 'add' ? '新增食品' : '编辑食品'))

    const resolveImage = (row) => {
      return row.imageUrl || row.foodImg || row.foodImage || ''
    }

    const loadFoods = async () => {
      if (!businessId.value) {
        ElMessage.warning('商家信息不存在')
        return
      }
      loading.value = true
      try {
        const response = await axios.get(`/api/food/business/${businessId.value}`)
        const result = response.data || {}
        foodList.value = result.success ? (result.data || []) : []
      } catch (error) {
        console.error('加载食品列表失败:', error)
        ElMessage.error('加载食品列表失败')
        foodList.value = []
      } finally {
        loading.value = false
      }
    }

    const openAddDialog = () => {
      dialogMode.value = 'add'
      editingFoodId.value = null
      foodForm.value = {
        foodName: '',
        foodExplain: '',
        foodPrice: 0
      }
      dialogVisible.value = true
    }

    const openEditDialog = (row) => {
      dialogMode.value = 'edit'
      editingFoodId.value = row.foodId
      foodForm.value = {
        foodName: row.foodName || '',
        foodExplain: row.foodExplain || '',
        foodPrice: row.foodPrice || 0
      }
      dialogVisible.value = true
    }

    const saveFood = async () => {
      if (!foodForm.value.foodName) {
        ElMessage.warning('请输入食品名称')
        return
      }
      if (foodForm.value.foodPrice === null || foodForm.value.foodPrice === undefined) {
        ElMessage.warning('请输入食品价格')
        return
      }

      saving.value = true
      try {
        if (dialogMode.value === 'add') {
          if (!businessId.value) {
            ElMessage.warning('商家信息不存在')
            return
          }
          const response = await axios.post('/api/food', {
            foodName: foodForm.value.foodName,
            foodExplain: foodForm.value.foodExplain,
            foodPrice: foodForm.value.foodPrice,
            businessId: Number(businessId.value)
          })
          const result = response.data || {}
          if (result.success) {
            ElMessage.success('新增食品成功')
            dialogVisible.value = false
            await loadFoods()
          } else {
            ElMessage.error(result.message || '新增食品失败')
          }
        } else {
          const response = await axios.put(`/api/food/${editingFoodId.value}`, {
            foodName: foodForm.value.foodName,
            foodExplain: foodForm.value.foodExplain,
            foodPrice: foodForm.value.foodPrice
          })
          const result = response.data || {}
          if (result.success) {
            ElMessage.success('修改食品成功')
            dialogVisible.value = false
            await loadFoods()
          } else {
            ElMessage.error(result.message || '修改食品失败')
          }
        }
      } catch (error) {
        console.error('保存食品失败:', error)
        ElMessage.error('保存食品失败，请检查网络连接')
      } finally {
        saving.value = false
      }
    }

    const removeFood = async (row) => {
      try {
        await ElMessageBox.confirm(`确认删除食品【${row.foodName}】吗？`, '提示', {
          type: 'warning'
        })
      } catch (error) {
        return
      }

      try {
        const response = await axios.delete(`/api/food/${row.foodId}`)
        const result = response.data || {}
        if (result.success) {
          ElMessage.success('删除食品成功')
          await loadFoods()
        } else {
          ElMessage.error(result.message || '删除食品失败')
        }
      } catch (error) {
        console.error('删除食品失败:', error)
        ElMessage.error('删除食品失败，请检查网络连接')
      }
    }

    onMounted(() => {
      if (!Auth.isBusiness()) {
        router.push('/')
        return
      }
      loadFoods()
    })

    const goToDashboard = () => {
      router.push('/business')
    }

    return {
      foodList,
      loading,
      dialogVisible,
      dialogTitle,
      foodForm,
      saving,
      goToDashboard,
      openAddDialog,
      openEditDialog,
      saveFood,
      removeFood,
      resolveImage
    }
  }
}
</script>

<style scoped>
.food-management {
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.image-placeholder {
  color: #999;
  font-size: 12px;
}
</style>
