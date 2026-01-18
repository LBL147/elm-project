package com.neusoft.elm.view.impl;

import com.neusoft.elm.dao.AdminDao;
import com.neusoft.elm.dao.impl.AdminDaoImpl;
import com.neusoft.elm.po.Admin;
import com.neusoft.elm.view.AdminView;

import java.util.Scanner;

/**
 * 管理员视图实现类
 * 实现 AdminView 接口，负责管理员端的控制台交互逻辑
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class AdminViewImpl implements AdminView {

    // 声明 AdminDao 接口（面向接口编程）
    private AdminDao adminDao = new AdminDaoImpl();

    // Scanner 对象用于获取控制台输入
    private Scanner input = new Scanner(System.in);

    /**
     * 管理员登录方法
     * 通过控制台获取用户输入的用户名和密码，并进行身份验证
     *
     * @return 登录成功返回 true，失败返回 false
     */
    @Override
    public boolean login() {
        System.out.println("\n========== 管理员登录 ==========");

        // 1. 获取用户输入
        System.out.print("请输入管理员用户名：");
        String adminName = input.next();

        System.out.print("请输入密码：");
        String password = input.next();

        // 2. 调用 DAO 层进行验证
        Admin admin = adminDao.getAdminByNameByPass(adminName, password);

        // 3. 判断登录结果
        if (admin != null) {
            System.out.println("\n✅ 登录成功！欢迎您，" + admin.getAdminName() + " 管理员！");
            return true;
        } else {
            System.out.println("\n❌ 登录失败！用户名或密码错误，请重试。");
            return false;
        }
    }

    /**
     * 测试登录功能
     * 运行此 main 方法可直接测试管理员登录
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        AdminView adminView = new AdminViewImpl();
        adminView.login();
    }
}
