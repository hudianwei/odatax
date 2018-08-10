package net.cnki.odatax.data.helper;

/**
 * @author hudianwei
 * @date 2018/8/2 14:54
 */

import net.cnki.odatax.core.Configuration;
import net.cnki.odatax.core.DataUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 提供数据库连接池 和数据库连接 方法为静态的 通过类型访问
 */
public class KBaseJdbcUtils {

    private static String driverName = Configuration.getConfig("kbase."
            + Configuration.DRIVER_NAME);
    private static String url = Configuration.getConfig("kbase."
            + Configuration.URL);
    private static String userName = Configuration.getConfig("kbase."
            + Configuration.USER_NAME);
    private static String userPwd = Configuration.getConfig("kbase."
            + Configuration.USER_PWD);

    // 静态方法块
    // 建立连接
    public static Connection getConnection(String dataSourceName) {
        try {
            // 注册驱动
            loadDriver(dataSourceName);
            // 建立连接 并返回
            return DriverManager.getConnection(url, userName, userPwd);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 静态方法块
    // 建立连接
    public static Connection getConnection() {
        try {
            // 注册驱动
            loadDriver(null);
            // 建立连接 并返回
            return DriverManager.getConnection(url, userName, userPwd);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 注册驱动
    public static void loadDriver(String dataSourceName) {
        if (DataUtil.isBlank(dataSourceName)) {
            dataSourceName = "kbase";
        }
        try {
            // 初始化DataSource
            driverName = Configuration.getConfig(dataSourceName + "."
                    + Configuration.DRIVER_NAME);
            url = Configuration.getConfig(dataSourceName + "."
                    + Configuration.URL);
            userName = Configuration.getConfig(dataSourceName + "."
                    + Configuration.USER_NAME);
            userPwd = Configuration.getConfig(dataSourceName + "."
                    + Configuration.USER_PWD);
            Class.forName(driverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 释放资源
    public static void release(ResultSet rs, Statement stat, Connection con,
                               KBaseDataSource dataSource) {
        // 存在连接或结果集的时候 释放
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            stat = null;
        }
        if (con != null) {
            dataSource.addBackToPool((com.kbase.jdbc.Connection) con);
            con = null;
        }
    }
}
