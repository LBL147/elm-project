package com.neusoft.elm.dao.impl;

import com.neusoft.elm.dao.AdminDao;
import com.neusoft.elm.po.Admin;
import com.neusoft.elm.util.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 管理员数据访问实现类
 * 实现 AdminDao 接口，封装管理员相关的 JDBC 操作
 *
 * @author Neusoft ELM Team
 * @version 2.0 (Spring Boot Edition)
 */
@Repository
public class AdminDaoImpl implements AdminDao {

    private final DataSource dataSource;

    public AdminDaoImpl() {
        this(DBUtil.getDataSource());
    }

    @Autowired
    public AdminDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 管理员登录验证
     * 根据用户名和密码查询管理员信息
     *
     * @param adminName 管理员用户名
     * @param password 管理员密码
     * @return 如果验证成功返回 Admin 对象，否则返回 null
     */
    @Override
    public Admin getAdminByNameByPass(String adminName, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Admin admin = null;

        try {
            // 1. 获取数据库连接
            conn = dataSource.getConnection();

            // 2. 编写 SQL 语句（使用 PreparedStatement 防止 SQL 注入）
            String sql = "SELECT adminId, adminName, password FROM admin WHERE adminName = ? AND password = ?";

            // 3. 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, adminName);
            pstmt.setString(2, password);

            // 4. 执行查询
            rs = pstmt.executeQuery();

            // 5. 处理结果集
            if (rs.next()) {
                admin = new Admin();
                admin.setAdminId(rs.getInt("adminId"));
                admin.setAdminName(rs.getString("adminName"));
                admin.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
            System.err.println("❌ 管理员登录验证失败：");
            e.printStackTrace();
        } finally {
            // 6. 关闭资源（必须在 finally 块中）
            closeResources(rs, pstmt, conn);
        }

        return admin;
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
