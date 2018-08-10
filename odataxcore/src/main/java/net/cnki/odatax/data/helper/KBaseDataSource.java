package net.cnki.odatax.data.helper;

import com.kbase.jdbc.Connection;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


/**
 * @author hudianwei
 * @date 2018/8/2 14:53
 */
public class KBaseDataSource implements DataSource {
    /* 默认数据源：kbase
     */
    public static final KBaseDataSource INSTANCE = new KBaseDataSource();

    /**
     * 多数据源，启动服务器创建，可在查询时切换
     */
    public static final Map<String, KBaseDataSource> KBASE_DATASOURCE_MAP = new ConcurrentHashMap<String, KBaseDataSource>();

    static {
        KBASE_DATASOURCE_MAP.put("kbase", INSTANCE);
    }

    // 通过 linkList充当 池
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    // 构造 函数 初始化 连接 数目,默认初始化20个
    private KBaseDataSource() {
        for (int i = 0; i < 20; i++) {
            // 创建 连接
            Connection connection = (Connection) KBaseJdbcUtils.getConnection();
            // 把 创建的连接 放入池子中
            pool.add(connection);
        }
    }

    /**
     * 启动服务器时调用
     *
     * @param dataSourceName
     */
    public KBaseDataSource(String dataSourceName) {
        for (int i = 0; i < 20; i++) {
            // 创建 连接
            Connection connection = (Connection) KBaseJdbcUtils
                    .getConnection(dataSourceName);
            // 把 创建的连接 放入池子中
            pool.add(connection);
        }
    }

    // 构造 函数 初始化 连接 数目
    public KBaseDataSource(int maxActive) {
        for (int i = 0; i < maxActive; i++) {
            // 创建 连接
            Connection connection = (Connection) KBaseJdbcUtils.getConnection();
            // 把 创建的连接 放入池子中
            pool.add(connection);
        }
    }

    // 用完 之后 将传递的连接放回 池中
    public void addBackToPool(Connection connection) {
        pool.add(connection);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    // 从线程池 中 取得 第一个
    @Override
    public Connection getConnection() throws SQLException {
        // 首先 判断 是否为空
        if (pool.isEmpty()) {
            // 为空的话 继续创建 5个连接
            for (int i = 0; i < 5; i++) {
                Connection connection = (Connection) KBaseJdbcUtils.getConnection();
                pool.add(connection);
            }
        }
        // 有连接的话 就取出第一个
        Connection con = pool.removeFirst();
        // System.out.println("取得一个连接 使用");
        return con;
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        return null;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

}
