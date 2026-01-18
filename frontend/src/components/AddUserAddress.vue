<template>
  <div class="add-address-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>新增收货地址</h1>
        </div>
      </el-header>
      <el-main>
        <el-card>
          <el-form :model="addressForm" label-width="100px">
            <el-form-item label="联系人">
              <el-input v-model="addressForm.contactName" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="addressForm.contactSex">
                <el-radio label="1">男</el-radio>
                <el-radio label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="addressForm.contactTel" />
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="addressForm.address" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitAddress">提交</el-button>
              <el-button @click="goBack">取消</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

export default {
  name: 'AddUserAddress',
  setup() {
    const router = useRouter()
    const addressForm = ref({
      contactName: '',
      contactSex: '1',
      contactTel: '',
      address: ''
    })

    const submitAddress = async () => {
      try {
        await axios.post('/api/address/add', addressForm.value)
        router.push('/user-address')
      } catch (error) {
        console.error('添加地址失败:', error)
      }
    }

    const goBack = () => {
      router.back()
    }

    return {
      addressForm,
      submitAddress,
      goBack
    }
  }
}
</script>

<style scoped>
.add-address-container {
  min-height: 100vh;
}

.el-header {
  background-color: #409eff;
  color: white;
  line-height: 60px;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
}
</style>


