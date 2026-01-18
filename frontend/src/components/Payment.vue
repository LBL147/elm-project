<template>
  <div class="payment-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>支付页面</h1>
        </div>
      </el-header>
      <el-main>
        <el-card>
          <h2>订单信息</h2>
          <p>订单号：{{ orderId }}</p>
          <p>订单金额：¥{{ orderAmount }}</p>
        </el-card>
        <el-card class="payment-methods">
          <h2>选择支付方式</h2>
          <el-radio-group v-model="paymentMethod">
            <el-radio label="alipay">支付宝</el-radio>
            <el-radio label="wechat">微信支付</el-radio>
          </el-radio-group>
        </el-card>
        <div class="payment-actions">
          <el-button type="primary" size="large" @click="submitPayment">
            确认支付
          </el-button>
          <el-button size="large" @click="goBack">取消</el-button>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'Payment',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const orderId = ref(route.query.orderId || '')
    const orderAmount = ref(route.query.amount || '0.00')
    const paymentMethod = ref('alipay')

    const submitPayment = () => {
      // 支付逻辑
      alert('支付成功！')
      router.push('/order-list')
    }

    const goBack = () => {
      router.back()
    }

    return {
      orderId,
      orderAmount,
      paymentMethod,
      submitPayment,
      goBack
    }
  }
}
</script>

<style scoped>
.payment-container {
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

.payment-methods {
  margin-top: 20px;
}

.payment-actions {
  margin-top: 20px;
  text-align: center;
}
</style>


