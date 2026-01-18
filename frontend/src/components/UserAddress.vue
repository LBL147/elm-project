<template>
  <div class="user-address-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>收货地址管理</h1>
          <el-button type="primary" @click="goToAddAddress">新增地址</el-button>
        </div>
      </el-header>
      <el-main>
        <el-table :data="addressList" style="width: 100%">
          <el-table-column prop="contactName" label="联系人" />
          <el-table-column prop="contactSex" label="性别" width="80" />
          <el-table-column prop="contactTel" label="联系电话" />
          <el-table-column prop="address" label="地址" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="editAddress(scope.row.id)">
                编辑
              </el-button>
              <el-button size="small" type="danger" @click="deleteAddress(scope.row.id)">
                删除
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
  name: 'UserAddress',
  setup() {
    const router = useRouter()
    const addressList = ref([])

    const loadAddressList = async () => {
      try {
        const response = await axios.get('/api/address/list')
        addressList.value = response.data
      } catch (error) {
        console.error('加载地址列表失败:', error)
      }
    }

    const goToAddAddress = () => {
      router.push('/add-user-address')
    }

    const editAddress = (id) => {
      router.push(`/edit-user-address/${id}`)
    }

    const deleteAddress = async (id) => {
      try {
        await axios.delete(`/api/address/${id}`)
        loadAddressList()
      } catch (error) {
        console.error('删除地址失败:', error)
      }
    }

    onMounted(() => {
      loadAddressList()
    })

    return {
      addressList,
      goToAddAddress,
      editAddress,
      deleteAddress
    }
  }
}
</script>

<style scoped>
.user-address-container {
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


