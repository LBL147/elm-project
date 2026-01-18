package com.neusoft.elm.po;

/**
 * 管理员实体类 (PO - Persistent Object)
 * 对应数据库表：admin
 * 严格遵循 README.md 第 3.3 节字段定义
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class Admin {

    // 对应数据库字段
    private Integer adminId;      // 管理员编号 (PK, AI)
    private String adminName;     // 管理员名称 (UQ)
    private String password;      // 密码

    // 无参构造方法
    public Admin() {
    }

    // 全参构造方法
    public Admin(Integer adminId, String adminName, String password) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.password = password;
    }

    // Getter 和 Setter 方法
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 重写 toString() 方法
     * 用于控制台数据的标准化展示
     *
     * @return 格式化的管理员信息字符串
     */
    @Override
    public String toString() {
        return "管理员编号：" + adminId + "\n" +
               "管理员名称：" + adminName + "\n" +
               "----------------------------------------";
    }
}
