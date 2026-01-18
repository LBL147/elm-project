package com.neusoft.elm.po;

/**
 * 商家实体类 (PO - Persistent Object)
 * 对应数据库表：business
 * 严格遵循 README.md 第 3.1 节字段定义
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class Business {

    // 对应数据库字段
    private Integer businessId;        // 商家编号 (PK, AI)
    private String password;           // 密码 (默认 123)
    private String businessName;       // 商家名称
    private String businessAddress;    // 商家地址
    private String businessExplain;    // 商家介绍
    private Double starPrice;          // 起送费
    private Double deliveryPrice;      // 配送费

    // 无参构造方法
    public Business() {
    }

    // 全参构造方法
    public Business(Integer businessId, String password, String businessName,
                    String businessAddress, String businessExplain,
                    Double starPrice, Double deliveryPrice) {
        this.businessId = businessId;
        this.password = password;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.businessExplain = businessExplain;
        this.starPrice = starPrice;
        this.deliveryPrice = deliveryPrice;
    }

    // Getter 和 Setter 方法
    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessExplain() {
        return businessExplain;
    }

    public void setBusinessExplain(String businessExplain) {
        this.businessExplain = businessExplain;
    }

    public Double getStarPrice() {
        return starPrice;
    }

    public void setStarPrice(Double starPrice) {
        this.starPrice = starPrice;
    }

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    /**
     * 重写 toString() 方法
     * 用于控制台数据的标准化展示
     * 格式要求：包含换行符和字段说明标签
     *
     * @return 格式化的商家信息字符串
     */
    @Override
    public String toString() {
        return "商家编号：" + businessId + "\n" +
               "商家名称：" + businessName + "\n" +
               "商家地址：" + businessAddress + "\n" +
               "商家介绍：" + businessExplain + "\n" +
               "起送费：¥" + String.format("%.2f", starPrice) + "\n" +
               "配送费：¥" + String.format("%.2f", deliveryPrice) + "\n" +
               "----------------------------------------";
    }
}
