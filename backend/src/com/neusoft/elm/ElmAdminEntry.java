package com.neusoft.elm;

import com.neusoft.elm.view.AdminView;
import com.neusoft.elm.view.BusinessView;
import com.neusoft.elm.view.impl.AdminViewImpl;
import com.neusoft.elm.view.impl.BusinessViewImpl;

import java.util.Scanner;

/**
 * 饿了么商家后台管理系统 - 管理员端主程序入口
 * 项目主入口类，负责系统启动和主流程控制
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class ElmAdminEntry {

    // 声明视图层接口
    private AdminView adminView = new AdminViewImpl();
    private BusinessView businessView = new BusinessViewImpl();

    // Scanner 对象用于获取控制台输入
    private Scanner input = new Scanner(System.in);

    /**
     * 系统主入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ElmAdminEntry entry = new ElmAdminEntry();
        entry.start();
    }

    /**
     * 系统启动方法
     */
    public void start() {
        System.out.println("========================================");
        System.out.println("    欢迎使用 饿了么商家后台管理系统");
        System.out.println("========================================");

        // 管理员登录
        if (adminView.login()) {
            // 登录成功，进入主工作流程
            work();
        } else {
            System.out.println("\n登录失败，系统退出。");
        }
    }

    /**
     * 管理员端主工作流程
     * 实现主循环菜单，包含：
     * 1.所有商家列表
     * 2.搜索商家
     * 3.新建商家
     * 4.删除商家
     * 5.退出系统
     */
    public void work() {
        boolean isRunning = true;

        while (isRunning) {
            // 显示菜单
            System.out.println("\n========== 管理员功能菜单 ==========");
            System.out.println("1. 所有商家列表");
            System.out.println("2. 搜索商家");
            System.out.println("3. 新建商家");
            System.out.println("4. 删除商家");
            System.out.println("5. 退出系统");
            System.out.println("====================================");
            System.out.print("请选择功能（1-5）：");

            // 获取用户选择
            int choice = input.nextInt();

            // 根据选择执行对应功能
            switch (choice) {
                case 1:
                    // 所有商家列表
                    businessView.listBusinessAll();
                    break;
                case 2:
                    // 搜索商家
                    businessView.listBusiness();
                    break;
                case 3:
                    // 新建商家
                    businessView.saveBusiness();
                    break;
                case 4:
                    // 删除商家
                    businessView.removeBusiness();
                    break;
                case 5:
                    // 退出系统
                    System.out.println("\n感谢使用！再见！");
                    isRunning = false;
                    break;
                default:
                    System.out.println("\n❌ 无效选择，请输入 1-5 之间的数字！");
                    break;
            }
        }

        // 关闭 Scanner 资源
        input.close();
    }
}
