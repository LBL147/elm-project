---

# 🛠️ 《饿了么商家后台管理系统》技术设计文档

1. 系统架构设计 

本项目采用经典的三层架构模式，确保业务逻辑、数据访问和界面展示的分离：

| 层次名称 | 包名 | 核心职责 |
| --- | --- | --- |
| **领域模型层 (PO)** | `com.neusoft.elm.po` | 对应数据库表结构的 Java 对象（Persistant Object） 。

 |
| **数据访问层 (DAO)** | `com.neusoft.elm.dao` | 封装低级别的 JDBC API，负责 SQL 语句的执行 。

 |
| **视图交互层 (View)** | `com.neusoft.elm.view` | 负责控制台界面的输入输出、业务逻辑判断及 DAO 调用 。

 |
| **工具类层 (Util)** | `com.neusoft.elm.util` | 提供数据库连接获取、资源关闭等通用支持 。

 |
| **入口层 (Entry)** | `com.neusoft.elm` | 程序主流程控制与系统启动 。

 |

## 2. 核心技术实现

2.1 数据库连接 (JDBC) 

* 
**驱动程序**：使用 `mysql-connector-java-bin.jar` 连接 MySQL 。


* 
**连接管理**：通过 `DBUtil.getConnection()` 获取 `Connection` 对象 。


* 
**资源释放**：必须在 `finally` 块中关闭 `ResultSet`、`PreparedStatement` 和 `Connection` 。



2.2 关键业务逻辑 

* 
**自增主键获取**：在 `saveBusiness` 时，通过 `RETURN_GENERATED_KEYS` 获取数据库自动生成的 `businessId` 。


* 
**模糊查询**：利用 `StringBuffer` 动态拼接 SQL，使用 `LIKE '%关键词%'` 实现 。


* 
**事务处理 (Transaction)**：在删除商家时，需开启手动提交模式 (`setAutoCommit(false)`)，确保级联删除食品和商家的操作要么全部成功，要么全部回滚 。


