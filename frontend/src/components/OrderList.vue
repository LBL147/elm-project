<template>
  <div class="order-list-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>我的订单</h1>
        </div>
      </el-header>
      <el-main>
        <el-table :data="orderList" style="width: 100%">
          <el-table-column prop="orderId" label="订单号" />
          <el-table-column prop="businessName" label="商家名称" />
          <el-table-column prop="orderDate" label="下单时间" />
          <el-table-column prop="orderTotal" label="订单金额" width="120">
            <template #default="scope">
              ¥{{ scope.row.orderTotal }}
            </template>
          </el-table-column>
          <el-table-column prop="orderState" label="订单状态" width="120">
            <template #default="scope">
              <el-tag v-if="scope.row.orderState === 0">未支付</el-tag>
              <el-tag type="success" v-else-if="scope.row.orderState === 1">已支付</el-tag>
              <el-tag type="warning" v-else-if="scope.row.orderState === 2">配送中</el-tag>
              <el-tag type="info" v-else>已完成</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button size="small" @click="viewOrderDetail(scope.row.orderId)">
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
import axios from 'axios'

export default {
  name: 'OrderList',
  setup() {
    const orderList = ref([])

    const loadOrderList = async () => {
      try {
        const response = await axios.get('/api/order/list')
        orderList.value = response.data
      } catch (error) {
        console.error('加载订单列表失败:', error)
      }
    }

    const viewOrderDetail = (orderId) => {
      // 查看订单详情
      console.log('查看订单详情:', orderId)
      // 可以使用router跳转到订单详情页
      // router.push(`/order-detail/${orderId}`)
    }

    onMounted(() => {
      loadOrderList()
    })

    return {
      orderList,
      viewOrderDetail
    }
  }
}
</script>

<style scoped>
.order-list-container {
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

