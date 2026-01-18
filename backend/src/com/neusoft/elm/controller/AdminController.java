package com.neusoft.elm.controller;

import com.neusoft.elm.dao.AdminDao;
import com.neusoft.elm.dao.BusinessDao;
import com.neusoft.elm.po.Admin;
import com.neusoft.elm.po.Business;
import com.neusoft.elm.vo.LoginRequest;
import com.neusoft.elm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 * 处理管理员相关的 REST API 请求
 *
 * @author Neusoft ELM Team
 * @version 2.0
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminDao adminDao;
    private final BusinessDao businessDao;

    @Autowired
    public AdminController(AdminDao adminDao, BusinessDao businessDao) {
        this.adminDao = adminDao;
        this.businessDao = businessDao;
    }

    /**
     * 管理员登录
     * POST /api/admin/login
     *
     * 请求体示例:
     * {
     *   "username": "admin",
     *   "password": "123456"
     * }
     *
     * 响应示例:
     * {
     *   "success": true,
     *   "message": "登录成功",
     *   "data": {
     *     "token": "admin_token_1",
     *     "type": "admin",
     *     "adminName": "admin"
     *   }
     * }
     *
     * @param loginRequest 登录请求对象
     * @return 统一响应结果
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        // 1. 参数验证
        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            return Result.fail("用户名不能为空");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return Result.fail("密码不能为空");
        }

        // 2. 调用 DAO 层验证登录
        Admin admin = adminDao.getAdminByNameByPass(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        // 3. 判断登录结果
        if (admin != null) {
            // 登录成功，构造响应数据（符合前端期望格式）
            Map<String, Object> data = new HashMap<>();
            data.put("token", "admin_token_" + admin.getAdminId());  // 生成 token
            data.put("type", "admin");  // 用户类型
            data.put("adminId", admin.getAdminId());
            data.put("adminName", admin.getAdminName());

            return Result.ok("登录成功", data);
        } else {
            // 登录失败
            return Result.fail("用户名或密码错误");
        }
    }

    /**
     * 查询所有商家
     * GET /api/admin/businesses
     *
     * 响应示例:
     * {
     *   "success": true,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "businessId": 1,
     *       "businessName": "肯德基",
     *       ...
     *     }
     *   ]
     * }
     *
     * @return 商家列表
     */
    @GetMapping("/businesses")
    public Result<List<Business>> listAllBusinesses(@RequestParam(required = false) String keyword) {
        List<Business> businessList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            businessList = businessDao.listBusiness(keyword, keyword);
        } else {
            // 调用 DAO 层查询所有商家（参数均为 null）
            businessList = businessDao.listBusiness(null, null);
        }

        if (businessList != null && !businessList.isEmpty()) {
            return Result.ok("查询成功，共 " + businessList.size() + " 家商家", businessList);
        } else {
            return Result.ok("暂无商家数据", businessList);
        }
    }

    /**
     * 搜索商家（模糊查询）
     * GET /api/admin/businesses/search?name=肯德基&address=万达
     *
     * @param name 商家名称关键词（可选）
     * @param address 商家地址关键词（可选）
     * @return 商家列表
     */
    @GetMapping("/businesses/search")
    public Result<List<Business>> searchBusinesses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String keyword) {

        if (keyword != null && !keyword.trim().isEmpty()) {
            name = keyword;
            address = keyword;
        }
        // 调用 DAO 层模糊查询（保留原有 StringBuffer 动态 SQL 逻辑）
        List<Business> businessList = businessDao.listBusiness(name, address);

        if (businessList != null && !businessList.isEmpty()) {
            return Result.ok("查询成功，共 " + businessList.size() + " 家商家", businessList);
        } else {
            return Result.ok("未找到符合条件的商家", businessList);
        }
    }

    /**
     * 新建商家
     * POST /api/admin/businesses
     *
     * 请求体示例:
     * {
     *   "businessName": "星巴克",
     *   "businessAddress": "北京市朝阳区",
     *   "businessExplain": "咖啡连锁店",
     *   "starPrice": 20.0,
     *   "deliveryPrice": 5.0
     * }
     *
     * @param business 商家对象
     * @return 新建的商家 ID
     */
    @PostMapping("/businesses")
    public Result<Map<String, Object>> createBusiness(@RequestBody Business business) {
        // 参数验证
        if (business.getBusinessName() == null || business.getBusinessName().trim().isEmpty()) {
            return Result.fail("商家名称不能为空");
        }
        String requestedPassword = business.getPassword();
        if (requestedPassword != null && requestedPassword.trim().isEmpty()) {
            business.setPassword(null);
            requestedPassword = null;
        }

        // 调用 DAO 层保存商家
        int businessId = businessDao.saveBusiness(business);

        if (businessId > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("businessId", businessId);
            data.put("defaultPassword", requestedPassword != null ? requestedPassword : "123");
            return Result.ok("新建商家成功", data);
        } else {
            return Result.fail("新建商家失败，请重试");
        }
    }

    /**
     * 删除商家（级联删除）
     * DELETE /api/admin/businesses/{id}
     *
     * 核心功能：使用 JDBC 事务处理，级联删除商家及其所有食品
     *
     * @param id 商家编号
     * @return 删除结果
     */
    @DeleteMapping("/businesses/{id}")
    public Result<Void> deleteBusiness(@PathVariable int id) {
        // 调用 DAO 层删除商家（保留原有事务处理逻辑）
        boolean result = businessDao.removeBusiness(id);

        if (result) {
            return Result.ok("删除商家成功，该商家及其所有食品已被删除");
        } else {
            return Result.fail("删除商家失败，请检查商家编号是否正确");
        }
    }
}
