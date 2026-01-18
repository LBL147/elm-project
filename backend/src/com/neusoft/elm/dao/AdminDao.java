package com.neusoft.elm.dao;

import com.neusoft.elm.po.Admin;

/**
 * 管理员数据访问接口
 * 定义管理员相关的数据库操作方法
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public interface AdminDao {

    /**
     * 管理员登录验证
     * 根据用户名和密码查询管理员信息
     *
     * @param adminName 管理员用户名
     * @param password 管理员密码
     * @return 如果验证成功返回 Admin 对象，否则返回 null
     */
    Admin getAdminByNameByPass(String adminName, String password);
}
