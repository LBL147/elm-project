# 饿了么商家后台管理系统 - API 文档

## 基础信息

- **Base URL**: `http://localhost:8080`
- **数据格式**: JSON
- **字符编码**: UTF-8

## 统一响应格式

所有接口均返回以下格式：

```json
{
  "success": true,         // 操作是否成功
  "message": "操作成功",   // 提示信息
  "data": { ... }          // 响应数据（可选）
}
```

---

## 1. 管理员端接口

### 1.1 管理员登录

**接口**: `POST /api/admin/login`

**请求体**:
```json
{
  "username": "admin",
  "password": "123456"
}
```

**成功响应**:
```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "token": "admin_token_1",
    "type": "admin",
    "adminId": 1,
    "adminName": "admin"
  }
}
```

**失败响应**:
```json
{
  "success": false,
  "message": "用户名或密码错误",
  "data": null
}
```

---

### 1.2 查询所有商家

**接口**: `GET /api/admin/businesses`

**成功响应**:
```json
{
  "success": true,
  "message": "查询成功，共 3 家商家",
  "data": [
    {
      "businessId": 2,
      "businessName": "肯德基(中关村店)",
      "businessAddress": "北京市海淀区中关村大街1号",
      "businessExplain": "快餐连锁",
      "starPrice": 20.0,
      "deliveryPrice": 5.0,
      "password": "123"
    }
  ]
}
```

---

### 1.3 搜索商家（模糊查询）

**接口**: `GET /api/admin/businesses/search?name={name}&address={address}`

**查询参数**:
- `name`: 商家名称关键词（可选）
- `address`: 商家地址关键词（可选）

**示例**: `GET /api/admin/businesses/search?name=肯德基`

**成功响应**:
```json
{
  "success": true,
  "message": "查询成功，共 1 家商家",
  "data": [ ... ]
}
```

---

### 1.4 新建商家

**接口**: `POST /api/admin/businesses`

**请求体**:
```json
{
  "businessName": "星巴克",
  "businessAddress": "北京市朝阳区",
  "businessExplain": "咖啡连锁店",
  "starPrice": 20.0,
  "deliveryPrice": 5.0
}
```

**成功响应**:
```json
{
  "success": true,
  "message": "新建商家成功",
  "data": {
    "businessId": 5,
    "defaultPassword": "123"
  }
}
```

---

### 1.5 删除商家（级联删除）

**接口**: `DELETE /api/admin/businesses/{id}`

**示例**: `DELETE /api/admin/businesses/1`

**成功响应**:
```json
{
  "success": true,
  "message": "删除商家成功，该商家及其所有食品已被删除",
  "data": null
}
```

**核心特性**:
- ✅ 使用 JDBC 手动事务处理
- ✅ 先删除该商家的所有食品
- ✅ 再删除商家记录
- ✅ 任何步骤失败都会回滚

---

## 2. 商家端接口

### 2.1 商家登录

**接口**: `POST /api/business/login`

**请求体**:
```json
{
  "username": "2",      // 商家编号
  "password": "123"
}
```

**成功响应**:
```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "token": "business_token_2",
    "type": "business",
    "businessId": 2,
    "businessName": "肯德基(中关村店)"
  }
}
```

---

### 2.2 查看商家信息

**接口**: `GET /api/business/{id}`

**示例**: `GET /api/business/2`

**成功响应**:
```json
{
  "success": true,
  "message": "查询成功",
  "data": {
    "businessId": 2,
    "businessName": "肯德基(中关村店)",
    "businessAddress": "北京市海淀区中关村大街1号",
    "businessExplain": "快餐连锁",
    "starPrice": 20.0,
    "deliveryPrice": 5.0,
    "password": "123"
  }
}
```

---

### 2.3 修改商家信息

**接口**: `PUT /api/business/{id}`

**请求体**:
```json
{
  "businessName": "肯德基(更新后)",
  "businessAddress": "北京市海淀区",
  "businessExplain": "快餐连锁",
  "starPrice": 25.0,
  "deliveryPrice": 6.0
}
```

**成功响应**:
```json
{
  "success": true,
  "message": "修改商家信息成功",
  "data": null
}
```

---

### 2.4 修改商家密码

**接口**: `PUT /api/business/{id}/password`

**请求体**:
```json
{
  "newPassword": "newpass123",
  "confirmPassword": "newpass123"
}
```

**成功响应**:
```json
{
  "success": true,
  "message": "修改密码成功，下次登录请使用新密码",
  "data": null
}
```

---

## 3. 食品管理接口

### 3.1 查询指定商家的所有食品

**接口**: `GET /api/food/business/{businessId}`

**示例**: `GET /api/food/business/2`

**成功响应**:
```json
{
  "success": true,
  "message": "查询成功，共 5 个食品",
  "data": [
    {
      "foodId": 4,
      "foodName": "香辣鸡腿堡",
      "foodExplain": "经典美味",
      "foodPrice": 18.5,
      "businessId": 2
    }
  ]
}
```

---

### 3.2 查询单个食品信息

**接口**: `GET /api/food/{id}`

**示例**: `GET /api/food/4`

**成功响应**:
```json
{
  "success": true,
  "message": "查询成功",
  "data": {
    "foodId": 4,
    "foodName": "香辣鸡腿堡",
    "foodExplain": "经典美味",
    "foodPrice": 18.5,
    "businessId": 2
  }
}
```

---

### 3.3 新增食品

**接口**: `POST /api/food`

**请求体**:
```json
{
  "foodName": "新奥尔良烤翅",
  "foodExplain": "香辣可口",
  "foodPrice": 15.0,
  "businessId": 2
}
```

