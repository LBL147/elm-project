package com.neusoft.elm.dao;

import com.neusoft.elm.po.Business;

import java.util.List;

/**
 * 商家数据访问接口
 * 定义商家相关的数据库操作方法
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public interface BusinessDao {

    /**
     * 查询所有商家或根据条件模糊查询商家
     * 使用 StringBuffer 动态拼接 SQL 实现多条件模糊查询
     *
     * @param businessName 商家名称关键词（可为 null）
     * @param businessAddress 商家地址关键词（可为 null）
     * @return 商家列表
     */
    List<Business> listBusiness(String businessName, String businessAddress);

    /**
     * 新建商家
     * 系统自动生成编号并设定默认密码
     *
     * @param business 商家对象
     * @return 新增成功返回自增主键 businessId，失败返回 0
     */
    int saveBusiness(Business business);

    /**
     * 删除商家
     * 需级联删除该商家下的所有食品，必须使用 JDBC 事务管理
     *
     * @param businessId 商家编号
     * @return 删除成功返回 true，失败返回 false
     */
    boolean removeBusiness(int businessId);

    /**
     * 商家登录验证
     * 根据商家编号和密码查询商家信息
     *
     * @param businessId 商家编号
     * @param password 商家密码
     * @return 如果验证成功返回 Business 对象，否则返回 null
     */
    Business getBusinessByIdByPass(int businessId, String password);

    /**
     * 修改商家信息
     * 更新商家基本资料（不含密码）
     *
     * @param business 商家对象
     * @return 修改成功返回 true，失败返回 false
     */
    boolean updateBusiness(Business business);

    /**
     * 修改商家密码
     * 根据商家编号更新密码
     *
     * @param businessId 商家编号
     * @param newPassword 新密码
     * @return 修改成功返回 true，失败返回 false
     */
    boolean updateBusinessPassword(int businessId, String newPassword);

    /**
     * 根据商家编号查询商家信息
     * 用于查看商家详细信息
     *
     * @param businessId 商家编号
     * @return Business 对象，如果不存在返回 null
     */
    Business getBusinessById(int businessId);
}
