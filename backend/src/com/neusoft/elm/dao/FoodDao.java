package com.neusoft.elm.dao;

import com.neusoft.elm.po.Food;

import java.util.List;

/**
 * 食品数据访问接口
 * 定义食品相关的数据库操作方法
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public interface FoodDao {

    /**
     * 查询指定商家的所有食品
     * 根据商家编号查询该商家名下的所有食品
     *
     * @param businessId 商家编号
     * @return 食品列表
     */
    List<Food> listFoodByBusinessId(int businessId);

    /**
     * 新增食品
     * 为当前商家添加新菜品
     *
     * @param food 食品对象
     * @return 新增成功返回自增主键 foodId，失败返回 0
     */
    int saveFood(Food food);

    /**
     * 修改食品
     * 更新已有菜品的价格或描述
     *
     * @param food 食品对象
     * @return 修改成功返回 true，失败返回 false
     */
    boolean updateFood(Food food);

    /**
     * 删除食品
     * 移除指定菜品
     *
     * @param foodId 食品编号
     * @return 删除成功返回 true，失败返回 false
     */
    boolean removeFood(int foodId);

    /**
     * 根据食品编号查询食品信息
     * 用于修改前的信息回显
     *
     * @param foodId 食品编号
     * @return Food 对象，如果不存在返回 null
     */
    Food getFoodById(int foodId);
}
