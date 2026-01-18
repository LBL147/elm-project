package com.neusoft.elm.po;

/**
 * 食品实体类 (PO - Persistent Object)
 * 对应数据库表：food
 * 严格遵循 README.md 第 3.2 节字段定义
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class Food {

    // 对应数据库字段
    private Integer foodId;           // 食品编号 (PK, AI)
    private String foodName;          // 食品名称
    private String foodExplain;       // 食品介绍
    private Double foodPrice;         // 食品价格
    private Integer businessId;       // 所属商家编号 (FK)

    // 无参构造方法
    public Food() {
    }

    // 全参构造方法
    public Food(Integer foodId, String foodName, String foodExplain,
                Double foodPrice, Integer businessId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodExplain = foodExplain;
        this.foodPrice = foodPrice;
        this.businessId = businessId;
    }

    // Getter 和 Setter 方法
    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodExplain() {
        return foodExplain;
    }

    public void setFoodExplain(String foodExplain) {
        this.foodExplain = foodExplain;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * 重写 toString() 方法
     * 用于控制台数据的标准化展示
     * 格式要求：包含换行符和字段说明标签
     *
     * @return 格式化的食品信息字符串
     */
    @Override
    public String toString() {
        return "食品编号：" + foodId + "\n" +
               "食品名称：" + foodName + "\n" +
               "食品介绍：" + foodExplain + "\n" +
               "食品价格：¥" + String.format("%.2f", foodPrice) + "\n" +
               "所属商家：" + businessId + "\n" +
               "----------------------------------------";
    }
}
