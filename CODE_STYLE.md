# 📏 《饿了么商家后台管理系统》代码规范文档

## 1. 命名规范

1.1 包命名 (Package) 

* 必须全部小写，采用公司反向域名格式：`com.neusoft.elm.[层名]`。
* 例如：`com.neusoft.elm.dao.impl`。

1.2 类与接口命名 (Class & Interface) 

* 
**实体类**：直接使用表名首字母大写，如 `Business`, `Food` 。


* 
**接口**：功能模块名 + 后缀，如 `BusinessDao`, `AdminView` 。


* 
**实现类**：接口名 + `Impl`，如 `BusinessDaoImpl`, `AdminViewImpl` 。



2. 编码基础规范 

* 
**JDK 版本**：必须使用 JDK 8 或以上版本 。


* 
**字符编码**：所有源码文件、数据库配置必须统一使用 **UTF-8** 。


* 
**资源关闭**：所有 JDBC 资源必须使用 `try-catch-finally` 结构手动关闭，严防连接泄漏 。



## 3. 开发约束要求

3.1 实体类 (PO) 

* 属性名必须与数据库字段名保持一致。
* 必须提供对应的 `Getter/Setter` 方法。
* 必须重写 `toString()` 方法，用于控制台数据的标准化展示 。



3.2 数据库操作 (DAO) 

* 接口定义数据访问的通用方法 。


* SQL 语句应通过 `PreparedStatement` 执行，防止 SQL 注入并提高性能 。



3.3 界面交互 (View) 

* 使用 `java.util.Scanner` 获取用户控制台输入 。


* 交互流程需包含必要的确认操作（如删除前的 `y/n` 确认） 。

---