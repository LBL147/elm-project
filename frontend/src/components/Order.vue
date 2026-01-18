<template>
  <div class="order-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>订单确认</h1>
        </div>
      </el-header>
      <el-main>
        <el-card>
          <h2>订单信息</h2>
          <el-table :data="cartItems" style="width: 100%">
            <el-table-column prop="foodName" label="食品名称" />
            <el-table-column prop="foodPrice" label="单价" width="100">
              <template #default="scope">
                ¥{{ scope.row.foodPrice }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column label="小计" width="100">
              <template #default="scope">
                ¥{{ (scope.row.foodPrice * scope.row.quantity).toFixed(2) }}
              </template>
            </el-table-column>
          </el-table>
          <div class="order-summary">
            <p><strong>总计：</strong>¥{{ totalPrice.toFixed(2) }}</p>
          </div>
        </el-card>
        <el-card class="address-card">
          <h2>收货地址</h2>
          <el-select v-model="selectedAddress" placeholder="请选择收货地址">
            <el-option
              v-for="address in addressList"
              :key="address.id"
              :label="address.address"
              :value="address.id"
            />
          </el-select>
          <el-button @click="goToAddress">管理地址</el-button>
        </el-card>
        <div class="order-actions">
          <el-button type="primary" size="large" @click="submitOrder">
            提交订单
          </el-button>
          <el-button size="large" @click="goBack">返回</el-button>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Order',
  setup() {
    const router = useRouter()
    const cartItems = ref([])
    const addressList = ref([])
    const selectedAddress = ref(null)

    const totalPrice = computed(() => {
      return cartItems.value.reduce((sum, item) => {
        return sum + item.foodPrice * item.quantity
      }, 0)
    })

    const submitOrder = () => {
      if (!selectedAddress.value) {
        alert('请选择收货地址')
        return
      }
      router.push('/payment')
    }

    const goToAddress = () => {
      router.push('/user-address')
    }

    const goBack = () => {
      router.back()
    }

    onMounted(() => {
      // 加载购物车和地址数据
    })

    return {
      cartItems,
      addressList,
      selectedAddress,
      totalPrice,
      submitOrder,
      goToAddress,
      goBack
    }
  }
}
</script>

<style scoped>
.order-container {
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

.order-summary {
  margin-top: 20px;
  text-align: right;
  font-size: 18px;
}

.address-card {
  margin-top: 20px;
}

.order-actions {
  margin-top: 20px;
  text-align: center;
}
</style>


