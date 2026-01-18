-- ============================================
-- 《饿了么商家后台管理系统》数据库初始化脚本
-- 严格按照 README.md 需求文档编写
-- 字符集：UTF-8
-- 数据库引擎：InnoDB（支持事务）
-- ============================================

-- 1. 创建数据库
DROP DATABASE IF EXISTS elm;
CREATE DATABASE elm DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE elm;

-- 2. 创建商家表 (business)
-- 严格按照 README.md 第 3.1 节要求
CREATE TABLE business (
    businessId INT PRIMARY KEY AUTO_INCREMENT COMMENT '商家编号',
    password VARCHAR(20) NOT NULL DEFAULT '123' COMMENT '密码（默认123）',
    businessName VARCHAR(40) NOT NULL COMMENT '商家名称',
    businessAddress VARCHAR(50) COMMENT '商家地址',
    businessExplain VARCHAR(40) COMMENT '商家介绍',
    starPrice DECIMAL(5,2) DEFAULT 0.00 COMMENT '起送费',
    deliveryPrice DECIMAL(5,2) DEFAULT 0.00 COMMENT '配送费'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家信息表';

-- 3. 创建食品表 (food)
-- 严格按照 README.md 第 3.2 节要求
CREATE TABLE food (
    foodId INT PRIMARY KEY AUTO_INCREMENT COMMENT '食品编号',
    foodName VARCHAR(30) NOT NULL COMMENT '食品名称',
    foodExplain VARCHAR(30) COMMENT '食品介绍',
    foodPrice DECIMAL(5,2) NOT NULL COMMENT '食品价格',
    businessId INT NOT NULL COMMENT '所属商家编号',
    FOREIGN KEY (businessId) REFERENCES business(businessId) ON DELETE CASCADE,
    INDEX idx_business (businessId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食品信息表';

-- 4. 创建管理员表 (admin)
-- 严格按照 README.md 第 3.3 节要求
CREATE TABLE admin (
    adminId INT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员编号',
    adminName VARCHAR(20) NOT NULL UNIQUE COMMENT '管理员名称',
    password VARCHAR(20) NOT NULL COMMENT '密码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 5. 插入测试数据
-- 商家数据（密码默认为 123）
INSERT INTO business (businessName, businessAddress, businessExplain, starPrice, deliveryPrice)
VALUES
('肯德基(建国路店)', '北京市朝阳区建国路88号', '全球知名快餐连锁品牌', 20.00, 5.00),
('麦当劳(世纪大道店)', '上海市浦东新区世纪大道1号', '麦当劳欢乐送', 15.00, 6.00),
('星巴克(天河店)', '广州市天河区天河路123号', '咖啡连锁品牌', 25.00, 8.00),
('必胜客', '深圳市南山区科技园', '比萨专家', 30.00, 10.00);

-- 食品数据
INSERT INTO food (foodName, foodExplain, foodPrice, businessId)
VALUES
('香辣鸡腿堡', '经典美味，辣度适中', 18.50, 1),
('薯条(大)', '金黄酥脆', 12.00, 1),
('可乐(中杯)', '冰爽可口', 8.00, 1),
('巨无霸', '双层牛肉堡', 22.00, 2),
('麦辣鸡翅(5块)', '香辣多汁', 15.50, 2),
('美式咖啡(中杯)', '浓郁香醇', 28.00, 3),
('拿铁咖啡(中杯)', '香浓丝滑', 32.00, 3),
('至尊比萨(9寸)', '料足味美', 68.00, 4);

-- 管理员数据
INSERT INTO admin (adminName, password)
VALUES ('admin', '123456');

-- 6. 数据验证查询
SELECT '========== 商家表数据 ==========' AS '';
SELECT * FROM business;

SELECT '========== 食品表数据 ==========' AS '';
SELECT * FROM food;

SELECT '========== 管理员表数据 ==========' AS '';
SELECT * FROM admin;

-- 7. 验证级联删除功能（取消注释后可测试）
-- DELETE FROM business WHERE businessId = 1;
-- SELECT '删除商家ID=1后，食品表数据：' AS '';
-- SELECT * FROM food;
