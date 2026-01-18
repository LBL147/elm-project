package com.neusoft.elm;

import com.neusoft.elm.dao.BusinessDao;
import com.neusoft.elm.dao.FoodDao;
import com.neusoft.elm.dao.impl.BusinessDaoImpl;
import com.neusoft.elm.dao.impl.FoodDaoImpl;
import com.neusoft.elm.po.Business;
import com.neusoft.elm.po.Food;

import java.util.List;

/**
 * 系统自动化测试工具
 * 用于验证管理员端和商家端的核心功能
 */
public class SystemTest {

    private static BusinessDao businessDao = new BusinessDaoImpl();
    private static FoodDao foodDao = new FoodDaoImpl();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   饿了么商家后台管理系统 - 自动化测试");
        System.out.println("========================================\n");

        // 测试前数据快照
        System.out.println("【测试前数据快照】");
        showAllBusinesses();
        showAllFoods();
        checkBusinessAndFoods(1);

        System.out.println("\n========================================");
        System.out.println("开始执行核心功能测试...");
        System.out.println("========================================\n");

        // 测试1: 查询所有商家
        test1_ListAllBusiness();

        // 测试2: 模糊查询商家
        test2_SearchBusiness();

        // 测试3: 级联删除测试（核心挑战）
        test3_CascadeDelete();

        // 测试后验证
        System.out.println("\n【测试后数据验证】");
        checkBusinessAndFoods(1);

        System.out.println("\n========================================");
        System.out.println("   测试完成！");
        System.out.println("========================================");
    }

    /**
     * 测试1: 查询所有商家
     */
    private static void test1_ListAllBusiness() {
        System.out.println("【测试1】查询所有商家");
        List<Business> list = businessDao.listBusiness(null, null);

        if (list != null && !list.isEmpty()) {
            System.out.println("✅ 查询成功！共查询到 " + list.size() + " 家商家");
            System.out.println("\n商家编号\t商家名称\t\t商家地址\t\t\t起送费\t配送费");
            System.out.println("------------------------------------------------------------------------");
            for (Business b : list) {
                System.out.printf("%d\t\t%s\t%s\t%.2f\t%.2f\n",
                    b.getBusinessId(),
                    b.getBusinessName(),
                    b.getBusinessAddress(),
                    b.getStarPrice(),
                    b.getDeliveryPrice()
                );
            }
        } else {
            System.out.println("❌ 查询失败或无数据");
        }
        System.out.println();
    }

    /**
     * 测试2: 模糊查询商家（关键词：肯德基）
     */
    private static void test2_SearchBusiness() {
        System.out.println("【测试2】模糊查询商家（关键词：肯德基）");
        List<Business> list = businessDao.listBusiness("肯德基", null);

        if (list != null && !list.isEmpty()) {
            System.out.println("✅ 模糊查询成功！共查询到 " + list.size() + " 家商家");
            for (Business b : list) {
                System.out.println("  - " + b.getBusinessName() + " (编号: " + b.getBusinessId() + ")");
            }
        } else {
            System.out.println("❌ 未找到匹配的商家");
        }
        System.out.println();
    }

    /**
     * 测试3: 级联删除测试（删除商家ID=1）
     */
    private static void test3_CascadeDelete() {
        System.out.println("【测试3】级联删除测试（删除商家 ID=1）");

        // 删除前检查
        System.out.println("删除前：");
        Business beforeBusiness = businessDao.getBusinessById(1);
        List<Food> beforeFoods = foodDao.listFoodByBusinessId(1);

        if (beforeBusiness != null) {
            System.out.println("  商家: " + beforeBusiness.getBusinessName());
            System.out.println("  该商家的食品数量: " + (beforeFoods != null ? beforeFoods.size() : 0));
        } else {
            System.out.println("  ⚠️ 商家 ID=1 不存在（可能已被删除）");
            return;
        }

        // 执行删除操作
        System.out.println("\n正在执行级联删除...");
        boolean result = businessDao.removeBusiness(1);

        if (result) {
            System.out.println("✅ 删除操作执行成功！");
        } else {
            System.out.println("❌ 删除操作失败！");
        }

        // 删除后验证
        System.out.println("\n删除后验证：");
        Business afterBusiness = businessDao.getBusinessById(1);
        List<Food> afterFoods = foodDao.listFoodByBusinessId(1);

        if (afterBusiness == null) {
            System.out.println("  ✅ 商家 ID=1 已被成功删除");
        } else {
            System.out.println("  ❌ 商家 ID=1 仍然存在（删除失败）");
        }

        if (afterFoods == null || afterFoods.isEmpty()) {
            System.out.println("  ✅ 商家 ID=1 的所有食品已被级联删除");
        } else {
            System.out.println("  ❌ 商家 ID=1 的食品未被级联删除（剩余 " + afterFoods.size() + " 个）");
        }

        System.out.println("\n【级联删除测试结论】");
        if (afterBusiness == null && (afterFoods == null || afterFoods.isEmpty())) {
            System.out.println("  ✅✅✅ JDBC 事务处理成功！级联删除功能正常！");
        } else {
            System.out.println("  ❌❌❌ JDBC 事务处理失败！请检查代码！");
        }
    }

    /**
     * 显示所有商家
     */
    private static void showAllBusinesses() {
        List<Business> list = businessDao.listBusiness(null, null);
        System.out.println("当前商家总数: " + (list != null ? list.size() : 0));
    }

    /**
     * 显示所有食品
     */
    private static void showAllFoods() {
        // 统计所有商家的食品
        List<Business> businesses = businessDao.listBusiness(null, null);
        int totalFoods = 0;
        if (businesses != null) {
            for (Business b : businesses) {
                List<Food> foods = foodDao.listFoodByBusinessId(b.getBusinessId());
                totalFoods += (foods != null ? foods.size() : 0);
            }
        }
        System.out.println("当前食品总数: " + totalFoods);
    }

    /**
     * 检查指定商家及其食品
     */
    private static void checkBusinessAndFoods(int businessId) {
        Business business = businessDao.getBusinessById(businessId);
        List<Food> foods = foodDao.listFoodByBusinessId(businessId);

        System.out.println("\n商家 ID=" + businessId + " 的数据:");
        if (business != null) {
            System.out.println("  商家存在: " + business.getBusinessName());
            System.out.println("  食品数量: " + (foods != null ? foods.size() : 0));
            if (foods != null && !foods.isEmpty()) {
                for (Food f : foods) {
                    System.out.println("    - " + f.getFoodName() + " (¥" + String.format("%.2f", f.getFoodPrice()) + ")");
                }
            }
        } else {
            System.out.println("  商家不存在");
            System.out.println("  食品数量: " + (foods != null ? foods.size() : 0));
        }
    }
}
