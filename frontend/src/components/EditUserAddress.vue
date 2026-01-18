<template>
  <div class="edit-address-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>编辑收货地址</h1>
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
              <el-button type="primary" @click="submitAddress">保存</el-button>
              <el-button @click="goBack">取消</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

export default {
  name: 'EditUserAddress',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const addressForm = ref({
      id: null,
      contactName: '',
      contactSex: '1',
      contactTel: '',
      address: ''
    })

    const loadAddress = async () => {
      const id = route.params.id
      try {
        const response = await axios.get(`/api/address/${id}`)
        addressForm.value = response.data
      } catch (error) {
        console.error('加载地址失败:', error)
      }
    }

    const submitAddress = async () => {
      try {
        await axios.put(`/api/address/${addressForm.value.id}`, addressForm.value)
        router.push('/user-address')
      } catch (error) {
        console.error('更新地址失败:', error)
      }
    }

    const goBack = () => {
      router.back()
    }

    onMounted(() => {
      loadAddress()
    })

    return {
      addressForm,
      submitAddress,
      goBack
    }
  }
}
</script>

<style scoped>
.edit-address-container {
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


