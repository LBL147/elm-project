package com.neusoft.elm.controller;

import com.neusoft.elm.dao.BusinessDao;
import com.neusoft.elm.po.Business;
import com.neusoft.elm.vo.BusinessPasswordUpdateRequest;
import com.neusoft.elm.vo.LoginRequest;
import com.neusoft.elm.vo.PasswordRequest;
import com.neusoft.elm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 商家控制器
 * 处理商家相关的 REST API 请求
 *
 * @author Neusoft ELM Team
 * @version 2.0
 */
@RestController
@RequestMapping("/api/business")
public class BusinessController {

    private final BusinessDao businessDao;

    @Autowired
    public BusinessController(BusinessDao businessDao) {
        this.businessDao = businessDao;
    }

    /**
     * 商家登录
     * POST /api/business/login
     *
     * 请求体示例:
     * {
     *   "username": "2",  // 商家编号
     *   "password": "123"
     * }
     *
     * 响应示例:
     * {
     *   "success": true,
     *   "message": "登录成功",
     *   "data": {
     *     "token": "business_token_2",
     *     "type": "business",
     *     "businessId": 2,
     *     "businessName": "肯德基(中关村店)"
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
            return Result.fail("商家编号不能为空");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return Result.fail("密码不能为空");
        }

        // 2. 解析商家编号（前端传的是字符串）
        int businessId;
        try {
            businessId = Integer.parseInt(loginRequest.getUsername());
        } catch (NumberFormatException e) {
            return Result.fail("商家编号格式错误");
        }

        // 3. 调用 DAO 层验证登录
        Business business = businessDao.getBusinessByIdByPass(businessId, loginRequest.getPassword());

        // 4. 判断登录结果
        if (business != null) {
            // 登录成功，构造响应数据（符合前端期望格式）
            Map<String, Object> data = new HashMap<>();
            data.put("token", "business_token_" + business.getBusinessId());  // 生成 token
            data.put("type", "business");  // 用户类型
            data.put("businessId", business.getBusinessId());
            data.put("businessName", business.getBusinessName());

            return Result.ok("登录成功", data);
        } else {
            // 登录失败
            return Result.fail("商家编号或密码错误");
        }
    }

    /**
     * 查看商家信息
     * GET /api/business/{id}
     *
     * @param id 商家编号
     * @return 商家详细信息
     */
    @GetMapping("/{id}")
    public Result<Business> getBusinessInfo(@PathVariable int id) {
        // 调用 DAO 层查询商家信息
        Business business = businessDao.getBusinessById(id);

        if (business != null) {
            return Result.ok("查询成功", business);
        } else {
            return Result.fail("商家信息不存在");
        }
    }

    /**
     * 修改商家信息
     * PUT /api/business/{id}
     *
     * 请求体示例:
     * {
     *   "businessName": "肯德基(更新后)",
     *   "businessAddress": "北京市海淀区",
     *   "businessExplain": "快餐连锁",
     *   "starPrice": 25.0,
     *   "deliveryPrice": 6.0
     * }
     *
     * @param id 商家编号
     * @param business 商家对象
     * @return 修改结果
     */
    @PutMapping("/{id}")
    public Result<Void> updateBusinessInfo(@PathVariable int id, @RequestBody Business business) {
        // 参数验证
        if (business.getBusinessName() == null || business.getBusinessName().trim().isEmpty()) {
            return Result.fail("商家名称不能为空");
        }

        // 设置商家编号（确保与路径参数一致）
        business.setBusinessId(id);

        // 调用 DAO 层修改商家信息
        boolean result = businessDao.updateBusiness(business);

        if (result) {
            return Result.ok("修改商家信息成功");
        } else {
            return Result.fail("修改商家信息失败，请重试");
        }
    }

    /**
     * 修改商家密码
     * PUT /api/business/{id}/password
     *
     * 请求体示例:
     * {
     *   "newPassword": "newpass123",
     *   "confirmPassword": "newpass123"
     * }
     *
     * @param id 商家编号
     * @param passwordRequest 密码请求对象
     * @return 修改结果
     */
    @PutMapping("/{id}/password")
    public Result<Void> updatePassword(@PathVariable int id, @RequestBody PasswordRequest passwordRequest) {
        // 参数验证
        if (passwordRequest.getNewPassword() == null || passwordRequest.getNewPassword().trim().isEmpty()) {
            return Result.fail("新密码不能为空");
        }

        // 验证两次密码是否一致（如果前端传了 confirmPassword）
        if (passwordRequest.getConfirmPassword() != null
            && !passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
            return Result.fail("两次密码输入不一致");
        }

        // 调用 DAO 层修改密码
        boolean result = businessDao.updateBusinessPassword(id, passwordRequest.getNewPassword());

        if (result) {
            return Result.ok("修改密码成功，下次登录请使用新密码");
        } else {
            return Result.fail("修改密码失败，请重试");
        }
    }

    /**
     * 修改商家密码（校验旧密码）
     * POST /api/business/updatePassword
     *
     * 请求体示例:
     * {
     *   "businessId": 2,
     *   "oldPassword": "123",
     *   "newPassword": "newpass123",
     *   "confirmPassword": "newpass123"
     * }
     *
     * @param request 密码修改请求对象
     * @return 修改结果
     */
    @PostMapping("/updatePassword")
    public Result<Void> updatePasswordWithOld(@RequestBody BusinessPasswordUpdateRequest request) {
        if (request.getBusinessId() == null || request.getBusinessId() <= 0) {
            return Result.fail("商家编号无效");
        }
        if (request.getOldPassword() == null || request.getOldPassword().trim().isEmpty()) {
            return Result.fail("旧密码不能为空");
        }
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            return Result.fail("新密码不能为空");
        }
        if (request.getConfirmPassword() != null
            && !request.getNewPassword().equals(request.getConfirmPassword())) {
            return Result.fail("两次密码输入不一致");
        }

        Business business = businessDao.getBusinessByIdByPass(
            request.getBusinessId(), request.getOldPassword()
        );
        if (business == null) {
            return Result.fail("旧密码错误");
        }

        boolean result = businessDao.updateBusinessPassword(
            request.getBusinessId(), request.getNewPassword()
        );
        if (result) {
            return Result.ok("修改密码成功，下次登录请使用新密码");
        } else {
            return Result.fail("修改密码失败，请重试");
        }
    }
}
