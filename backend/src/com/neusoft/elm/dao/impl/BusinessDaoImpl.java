package com.neusoft.elm.dao.impl;

import com.neusoft.elm.dao.BusinessDao;
import com.neusoft.elm.po.Business;
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
 * 商家数据访问实现类
 * 实现 BusinessDao 接口，封装商家相关的 JDBC 操作
 *
 * @author Neusoft ELM Team
 * @version 2.0 (Spring Boot Edition)
 */
@Repository
public class BusinessDaoImpl implements BusinessDao {

    private final DataSource dataSource;

    public BusinessDaoImpl() {
        this(DBUtil.getDataSource());
    }

    @Autowired
    public BusinessDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 查询所有商家或根据条件模糊查询商家
     * 使用 StringBuffer 动态拼接 SQL 实现多条件模糊查询
     *
     * @param businessName 商家名称关键词（可为 null）
     * @param businessAddress 商家地址关键词（可为 null）
     * @return 商家列表
     */
    @Override
    public List<Business> listBusiness(String businessName, String businessAddress) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Business> businessList = new ArrayList<>();

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 使用 StringBuffer 动态拼接 SQL（符合 TECHNICAL_DESIGN.md 第 2.2 节要求）
            StringBuffer sql = new StringBuffer("SELECT businessId, password, businessName, businessAddress, businessExplain, starPrice, deliveryPrice FROM business WHERE 1=1");

            // 3. 动态添加查询条件（名称/地址模糊查询）
            boolean hasName = businessName != null && !businessName.trim().isEmpty();
            boolean hasAddress = businessAddress != null && !businessAddress.trim().isEmpty();
            if (hasName && hasAddress) {
                sql.append(" AND (businessName LIKE ? OR businessAddress LIKE ?)");
            } else if (hasName) {
                sql.append(" AND businessName LIKE ?");
            } else if (hasAddress) {
                sql.append(" AND businessAddress LIKE ?");
            }

            // 4. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql.toString());

            // 5. 设置参数（使用 LIKE '%关键词%' 实现模糊查询）
            int paramIndex = 1;
            if (hasName) {
                pstmt.setString(paramIndex++, "%" + businessName + "%");
            }
            if (hasAddress) {
                pstmt.setString(paramIndex++, "%" + businessAddress + "%");
            }

            // 6. 执行查询
            rs = pstmt.executeQuery();

