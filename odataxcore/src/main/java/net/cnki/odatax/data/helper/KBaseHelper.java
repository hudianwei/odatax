package net.cnki.odatax.data.helper;

import com.kbase.jdbc.Connection;
import com.kbase.jdbc.PreparedStatement;
import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.exception.SystemException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author hudianwei
 * @date 2018/8/2 14:26
 */
public class KBaseHelper {
    private Connection conn = null;
    private KBaseDataSource kBaseDataSource = null;

    public KBaseHelper() {
        try {
            this.kBaseDataSource = KBaseDataSource.INSTANCE;
            this.conn = KBaseDataSource.INSTANCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public KBaseHelper(String dataSource) {
        try {
            this.kBaseDataSource = KBaseDataSource.KBASE_DATASOURCE_MAP.get(dataSource);
            if (this.kBaseDataSource == null) {
                kBaseDataSource = new KBaseDataSource(dataSource);
                kBaseDataSource.KBASE_DATASOURCE_MAP.put(dataSource, kBaseDataSource);
            }
            this.conn = (Connection) kBaseDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 执行sql语句;preparedStatement的方法还没实现度utf-8字符的处理
     * @Param: [sql, params, bUnicode] [sql语句，参数，是否是unicode默认false(false : ANSI ， true : UTF-8)]
     * @Return: int
     * @Author: HU
     * @Date: 2018/8/9 10:59
     */
    public int executeNonQuery(String sql, String[] params, boolean bUnicode) {
        PreparedStatement preparedStatement = null;
        int rows = 0;
        try {
            preparedStatement = (PreparedStatement) conn.prepareStatement(sql);
            if (!DataUtil.isBlank(params)) {
                int i = 1;
                for (String param : params) {
                    preparedStatement.setString(i++, param);
                }
            }
            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(e);
        } finally {
            KBaseJdbcUtils.release(null, preparedStatement, conn, this.kBaseDataSource);
        }
        return rows;
    }

    /**
     * @Description:
     * @Param: [sql, params]
     * @Return: java.sql.ResultSet
     * @Author: HU
     * @Date: 2018/8/9 13:55
     */
    public ResultSet executeQuery(String sql, String[] params, boolean bUnicode) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = (PreparedStatement) conn.prepareStatement(sql);
            if (!DataUtil.isBlank(params)) {
                int i = 1;
                for (String param : params) {
                    preparedStatement.setString(i++, param);
                }
            }
            rs = preparedStatement.executeQuery(bUnicode);
        } catch (SQLException e) {
            throw new SystemException(e);
        } finally {
            KBaseJdbcUtils.release(null, preparedStatement, conn, this.kBaseDataSource);
        }
        return rs;
    }

    /**
     * @Description: 执行sql，返回List 泛型
     * @Param: [sql, params, clazz, bUnicode]
     * @Return: java.util.List<T>
     * @Author: HU
     * @Date: 2018/8/9 14:31
     */
    public <T> List<T> executeQuery(String sql, String[] params, Class<T> clazz, boolean bUnicode) {
        List<T> records = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = (PreparedStatement) conn.prepareStatement(sql);
            if (!DataUtil.isBlank(params)) {
                int i = 1;
                for (String param : params) {
                    preparedStatement.setString(i++, param);
                }
            }
            rs = preparedStatement.executeQuery(bUnicode);
            while (rs.next()) {
                //实体和数据字段对应赋值怎么处理
            }
        } catch (SQLException e) {
            throw new SystemException(e);
        } finally {
            KBaseJdbcUtils.release(rs, preparedStatement, conn, this.kBaseDataSource);
        }
        return records;
    }

    /**
     * @Description: 执行sql，返回List Map
     * @Param: [sql, columns, params, bUnicode]
     * @Return: java.util.List<java.util.Map                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>>
     * @Author: HU
     * @Date: 2018/8/9 15:52
     */
    public List<Map<String, Object>> executeQuery(String sql, String[] columns, String[] params, boolean bUnicode) {
        if (DataUtil.isBlank(columns)) {
            return null;
        }
        List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
        Map<String, Object> record = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = (PreparedStatement) conn.prepareStatement(sql);
            if (!DataUtil.isBlank(params)) {
                int i = 1;
                for (String param : params) {
                    preparedStatement.setString(i++, param);
                }
            }
            rs = preparedStatement.executeQuery(bUnicode);
            while (rs.next()) {
                record = new HashMap<>();
                for (String column : columns) {
                    record.put(column, rs.getString(column));
                }
                records.add(record);
            }
        } catch (SQLException e) {
            throw new SystemException(e);
        } finally {
            KBaseJdbcUtils.release(rs, preparedStatement, conn, this.kBaseDataSource);
        }
        return records;
    }

    /**
     * @Description: 执行sql，返回DataStore
     * @Param: [selectSql, countSql, selectColumns, countColumn, param]
     * @Return: net.cnki.odatax.data.DataStore
     * @Author: HU
     * @Date: 2018/8/9 15:53
     */
    public DataStore executeQueryForDataStore(String selectSql, String countSql, String[] selectColumns, String countColumn, String[] params, boolean bUnicode) {
        DataStore dataStore = new DataStore();
        try {
            long beginTime = System.currentTimeMillis();
            List<Map<String, Object>> records = this.executeQuery(selectSql, selectColumns, params, bUnicode);
            List<Map<String, Object>> countQueryResults = this.executeQuery(countSql, new String[]{countColumn}, params, bUnicode);
            String countsStr = "0";
            if (!DataUtil.isBlank(countQueryResults)) {
                countsStr = (String) countQueryResults.get(0).get(countColumn);
            }
            long endTime = System.currentTimeMillis();
            dataStore.setRecords(records);
            dataStore.setProcessingTime(new BigDecimal(((endTime - beginTime) / 1000F) + ""));
            dataStore.setTotalCount(Long.parseLong(countsStr));
        } catch (Exception e) {
            dataStore.setErrorMessage(e.getMessage());
            dataStore.setErrorCode(500);
        }
        return dataStore;
    }
}
