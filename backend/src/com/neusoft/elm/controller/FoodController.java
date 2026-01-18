package com.neusoft.elm.controller;

import com.neusoft.elm.dao.FoodDao;
import com.neusoft.elm.po.Food;
import com.neusoft.elm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 食品控制器
 * 处理食品相关的 REST API 请求
 *
 * @author Neusoft ELM Team
 * @version 2.0
 */
@RestController
@RequestMapping("/api/food")
public class FoodController {

    private final FoodDao foodDao;

    @Autowired
    public FoodController(FoodDao foodDao) {
        this.foodDao = foodDao;
    }

    /**
     * 查询指定商家的所有食品
     * GET /api/food/business/{businessId}
     *
     * 响应示例:
     * {
     *   "success": true,
     *   "message": "查询成功",
     *   "data": [
     *     {
     *       "foodId": 1,
     *       "foodName": "香辣鸡腿堡",
     *       "foodExplain": "经典美味",
     *       "foodPrice": 18.5,
     *       "businessId": 1
     *     }
     *   ]
     * }
     *
     * @param businessId 商家编号
     * @return 食品列表
     */
    @GetMapping("/business/{businessId}")
    public Result<List<Food>> listFoodsByBusiness(@PathVariable int businessId) {
        // 调用 DAO 层查询食品列表
        List<Food> foodList = foodDao.listFoodByBusinessId(businessId);

        if (foodList != null && !foodList.isEmpty()) {
            return Result.ok("查询成功，共 " + foodList.size() + " 个食品", foodList);
        } else {
            return Result.ok("该商家暂无食品", foodList);
        }
    }

    /**
     * 根据食品编号查询食品信息
     * GET /api/food/{id}
     *
     * @param id 食品编号
     * @return 食品详细信息
     */
    @GetMapping("/{id}")
    public Result<Food> getFoodById(@PathVariable int id) {
        // 调用 DAO 层查询食品信息
        Food food = foodDao.getFoodById(id);

        if (food != null) {
            return Result.ok("查询成功", food);
        } else {
            return Result.fail("食品信息不存在");
        }
    }

    /**
     * 新增食品
     * POST /api/food
     *
     * 请求体示例:
     * {
     *   "foodName": "新奥尔良烤翅",
     *   "foodExplain": "香辣可口",
     *   "foodPrice": 15.0,
     *   "businessId": 2
     * }
     *
     * 响应示例:
     * {
     *   "success": true,
     *   "message": "新增食品成功",
     *   "data": {
     *     "foodId": 10
     *   }
     * }
     *
     * @param food 食品对象
     * @return 新增的食品 ID
     */
    @PostMapping
    public Result<Map<String, Object>> createFood(@RequestBody Food food) {
        // 参数验证
        if (food.getFoodName() == null || food.getFoodName().trim().isEmpty()) {
            return Result.fail("食品名称不能为空");
        }
        if (food.getFoodPrice() == null || food.getFoodPrice() <= 0) {
            return Result.fail("食品价格必须大于 0");
        }
        if (food.getBusinessId() == null || food.getBusinessId() <= 0) {
            return Result.fail("所属商家编号无效");
        }

        // 调用 DAO 层保存食品
        int foodId = foodDao.saveFood(food);

        if (foodId > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("foodId", foodId);
            return Result.ok("新增食品成功", data);
        } else {
            return Result.fail("新增食品失败，请重试");
        }
    }

    /**
     * 修改食品
     * PUT /api/food/{id}
     *
     * 请求体示例:
     * {
     *   "foodName": "香辣鸡腿堡(升级版)",
     *   "foodExplain": "更加美味",
     *   "foodPrice": 20.0
     * }
     *
     * @param id 食品编号
     * @param food 食品对象
     * @return 修改结果
     */
    @PutMapping("/{id}")
    public Result<Void> updateFood(@PathVariable int id, @RequestBody Food food) {
        // 参数验证
        if (food.getFoodName() == null || food.getFoodName().trim().isEmpty()) {
            return Result.fail("食品名称不能为空");
        }
        if (food.getFoodPrice() == null || food.getFoodPrice() <= 0) {
            return Result.fail("食品价格必须大于 0");
        }

        // 设置食品编号（确保与路径参数一致）
        food.setFoodId(id);

        // 调用 DAO 层修改食品
        boolean result = foodDao.updateFood(food);

        if (result) {
            return Result.ok("修改食品成功");
        } else {
            return Result.fail("修改食品失败，请检查食品编号是否正确");
        }
    }

    /**
     * 删除食品
     * DELETE /api/food/{id}
     *
     * 注意：此操作是单独删除食品，不涉及事务处理。
     * 商家级联删除的事务处理在 AdminController.deleteBusiness() 中已实现。
     *
     * @param id 食品编号
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteFood(@PathVariable int id) {
        // 调用 DAO 层删除食品
        boolean result = foodDao.removeFood(id);

        if (result) {
            return Result.ok("删除食品成功");
        } else {
            return Result.fail("删除食品失败，请检查食品编号是否正确");
        }
    }

    /**
     * 权限验证：检查食品是否属于指定商家
     * GET /api/food/{id}/verify?businessId={businessId}
     *
     * 用于前端在修改/删除食品前验证权限
     *
     * @param id 食品编号
     * @param businessId 商家编号
     * @return 验证结果
     */
    @GetMapping("/{id}/verify")
    public Result<Map<String, Object>> verifyFoodOwnership(
            @PathVariable int id,
            @RequestParam int businessId) {

        // 查询食品信息
        Food food = foodDao.getFoodById(id);

        if (food == null) {
            return Result.fail("食品不存在");
        }

        // 验证所有权
        boolean isOwner = food.getBusinessId().equals(businessId);
        Map<String, Object> data = new HashMap<>();
        data.put("isOwner", isOwner);
        data.put("foodName", food.getFoodName());

        if (isOwner) {
            return Result.ok("验证通过", data);
        } else {
            return Result.fail("该食品不属于您的商家，无权操作");
        }
    }
}
