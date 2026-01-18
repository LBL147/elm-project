package com.neusoft.elm.view;

/**
 * 食品视图接口
 * 定义食品管理相关的控制台交互方法
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public interface FoodView {

    /**
     * 显示指定商家的所有食品列表
     * 查询并展示当前商家名下的所有食品
     *
     * @param businessId 商家编号
     */
    void listFood(int businessId);

    /**
     * 新增食品
     * 为当前商家添加新菜品
     *
     * @param businessId 商家编号
     */
    void saveFood(int businessId);

    /**
     * 修改食品
     * 更新已有菜品的价格或描述
     *
     * @param businessId 商家编号
     */
    void updateFood(int businessId);

    /**
     * 删除食品
     * 移除指定菜品
     *
     * @param businessId 商家编号
     */
    void removeFood(int businessId);

    /**
     * 食品管理二级菜单
     * 进入食品管理模块，包含食品列表、新增、修改、删除功能
     *
     * @param businessId 商家编号
     */
    void foodMenu(int businessId);
}