**成功响应**:
```json
{
  "success": true,
  "message": "新增食品成功",
  "data": {
    "foodId": 10
  }
}
```

---

### 3.4 修改食品

**接口**: `PUT /api/food/{id}`

**请求体**:
```json
{
  "foodName": "香辣鸡腿堡(升级版)",
  "foodExplain": "更加美味",
  "foodPrice": 20.0
}
```

**成功响应**:
```json
{
  "success": true,
  "message": "修改食品成功",
  "data": null
}
```

---

### 3.5 删除食品

**接口**: `DELETE /api/food/{id}`

**示例**: `DELETE /api/food/10`

**成功响应**:
```json
{
  "success": true,
  "message": "删除食品成功",
  "data": null
}
```

**注意**: 此操作是单独删除食品，不涉及事务处理。商家级联删除的事务处理在 `DELETE /api/admin/businesses/{id}` 中已实现。

---

### 3.6 验证食品所有权

**接口**: `GET /api/food/{id}/verify?businessId={businessId}`

**示例**: `GET /api/food/4/verify?businessId=2`

**成功响应（验证通过）**:
```json
{
  "success": true,
  "message": "验证通过",
  "data": {
    "isOwner": true,
    "foodName": "香辣鸡腿堡"
  }
}
```

**失败响应（验证失败）**:
```json
{
  "success": false,
  "message": "该食品不属于您的商家，无权操作",
  "data": {
    "isOwner": false,
    "foodName": "香辣鸡腿堡"
  }
}
```

---

## 4. 错误处理

所有异常都会被全局异常处理器捕获，返回统一的错误格式：

### 4.1 数据库异常

**场景**: SQL 执行失败、连接超时等

**响应**:
```json
{
  "success": false,
  "message": "数据库操作失败：Table 'elm.xxx' doesn't exist",
  "data": null
}
```

---

### 4.2 参数格式错误

**场景**: 商家编号格式错误等

**响应**:
```json
{
  "success": false,
  "message": "参数格式错误，请检查输入的数字格式",
  "data": null
}
```

---

### 4.3 参数验证失败

**场景**: 必填参数为空等

**响应**:
```json
{
  "success": false,
  "message": "商家名称不能为空",
  "data": null
}
```

---

### 4.4 未知异常

**场景**: 未预期的服务器错误

**响应**:
```json
{
  "success": false,
  "message": "服务器处理请求时发生错误，请联系管理员",
  "data": null
}
```

---

## 5. 前后端对接说明

### 5.1 Token 格式约定

- **管理员**: `admin_token_{adminId}`
- **商家**: `business_token_{businessId}`

前端从 token 中提取用户 ID 的方式：
```javascript
// 管理员
const adminId = token.replace('admin_token_', '')

// 商家
const businessId = token.replace('business_token_', '')
```

---

### 5.2 CORS 配置

后端已配置 CORS，允许来自任何源的跨域请求（开发环境）。生产环境应限制为具体域名。

---

### 5.3 接口路径前缀

所有接口路径均以 `/api` 开头，符合前端 `vue.config.js` 的代理配置：
```javascript
'/api' → 'http://localhost:8080'
```

---

## 6. 事务一致性说明

### 6.1 商家级联删除事务

**接口**: `DELETE /api/admin/businesses/{id}`

**事务处理流程**:
```java
conn.setAutoCommit(false);  // 开启手动事务

// 步骤 1: 删除该商家的所有食品
DELETE FROM food WHERE businessId = ?

// 步骤 2: 删除商家记录
DELETE FROM business WHERE businessId = ?

conn.commit();  // 提交事务
```

**异常处理**:
- 任何步骤失败都会执行 `conn.rollback()`
- 确保数据一致性（要么全部删除，要么全部保留）

---

### 6.2 食品删除操作

**接口**: `DELETE /api/food/{id}`

**特点**:
- 单独删除食品，不涉及级联操作
- 不需要事务处理
- 执行简单的 `DELETE FROM food WHERE foodId = ?`

---

## 7. 测试建议

### 7.1 Postman 测试集合

建议创建以下测试用例：

1. **管理员登录** → 获取 token
2. **查询所有商家** → 验证列表返回
3. **搜索商家** → 验证模糊查询
4. **新建商家** → 记录新商家 ID
5. **删除商家** → 验证级联删除（重要！）
6. **商家登录** → 获取 business token
7. **查看商家信息** → 验证数据正确性
8. **修改商家信息** → 验证更新成功
9. **修改商家密码** → 验证密码更新
10. **查询食品列表** → 验证商家的食品
11. **新增食品** → 记录新食品 ID
12. **修改食品** → 验证更新成功
13. **删除食品** → 验证删除成功
14. **验证食品所有权** → 验证权限检查

---

### 7.2 关键测试点

✅ **级联删除测试**（最重要）:
1. 创建一个新商家
2. 为该商家添加 2-3 个食品
3. 删除该商家
4. 验证商家和食品都已被删除

✅ **权限验证测试**:
1. 商家 A 创建食品
2. 商家 B 尝试修改/删除该食品
3. 验证操作失败

✅ **异常处理测试**:
1. 故意发送错误格式的请求
2. 验证返回友好的错误信息（而非 500 错误页面）

---

## 8. 注意事项

1. ⚠️ 密码明文存储（生产环境应使用 BCrypt 加密）
2. ⚠️ Token 无过期时间（生产环境应使用 JWT）
3. ⚠️ CORS 允许所有来源（生产环境应限制域名）
4. ✅ 所有 SQL 逻辑已验证通过
5. ✅ JDBC 事务处理已实现并测试
6. ✅ 全局异常处理已配置
