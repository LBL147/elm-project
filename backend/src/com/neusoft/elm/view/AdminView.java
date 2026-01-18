package com.neusoft.elm.view;

/**
 * 管理员视图接口
 * 定义管理员端的控制台交互方法
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public interface AdminView {

    /**
     * 管理员登录方法
     * 通过控制台获取用户输入的用户名和密码，并进行身份验证
     *
     * @return 登录成功返回 true，失败返回 false
     */
    boolean login();
}