            // 7. 处理结果集
            while (rs.next()) {
                Business business = new Business();
                business.setBusinessId(rs.getInt("businessId"));
                business.setPassword(rs.getString("password"));
                business.setBusinessName(rs.getString("businessName"));
                business.setBusinessAddress(rs.getString("businessAddress"));
                business.setBusinessExplain(rs.getString("businessExplain"));
                business.setStarPrice(rs.getDouble("starPrice"));
                business.setDeliveryPrice(rs.getDouble("deliveryPrice"));
                businessList.add(business);
            }

        } catch (SQLException e) {
            System.err.println("❌ 查询商家列表失败：");
            e.printStackTrace();
        } finally {
            // 8. 关闭资源
            closeResources(rs, pstmt, conn);
        }

        return businessList;
    }

    /**
     * 新建商家
     * 系统自动生成编号并设定默认密码
     *
     * @param business 商家对象
     * @return 新增成功返回自增主键 businessId，失败返回 0
     */
    @Override
    public int saveBusiness(Business business) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int businessId = 0;
        boolean hasPassword = business.getPassword() != null && !business.getPassword().trim().isEmpty();

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句（密码使用数据库默认值 123）
            String sql;
            if (hasPassword) {
                sql = "INSERT INTO business (businessName, businessAddress, businessExplain, starPrice, deliveryPrice, password) VALUES (?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO business (businessName, businessAddress, businessExplain, starPrice, deliveryPrice) VALUES (?, ?, ?, ?, ?)";
            }

            // 3. 创建 PreparedStatement 对象（使用 RETURN_GENERATED_KEYS 获取自增主键）
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, business.getBusinessName());
            pstmt.setString(2, business.getBusinessAddress());
            pstmt.setString(3, business.getBusinessExplain());
            pstmt.setDouble(4, business.getStarPrice() != null ? business.getStarPrice() : 0.00);
            pstmt.setDouble(5, business.getDeliveryPrice() != null ? business.getDeliveryPrice() : 0.00);
            if (hasPassword) {
                pstmt.setString(6, business.getPassword());
            }

            // 4. 执行插入操作
            int result = pstmt.executeUpdate();

            // 5. 获取自动生成的主键（符合 TECHNICAL_DESIGN.md 第 2.2 节要求）
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    businessId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ 新建商家失败：");
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            closeResources(rs, pstmt, conn);
        }

        return businessId;
    }

    /**
     * 删除商家
     * 需级联删除该商家下的所有食品，必须使用 JDBC 事务管理
     *
     * @param businessId 商家编号
     * @return 删除成功返回 true，失败返回 false
     */
    @Override
    public boolean removeBusiness(int businessId) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 开启手动提交模式（符合 TECHNICAL_DESIGN.md 第 2.2 节事务处理要求）
            conn.setAutoCommit(false);

            // 3. 删除该商家下的所有食品
            String sql1 = "DELETE FROM food WHERE businessId = ?";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, businessId);
            pstmt1.executeUpdate();

            // 4. 删除商家
            String sql2 = "DELETE FROM business WHERE businessId = ?";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, businessId);
            int result = pstmt2.executeUpdate();

            // 5. 提交事务
            if (result > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ 删除商家失败，事务回滚：");
            e.printStackTrace();
            // 6. 发生异常时回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // 7. 关闭资源
            if (pstmt1 != null) {
                try {
                    pstmt1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeResources(null, pstmt2, conn);
        }
    }

    /**
     * 商家登录验证
     * 根据商家编号和密码查询商家信息
     *
     * @param businessId 商家编号
     * @param password 商家密码
     * @return 如果验证成功返回 Business 对象，否则返回 null
     */
    @Override
    public Business getBusinessByIdByPass(int businessId, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Business business = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "SELECT businessId, password, businessName, businessAddress, businessExplain, starPrice, deliveryPrice FROM business WHERE businessId = ? AND password = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, businessId);
            pstmt.setString(2, password);

            // 4. 执行查询
            rs = pstmt.executeQuery();

            // 5. 处理结果集
            if (rs.next()) {
                business = new Business();
                business.setBusinessId(rs.getInt("businessId"));
                business.setPassword(rs.getString("password"));
                business.setBusinessName(rs.getString("businessName"));
                business.setBusinessAddress(rs.getString("businessAddress"));
                business.setBusinessExplain(rs.getString("businessExplain"));
                business.setStarPrice(rs.getDouble("starPrice"));
                business.setDeliveryPrice(rs.getDouble("deliveryPrice"));
            }

        } catch (SQLException e) {
            System.err.println("❌ 商家登录验证失败：");
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            closeResources(rs, pstmt, conn);
        }

        return business;
    }

    /**
     * 修改商家信息
     * 更新商家基本资料（不含密码）
     *
     * @param business 商家对象
     * @return 修改成功返回 true，失败返回 false
     */
    @Override
    public boolean updateBusiness(Business business) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句（不更新密码）
            String sql = "UPDATE business SET businessName = ?, businessAddress = ?, businessExplain = ?, starPrice = ?, deliveryPrice = ? WHERE businessId = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, business.getBusinessName());
            pstmt.setString(2, business.getBusinessAddress());
            pstmt.setString(3, business.getBusinessExplain());
            pstmt.setDouble(4, business.getStarPrice());
            pstmt.setDouble(5, business.getDeliveryPrice());
            pstmt.setInt(6, business.getBusinessId());

            // 4. 执行更新操作
            int result = pstmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.err.println("❌ 修改商家信息失败：");
            e.printStackTrace();
            return false;
        } finally {
            // 5. 关闭资源
            closeResources(null, pstmt, conn);
        }
    }

    /**
     * 修改商家密码
     * 根据商家编号更新密码
     *
     * @param businessId 商家编号
     * @param newPassword 新密码
     * @return 修改成功返回 true，失败返回 false
     */
    @Override
    public boolean updateBusinessPassword(int businessId, String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "UPDATE business SET password = ? WHERE businessId = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, businessId);

            // 4. 执行更新操作
            int result = pstmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.err.println("❌ 修改商家密码失败：");
            e.printStackTrace();
            return false;
        } finally {
            // 5. 关闭资源
            closeResources(null, pstmt, conn);
        }
    }

    /**
     * 根据商家编号查询商家信息
     * 用于查看商家详细信息
     *
     * @param businessId 商家编号
     * @return Business 对象，如果不存在返回 null
     */
    @Override
    public Business getBusinessById(int businessId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Business business = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句
            String sql = "SELECT businessId, password, businessName, businessAddress, businessExplain, starPrice, deliveryPrice FROM business WHERE businessId = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, businessId);

            // 4. 执行查询
            rs = pstmt.executeQuery();

            // 5. 处理结果集
            if (rs.next()) {
                business = new Business();
                business.setBusinessId(rs.getInt("businessId"));
                business.setPassword(rs.getString("password"));
                business.setBusinessName(rs.getString("businessName"));
                business.setBusinessAddress(rs.getString("businessAddress"));
                business.setBusinessExplain(rs.getString("businessExplain"));
                business.setStarPrice(rs.getDouble("starPrice"));
                business.setDeliveryPrice(rs.getDouble("deliveryPrice"));
            }

        } catch (SQLException e) {
            System.err.println("❌ 查询商家信息失败：");
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            closeResources(rs, pstmt, conn);
        }

        return business;
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
