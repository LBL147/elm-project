package com.neusoft.elm.view.impl;

import com.neusoft.elm.dao.FoodDao;
import com.neusoft.elm.dao.impl.FoodDaoImpl;
import com.neusoft.elm.po.Food;
import com.neusoft.elm.view.FoodView;

import java.util.List;
import java.util.Scanner;

/**
 * 食品视图实现类
 * 实现 FoodView 接口，负责食品管理的控制台交互逻辑
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class FoodViewImpl implements FoodView {

    // 声明 FoodDao 接口（面向接口编程）
    private FoodDao foodDao = new FoodDaoImpl();

    // Scanner 对象用于获取控制台输入
    private Scanner input = new Scanner(System.in);

    /**
     * 显示指定商家的所有食品列表
     * 查询并展示当前商家名下的所有食品
     *
     * @param businessId 商家编号
     */
    @Override
    public void listFood(int businessId) {
        System.out.println("\n========== 食品列表 ==========");

        // 调用 DAO 层查询食品列表
        List<Food> foodList = foodDao.listFoodByBusinessId(businessId);

        // 判断查询结果
        if (foodList == null || foodList.isEmpty()) {
            System.out.println("暂无食品数据！");
            return;
        }

        // 输出表头
        System.out.println("食品编号\t食品名称\t\t食品介绍\t\t\t食品价格");
        System.out.println("------------------------------------------------------------");

        // 遍历输出食品信息
        for (Food food : foodList) {
            System.out.println(
                food.getFoodId() + "\t\t" +
                food.getFoodName() + "\t\t" +
                (food.getFoodExplain() != null ? food.getFoodExplain() : "无") + "\t\t" +
                String.format("¥%.2f", food.getFoodPrice())
            );
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("共查询到 " + foodList.size() + " 个食品。");
    }

    /**
     * 新增食品
     * 为当前商家添加新菜品
     *
     * @param businessId 商家编号
     */
    @Override
    public void saveFood(int businessId) {
        System.out.println("\n========== 新增食品 ==========");

        // 获取用户输入
        System.out.print("请输入食品名称：");
        String foodName = input.next();

        System.out.print("请输入食品介绍：");
        input.nextLine(); // 消费换行符
        String foodExplain = input.nextLine();

        System.out.print("请输入食品价格：");
        double foodPrice = input.nextDouble();

        // 创建 Food 对象
        Food food = new Food();
        food.setFoodName(foodName);
        food.setFoodExplain(foodExplain);
        food.setFoodPrice(foodPrice);
        food.setBusinessId(businessId);

        // 调用 DAO 层保存食品
        int foodId = foodDao.saveFood(food);

        // 判断操作结果
        if (foodId > 0) {
            System.out.println("\n✅ 新增食品成功！");
            System.out.println("食品编号：" + foodId);
        } else {
            System.out.println("\n❌ 新增食品失败，请重试！");
        }
    }

    /**
     * 修改食品
     * 更新已有菜品的价格或描述
     *
     * @param businessId 商家编号
     */
    @Override
    public void updateFood(int businessId) {
        System.out.println("\n========== 修改食品 ==========");

        // 先显示当前商家的食品列表
        listFood(businessId);

        // 获取用户输入
        System.out.print("\n请输入要修改的食品编号：");
        int foodId = input.nextInt();

        // 查询食品信息（用于验证是否属于当前商家）
        Food oldFood = foodDao.getFoodById(foodId);
        if (oldFood == null) {
            System.out.println("\n❌ 食品编号不存在！");
            return;
        }

        if (oldFood.getBusinessId() != businessId) {
            System.out.println("\n❌ 该食品不属于您的商家，无法修改！");
            return;
        }

        // 显示原有信息
        System.out.println("\n当前食品信息：");
        System.out.println("食品名称：" + oldFood.getFoodName());
        System.out.println("食品介绍：" + oldFood.getFoodExplain());
        System.out.println("食品价格：¥" + String.format("%.2f", oldFood.getFoodPrice()));

        // 获取新的食品信息
        System.out.print("\n请输入新的食品名称：");
        String foodName = input.next();

        System.out.print("请输入新的食品介绍：");
        input.nextLine(); // 消费换行符
        String foodExplain = input.nextLine();

        System.out.print("请输入新的食品价格：");
        double foodPrice = input.nextDouble();

        // 二次确认
        System.out.print("确认要修改吗？(y/n)：");
        String confirm = input.next();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("已取消修改操作。");
            return;
        }

        // 创建 Food 对象
        Food food = new Food();
        food.setFoodId(foodId);
        food.setFoodName(foodName);
        food.setFoodExplain(foodExplain);
        food.setFoodPrice(foodPrice);

        // 调用 DAO 层修改食品
        boolean result = foodDao.updateFood(food);

        // 判断操作结果
        if (result) {
            System.out.println("\n✅ 修改食品成功！");
        } else {
            System.out.println("\n❌ 修改食品失败，请重试！");
        }
    }

    /**
     * 删除食品
     * 移除指定菜品
     *
     * @param businessId 商家编号
     */
    @Override
    public void removeFood(int businessId) {
        System.out.println("\n========== 删除食品 ==========");

        // 先显示当前商家的食品列表
        listFood(businessId);

        // 获取用户输入
        System.out.print("\n请输入要删除的食品编号：");
        int foodId = input.nextInt();

        // 查询食品信息（用于验证是否属于当前商家）
        Food food = foodDao.getFoodById(foodId);
        if (food == null) {
            System.out.println("\n❌ 食品编号不存在！");
            return;
        }

        if (food.getBusinessId() != businessId) {
            System.out.println("\n❌ 该食品不属于您的商家，无法删除！");
            return;
        }

        // 二次确认
        System.out.print("确认删除食品【" + food.getFoodName() + "】吗？(y/n)：");
        String confirm = input.next();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("已取消删除操作。");
            return;
        }

        // 调用 DAO 层删除食品
        boolean result = foodDao.removeFood(foodId);

        // 判断操作结果
        if (result) {
            System.out.println("\n✅ 删除食品成功！");
        } else {
            System.out.println("\n❌ 删除食品失败，请重试！");
        }
    }

    /**
     * 食品管理二级菜单
     * 进入食品管理模块，包含食品列表、新增、修改、删除功能
     *
     * @param businessId 商家编号
     */
    @Override
    public void foodMenu(int businessId) {
        boolean isRunning = true;

        while (isRunning) {
            // 显示二级菜单
            System.out.println("\n========== 所属商品管理 ==========");
            System.out.println("1. 食品列表");
            System.out.println("2. 新增食品");
            System.out.println("3. 修改食品");
            System.out.println("4. 删除食品");
            System.out.println("5. 返回上一级菜单");
            System.out.println("====================================");
            System.out.print("请选择功能（1-5）：");

            // 获取用户选择
            int choice = input.nextInt();

            // 根据选择执行对应功能
            switch (choice) {
                case 1:
                    // 食品列表
                    listFood(businessId);
                    break;
                case 2:
                    // 新增食品
                    saveFood(businessId);
                    break;
                case 3:
                    // 修改食品
                    updateFood(businessId);
                    break;
                case 4:
                    // 删除食品
                    removeFood(businessId);
                    break;
                case 5:
                    // 返回上一级菜单
                    System.out.println("返回商家主菜单...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("\n❌ 无效选择，请输入 1-5 之间的数字！");
                    break;
            }
        }
    }
}
