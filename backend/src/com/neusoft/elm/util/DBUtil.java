package com.neusoft.elm.util;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 负责提供数据库连接获取和资源关闭功能
 * 严格遵循 TECHNICAL_DESIGN.md 和 CODE_STYLE.md 规范
 *
 * @author Neusoft ELM Team
 * @version 1.0
 */
public class DBUtil {

    // 数据库连接参数配置（请根据实际情况修改）
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/elm?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final DataSource DATA_SOURCE = buildDataSource();

    // 静态代码块：加载 MySQL 驱动
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("✅ MySQL 驱动加载成功！");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL 驱动加载失败！");
            System.err.println("请确认已将 mysql-connector-java.jar 添加到项目 lib 目录！");
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象
     *
     * @return Connection 数据库连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    private static DataSource buildDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

    /**
     * 关闭数据库资源（重载方法1：仅关闭 Connection）
     *
     * 使用场景：执行 INSERT/UPDATE/DELETE 操作后
     *
     * @param conn 数据库连接对象
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("❌ 关闭 Connection 时发生异常：");
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭数据库资源（重载方法2：关闭 PreparedStatement 和 Connection）
     *
     * 使用场景：执行 INSERT/UPDATE/DELETE 操作后
     *
     * @param pstmt PreparedStatement 对象
     * @param conn 数据库连接对象
     */
    public static void close(PreparedStatement pstmt, Connection conn) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("❌ 关闭 PreparedStatement 时发生异常：");
                e.printStackTrace();
            }
        }
        close(conn);
    }

    /**
     * 关闭数据库资源（重载方法3：关闭 ResultSet、PreparedStatement 和 Connection）
     *
     * 使用场景：执行 SELECT 查询操作后
     *
     * @param rs ResultSet 结果集对象
     * @param pstmt PreparedStatement 对象
     * @param conn 数据库连接对象
     */
    public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("❌ 关闭 ResultSet 时发生异常：");
                e.printStackTrace();
            }
        }
        close(pstmt, conn);
    }

    /**
     * 测试数据库连接是否正常
     * 运行此 main 方法可验证数据库配置是否正确
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("🎉 数据库连接测试成功！");
                System.out.println("📌 连接信息：" + conn);
            }
        } catch (SQLException e) {
            System.err.println("❌ 数据库连接测试失败！");
            System.err.println("请检查以下配置：");
            System.err.println("  1. MySQL 服务是否已启动");
            System.err.println("  2. 数据库名称 'elm' 是否已创建");
            System.err.println("  3. 用户名和密码是否正确");
            System.err.println("  4. mysql-connector-java.jar 是否已添加到 lib 目录");
            e.printStackTrace();
        } finally {
            DBUtil.close(conn);
        }
    }
}
