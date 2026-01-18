-- 测试前：查看初始数据
SELECT '========== 测试前数据快照 ==========' AS '';

SELECT '商家数据：' AS '';
SELECT businessId, businessName, businessAddress FROM business;

SELECT '食品数据：' AS '';
SELECT foodId, foodName, businessId FROM food;

SELECT '商家ID=1的食品数量：' AS '';
SELECT COUNT(*) as food_count FROM food WHERE businessId = 1;
