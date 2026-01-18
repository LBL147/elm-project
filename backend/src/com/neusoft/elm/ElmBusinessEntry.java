package com.neusoft.elm;

import com.neusoft.elm.po.Business;
import com.neusoft.elm.view.BusinessView;
import com.neusoft.elm.view.FoodView;
import com.neusoft.elm.view.impl.BusinessViewImpl;
import com.neusoft.elm.view.impl.FoodViewImpl;

import java.util.Scanner;

/**
 * 饿了么商家后台管理系统 - 商家端主程序入口
 * 商家端入口类，负责商家登录和主流程控制
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class ElmBusinessEntry {

    // 声明视图层接口
    private BusinessView businessView = new BusinessViewImpl();
    private FoodView foodView = new FoodViewImpl();

    // Scanner 对象用于获取控制台输入
    private Scanner input = new Scanner(System.in);

    // 当前登录的商家对象
    private Business currentBusiness = null;

    /**
     * 系统主入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ElmBusinessEntry entry = new ElmBusinessEntry();
        entry.start();
    }

    /**
     * 系统启动方法
     */
    public void start() {
        System.out.println("========================================");
        System.out.println("    欢迎使用 饿了么商家后台管理系统");
        System.out.println("           (商家端)");
        System.out.println("========================================");

        // 商家登录
        currentBusiness = businessView.login();

        if (currentBusiness != null) {
            // 登录成功，进入主工作流程
            work();
        } else {
            System.out.println("\n登录失败，系统退出。");
        }
    }

    /**
     * 商家端主工作流程
     * 实现一级菜单，包含：
     * 1.查看商家信息
     * 2.修改商家信息
     * 3.修改密码
     * 4.所属商品管理
     * 5.退出系统
     */
    public void work() {
        boolean isRunning = true;

        while (isRunning) {
            // 显示一级菜单
            System.out.println("\n========== 商家功能菜单 ==========");
            System.out.println("1. 查看商家信息");
            System.out.println("2. 修改商家信息");
            System.out.println("3. 修改密码");
            System.out.println("4. 所属商品管理");
            System.out.println("5. 退出系统");
            System.out.println("====================================");
            System.out.print("请选择功能（1-5）：");

            // 获取用户选择
            int choice = input.nextInt();

            // 根据选择执行对应功能
            switch (choice) {
                case 1:
                    // 查看商家信息
                    businessView.showBusinessInfo(currentBusiness.getBusinessId());
                    break;
                case 2:
                    // 修改商家信息
                    businessView.updateBusinessInfo(currentBusiness.getBusinessId());
                    break;
                case 3:
                    // 修改密码
                    businessView.updatePassword(currentBusiness.getBusinessId());
                    break;
                case 4:
                    // 所属商品管理（进入二级菜单）
                    foodView.foodMenu(currentBusiness.getBusinessId());
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
