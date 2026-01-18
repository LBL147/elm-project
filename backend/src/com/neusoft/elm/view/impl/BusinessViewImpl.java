package com.neusoft.elm.view.impl;

import com.neusoft.elm.dao.BusinessDao;
import com.neusoft.elm.dao.impl.BusinessDaoImpl;
import com.neusoft.elm.po.Business;
import com.neusoft.elm.view.BusinessView;

import java.util.List;
import java.util.Scanner;

/**
 * 商家视图实现类
 * 实现 BusinessView 接口，负责商家管理的控制台交互逻辑
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class BusinessViewImpl implements BusinessView {

    // 声明 BusinessDao 接口（面向接口编程）
    private BusinessDao businessDao = new BusinessDaoImpl();

    // Scanner 对象用于获取控制台输入
    private Scanner input = new Scanner(System.in);

    /**
     * 显示所有商家列表
     * 查询并展示所有注册商家
     */
    @Override
    public void listBusinessAll() {
        System.out.println("\n========== 所有商家列表 ==========");

        // 调用 DAO 层查询所有商家（参数均为 null）
        List<Business> businessList = businessDao.listBusiness(null, null);

        // 判断查询结果
        if (businessList == null || businessList.isEmpty()) {
            System.out.println("暂无商家数据！");
            return;
        }

        // 输出表头（严格遵守输出格式要求）
        System.out.println("商家编号\t商家名称\t\t商家地址\t\t\t商家介绍\t\t起送费\t配送费");
        System.out.println("------------------------------------------------------------------------");

        // 遍历输出商家信息
        for (Business business : businessList) {
            System.out.println(
                business.getBusinessId() + "\t\t" +
                business.getBusinessName() + "\t\t" +
                business.getBusinessAddress() + "\t" +
                business.getBusinessExplain() + "\t" +
                String.format("%.2f", business.getStarPrice()) + "\t" +
                String.format("%.2f", business.getDeliveryPrice())
            );
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("共查询到 " + businessList.size() + " 家商家。");
    }

    /**
     * 搜索商家
     * 根据商家名称或地址进行模糊查询
     */
    @Override
    public void listBusiness() {
        System.out.println("\n========== 搜索商家 ==========");

        String businessName = null;
        String businessAddress = null;

        // 询问是否需要输入商家名称关键词
        System.out.print("是否需要输入商家名称关键词(y/n)：");
        String choice1 = input.next();
        if ("y".equalsIgnoreCase(choice1)) {
            System.out.print("请输入商家名称关键词：");
            businessName = input.next();
        }

        // 询问是否需要输入商家地址关键词
        System.out.print("是否需要输入商家地址关键词(y/n)：");
        String choice2 = input.next();
        if ("y".equalsIgnoreCase(choice2)) {
            System.out.print("请输入商家地址关键词：");
            businessAddress = input.next();
        }

        // 调用 DAO 层进行模糊查询
        List<Business> businessList = businessDao.listBusiness(businessName, businessAddress);

        // 判断查询结果
        if (businessList == null || businessList.isEmpty()) {
            System.out.println("未找到符合条件的商家！");
            return;
        }

        // 输出表头（严格遵守输出格式要求）
        System.out.println("\n商家编号\t商家名称\t\t商家地址\t\t\t商家介绍\t\t起送费\t配送费");
        System.out.println("------------------------------------------------------------------------");

        // 遍历输出商家信息
        for (Business business : businessList) {
            System.out.println(
                business.getBusinessId() + "\t\t" +
                business.getBusinessName() + "\t\t" +
                business.getBusinessAddress() + "\t" +
                business.getBusinessExplain() + "\t" +
                String.format("%.2f", business.getStarPrice()) + "\t" +
                String.format("%.2f", business.getDeliveryPrice())
            );
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("共查询到 " + businessList.size() + " 家商家。");
    }

    /**
     * 新建商家
     * 输入商家名称，系统自动生成编号并设定默认密码
     */
    @Override
    public void saveBusiness() {
        System.out.println("\n========== 新建商家 ==========");

        // 获取用户输入
        System.out.print("请输入商家名称：");
        String businessName = input.next();

        System.out.print("请输入商家地址：");
        input.nextLine(); // 消费换行符
        String businessAddress = input.nextLine();

        System.out.print("请输入商家介绍：");
        String businessExplain = input.nextLine();

        System.out.print("请输入起送费：");
        double starPrice = input.nextDouble();

        System.out.print("请输入配送费：");
        double deliveryPrice = input.nextDouble();

        // 创建 Business 对象
        Business business = new Business();
        business.setBusinessName(businessName);
        business.setBusinessAddress(businessAddress);
        business.setBusinessExplain(businessExplain);
        business.setStarPrice(starPrice);
        business.setDeliveryPrice(deliveryPrice);

        // 调用 DAO 层保存商家
        int businessId = businessDao.saveBusiness(business);

        // 判断操作结果
        if (businessId > 0) {
            System.out.println("\n✅ 新建商家成功！");
            System.out.println("商家编号：" + businessId);
            System.out.println("默认密码：123");
        } else {
            System.out.println("\n❌ 新建商家失败，请重试！");
        }
    }

    /**
     * 删除商家
     * 根据编号删除商家，需级联删除该商家下的所有食品
     */
    @Override
    public void removeBusiness() {
        System.out.println("\n========== 删除商家 ==========");

        // 获取用户输入
        System.out.print("请输入要删除的商家编号：");
        int businessId = input.nextInt();

        // 二次确认
        System.out.print("确认删除商家编号为 " + businessId + " 的商家及其所有食品吗？(y/n)：");
        String confirm = input.next();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("已取消删除操作。");
            return;
        }

        // 调用 DAO 层删除商家（使用事务处理）
        boolean result = businessDao.removeBusiness(businessId);

        // 判断操作结果
        if (result) {
            System.out.println("\n✅ 删除商家成功！该商家及其所有食品已被删除。");
        } else {
            System.out.println("\n❌ 删除商家失败，请检查商家编号是否正确！");
        }
    }

    /**
     * 商家登录
     * 根据商家编号和密码登录
     *
     * @return 登录成功返回 Business 对象，失败返回 null
     */
    @Override
    public Business login() {
        System.out.println("\n========== 商家登录 ==========");

        // 1. 获取用户输入
        System.out.print("请输入商家编号：");
        int businessId = input.nextInt();

        System.out.print("请输入密码：");
        String password = input.next();

        // 2. 调用 DAO 层进行验证
        Business business = businessDao.getBusinessByIdByPass(businessId, password);

        // 3. 判断登录结果
        if (business != null) {
            System.out.println("\n✅ 登录成功！欢迎您，" + business.getBusinessName() + "！");
            return business;
        } else {
            System.out.println("\n❌ 登录失败！商家编号或密码错误，请重试。");
            return null;
        }
    }

    /**
     * 查看商家信息
     * 显示当前商家的详细信息
     *
     * @param businessId 商家编号
     */
    @Override
    public void showBusinessInfo(int businessId) {
        System.out.println("\n========== 商家信息 ==========");

        // 调用 DAO 层查询商家信息
        Business business = businessDao.getBusinessById(businessId);

        if (business == null) {
            System.out.println("❌ 商家信息查询失败！");
            return;
        }

        // 显示商家详细信息
        System.out.println("商家编号：" + business.getBusinessId());
        System.out.println("商家名称：" + business.getBusinessName());
        System.out.println("商家地址：" + business.getBusinessAddress());
        System.out.println("商家介绍：" + business.getBusinessExplain());
        System.out.println("起送费：¥" + String.format("%.2f", business.getStarPrice()));
        System.out.println("配送费：¥" + String.format("%.2f", business.getDeliveryPrice()));
        System.out.println("================================");
    }

    /**
     * 修改商家信息
     * 更新商家基本资料（不含密码）
     *
     * @param businessId 商家编号
     */
    @Override
    public void updateBusinessInfo(int businessId) {
        System.out.println("\n========== 修改商家信息 ==========");

        // 先显示当前商家信息
        Business oldBusiness = businessDao.getBusinessById(businessId);
        if (oldBusiness == null) {
            System.out.println("❌ 商家信息查询失败！");
            return;
        }

        System.out.println("\n当前商家信息：");
        System.out.println("商家名称：" + oldBusiness.getBusinessName());
        System.out.println("商家地址：" + oldBusiness.getBusinessAddress());
        System.out.println("商家介绍：" + oldBusiness.getBusinessExplain());
        System.out.println("起送费：¥" + String.format("%.2f", oldBusiness.getStarPrice()));
        System.out.println("配送费：¥" + String.format("%.2f", oldBusiness.getDeliveryPrice()));

        // 获取新的商家信息
        System.out.print("\n请输入新的商家名称：");
        String businessName = input.next();

        System.out.print("请输入新的商家地址：");
        input.nextLine(); // 消费换行符
        String businessAddress = input.nextLine();

        System.out.print("请输入新的商家介绍：");
        String businessExplain = input.nextLine();

        System.out.print("请输入新的起送费：");
        double starPrice = input.nextDouble();

        System.out.print("请输入新的配送费：");
        double deliveryPrice = input.nextDouble();

        // 二次确认
        System.out.print("确认要修改商家信息吗？(y/n)：");
        String confirm = input.next();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("已取消修改操作。");
            return;
        }

        // 创建 Business 对象
        Business business = new Business();
        business.setBusinessId(businessId);
        business.setBusinessName(businessName);
        business.setBusinessAddress(businessAddress);
        business.setBusinessExplain(businessExplain);
        business.setStarPrice(starPrice);
        business.setDeliveryPrice(deliveryPrice);

        // 调用 DAO 层修改商家信息
        boolean result = businessDao.updateBusiness(business);

        // 判断操作结果
        if (result) {
            System.out.println("\n✅ 修改商家信息成功！");
        } else {
            System.out.println("\n❌ 修改商家信息失败，请重试！");
        }
    }

    /**
     * 修改商家密码
     * 输入新密码进行更新
     *
     * @param businessId 商家编号
     */
    @Override
    public void updatePassword(int businessId) {
        System.out.println("\n========== 修改密码 ==========");

        // 获取用户输入
        System.out.print("请输入新密码：");
        String newPassword = input.next();

        System.out.print("请再次确认新密码：");
        String confirmPassword = input.next();

        // 验证两次密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("\n❌ 两次密码输入不一致，请重新操作！");
            return;
        }

        // 二次确认
        System.out.print("确认要修改密码吗？(y/n)：");
        String confirm = input.next();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("已取消修改操作。");
            return;
        }

        // 调用 DAO 层修改密码
        boolean result = businessDao.updateBusinessPassword(businessId, newPassword);

        // 判断操作结果
        if (result) {
            System.out.println("\n✅ 修改密码成功！下次登录请使用新密码。");
        } else {
            System.out.println("\n❌ 修改密码失败，请重试！");
        }
    }
}
