
---

# 📝 饿了么商家后台管理系统 - 后端需求文档

## 1. 项目概述

本项目为课程级贯穿项目的第一阶段，主要目标是使用 **JavaSE + JDBC + MySQL** 技术开发一个基于控制台（C/S 结构）的后台管理系统 。系统分为**管理员端**和**商家端**两大模块 。

---

2. 技术栈与环境 

* **开发工具**：IntelliJ IDEA
* **Java 版本**：JDK 8 及以上
* **数据库**：MySQL
* **数据库连接**：JDBC (需导入 `mysql-connector-java-bin.jar`)
* 
**项目架构**：PO-DAO-View 分层架构 


* **字符编码**：UTF-8

---

3. 数据库设计 (DB Schema) 

3.1 商家表 (business) 

| 字段名 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| **businessId** | int | PK, AI, NN | 商家编号 |
| **password** | varchar(20) | NN | 密码 (默认 123) |
| **businessName** | varchar(40) | NN | 商家名称 |
| **businessAddress** | varchar(50) | - | 商家地址 |
| **businessExplain** | varchar(40) | - | 商家介绍 |
| **starPrice** | decimal(5,2) | 默认 0.00 | 起送费 |
| **deliveryPrice** | decimal(5,2) | 默认 0.00 | 配送费 |

3.2 食品表 (food) 

| 字段名 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| **foodId** | int | PK, AI, NN | 食品编号 |
| **foodName** | varchar(30) | NN | 食品名称 |
| **foodExplain** | varchar(30) | - | 食品介绍 |
| **foodPrice** | decimal(5,2) | NN | 食品价格 |
| **businessId** | int | FK, NN | 所属商家编号 |

3.3 管理员表 (admin) 

| 字段名 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| **adminId** | int | PK, AI, NN | 管理员编号 |
| **adminName** | varchar(20) | NN, UQ | 管理员名称 |
| **password** | varchar(20) | NN | 密码 |

---

## 4. 业务功能需求

4.1 管理员端功能 (Admin Side) 

1. 
**管理员登录**：根据用户名和密码进行身份验证 。


2. 
**所有商家列表**：查询并展示所有注册商家 。


3. 
**搜索商家**：根据商家名称或地址进行模糊查询 。


4. 
**新建商家**：输入商家名称，系统自动生成编号并设定默认密码 。


5. 
**删除商家**：根据编号删除商家。**特别要求**：需级联删除该商家下的所有食品，必须使用 **JDBC 事务管理** 。



4.2 商家端功能 (Business Side) 

1. 
**商家登录**：根据商家编号和密码登录 。


2. 
**商家信息维护**：查看信息、修改基本资料（不含密码） 。


3. 
**修改密码**：输入新密码进行更新 。


4. **食品管理 (二级菜单)**：
* 
**食品列表**：查看当前商家名下的所有食品 。


* 
**新增食品**：为当前商家添加新菜品 。


* 
**修改食品**：更新已有菜品的价格或描述 。


* 
**删除食品**：移除指定菜品 。





---

5. 项目工程结构规范 

* 
**`com.neusoft.elm`**：项目主入口 (`ElmAdminEntry`) 。


* 
**`com.neusoft.elm.po`**：持久化对象 (Entity Classes) 。


* 
**`com.neusoft.elm.dao`**：数据访问接口 。


* 
**`com.neusoft.elm.dao.impl`**：JDBC 实现逻辑 。


* 
**`com.neusoft.elm.view`**：界面处理接口 。


* 
**`com.neusoft.elm.view.impl`**：控制台交互实现 。


* 
**`com.neusoft.elm.util`**：工具类 (`DBUtil`) 。



---
