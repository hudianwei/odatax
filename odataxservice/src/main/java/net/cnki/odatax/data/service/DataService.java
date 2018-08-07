package net.cnki.odatax.data.service;

import javax.sql.DataSource;

/**
 * @author hudianwei
 * @Description 数据服务
 * @date 2018/8/2 14:23
 */
public interface DataService {
    public DataSource query(String type, String fields, String filter, String group, String order, Integer start, Integer length);
}
