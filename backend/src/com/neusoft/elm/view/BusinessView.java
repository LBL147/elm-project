package com.neusoft.elm.view;

import com.neusoft.elm.po.Business;

/**
 * 商家视图接口
 * 定义商家管理相关的控制台交互方法
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public interface BusinessView {

    /**
     * 显示所有商家列表
     * 查询并展示所有注册商家
     */
    void listBusinessAll();

    /**
     * 搜索商家
     * 根据商家名称或地址进行模糊查询
     */
    void listBusiness();

    /**
     * 新建商家
     * 输入商家名称，系统自动生成编号并设定默认密码
     */
    void saveBusiness();

    /**
     * 删除商家
     * 根据编号删除商家，需级联删除该商家下的所有食品
     */
    void removeBusiness();

    /**
     * 商家登录
     * 根据商家编号和密码登录
     *
     * @return 登录成功返回 Business 对象，失败返回 null
     */
    Business login();

    /**
     * 查看商家信息
     * 显示当前商家的详细信息
     *
     * @param businessId 商家编号
     */
    void showBusinessInfo(int businessId);

    /**
     * 修改商家信息
     * 更新商家基本资料（不含密码）
     *
     * @param businessId 商家编号
     */
    void updateBusinessInfo(int businessId);

    /**
     * 修改商家密码
     * 输入新密码进行更新
     *
     * @param businessId 商家编号
     */
    void updatePassword(int businessId);
}
