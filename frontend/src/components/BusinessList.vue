<template>
  <div class="business-list-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>商家列表</h1>
          <el-button @click="goBack">返回首页</el-button>
        </div>
      </el-header>
      <el-main>
        <el-table :data="businessList" style="width: 100%">
          <el-table-column prop="businessId" label="编号" width="80" />
          <el-table-column prop="businessName" label="商家名称" />
          <el-table-column prop="businessAddress" label="商家地址" />
          <el-table-column prop="businessExplain" label="商家介绍" />
          <el-table-column prop="starPrice" label="起送费" width="100">
            <template #default="scope">
              ¥{{ scope.row.starPrice }}
            </template>
          </el-table-column>
          <el-table-column prop="deliveryPrice" label="配送费" width="100">
            <template #default="scope">
              ¥{{ scope.row.deliveryPrice }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button size="small" @click="viewBusiness(scope.row.businessId)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

export default {
  name: 'BusinessList',
  setup() {
    const router = useRouter()
    const businessList = ref([])

    const loadBusinessList = async () => {
      try {
        const response = await axios.get('/api/admin/businesses')
        const result = response.data || {}
        businessList.value = result.success ? (result.data || []) : []
      } catch (error) {
        console.error('加载商家列表失败:', error)
      }
    }

    const viewBusiness = (businessId) => {
      router.push(`/business-info/${businessId}`)
    }

    const goBack = () => {
      router.push('/')
    }

    onMounted(() => {
      loadBusinessList()
    })

    return {
      businessList,
      viewBusiness,
      goBack
    }
  }
}
</script>

<style scoped>
.business-list-container {
  min-height: 100vh;
}

.el-header {
  background-color: #409eff;
  color: white;
  line-height: 60px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}
</style>


