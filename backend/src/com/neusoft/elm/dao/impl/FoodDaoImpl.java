package com.neusoft.elm.dao.impl;

import com.neusoft.elm.dao.FoodDao;
import com.neusoft.elm.po.Food;
import com.neusoft.elm.util.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 食品数据访问实现类
 * 实现 FoodDao 接口，封装食品相关的 JDBC 操作
 *
 * @author Neusoft ELM Team
 * @version 2.0 (Spring Boot Edition)
 */
@Repository
public class FoodDaoImpl implements FoodDao {

    private final DataSource dataSource;

    public FoodDaoImpl() {
        this(DBUtil.getDataSource());
    }

    @Autowired
    public FoodDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 查询指定商家的所有食品
     * 根据商家编号查询该商家名下的所有食品
     *
     * @param businessId 商家编号
     * @return 食品列表
     */
    @Override
    public List<Food> listFoodByBusinessId(int businessId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Food> foodList = new ArrayList<>();

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "SELECT foodId, foodName, foodExplain, foodPrice, businessId FROM food WHERE businessId = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, businessId);

            // 4. 执行查询
            rs = pstmt.executeQuery();

            // 5. 处理结果集
            while (rs.next()) {
                Food food = new Food();
                food.setFoodId(rs.getInt("foodId"));
                food.setFoodName(rs.getString("foodName"));
                food.setFoodExplain(rs.getString("foodExplain"));
                food.setFoodPrice(rs.getDouble("foodPrice"));
                food.setBusinessId(rs.getInt("businessId"));
                foodList.add(food);
            }

        } catch (SQLException e) {
            System.err.println("❌ 查询食品列表失败：");
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            closeResources(rs, pstmt, conn);
        }

        return foodList;
    }

    /**
     * 新增食品
     * 为当前商家添加新菜品
     *
     * @param food 食品对象
     * @return 新增成功返回自增主键 foodId，失败返回 0
     */
    @Override
    public int saveFood(Food food) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int foodId = 0;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "INSERT INTO food (foodName, foodExplain, foodPrice, businessId) VALUES (?, ?, ?, ?)";

            // 3. 创建 PreparedStatement 对象（使用 RETURN_GENERATED_KEYS 获取自增主键）
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, food.getFoodName());
            pstmt.setString(2, food.getFoodExplain());
            pstmt.setDouble(3, food.getFoodPrice());
            pstmt.setInt(4, food.getBusinessId());

            // 4. 执行插入操作
            int result = pstmt.executeUpdate();

            // 5. 获取自动生成的主键
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    foodId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ 新增食品失败：");
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            closeResources(rs, pstmt, conn);
        }

        return foodId;
    }

    /**
     * 修改食品
     * 更新已有菜品的价格或描述
     *
     * @param food 食品对象
     * @return 修改成功返回 true，失败返回 false
     */
    @Override
    public boolean updateFood(Food food) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "UPDATE food SET foodName = ?, foodExplain = ?, foodPrice = ? WHERE foodId = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, food.getFoodName());
            pstmt.setString(2, food.getFoodExplain());
            pstmt.setDouble(3, food.getFoodPrice());
            pstmt.setInt(4, food.getFoodId());

            // 4. 执行更新操作
            int result = pstmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.err.println("❌ 修改食品失败：");
            e.printStackTrace();
            return false;
        } finally {
            // 5. 关闭资源
            closeResources(null, pstmt, conn);
        }
    }

    /**
     * 删除食品
     * 移除指定菜品
     *
     * @param foodId 食品编号
     * @return 删除成功返回 true，失败返回 false
     */
    @Override
    public boolean removeFood(int foodId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "DELETE FROM food WHERE foodId = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, foodId);

            // 4. 执行删除操作
            int result = pstmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.err.println("❌ 删除食品失败：");
            e.printStackTrace();
            return false;
        } finally {
            // 5. 关闭资源
            closeResources(null, pstmt, conn);
        }
    }

    /**
     * 根据食品编号查询食品信息
     * 用于修改前的信息回显
     *
     * @param foodId 食品编号
     * @return Food 对象，如果不存在返回 null
     */
    @Override
    public Food getFoodById(int foodId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Food food = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "SELECT foodId, foodName, foodExplain, foodPrice, businessId FROM food WHERE foodId = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, foodId);

            // 4. 执行查询
            rs = pstmt.executeQuery();

            // 5. 处理结果集
            if (rs.next()) {
                food = new Food();
                food.setFoodId(rs.getInt("foodId"));
                food.setFoodName(rs.getString("foodName"));
                food.setFoodExplain(rs.getString("foodExplain"));
                food.setFoodPrice(rs.getDouble("foodPrice"));
                food.setBusinessId(rs.getInt("businessId"));
            }

        } catch (SQLException e) {
            System.err.println("❌ 查询食品信息失败：");
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            closeResources(rs, pstmt, conn);
        }

        return food;
    }

    /**
     * 关闭数据库资源
     */
    private void closeResources(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
