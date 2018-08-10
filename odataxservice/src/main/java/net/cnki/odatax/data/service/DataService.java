package net.cnki.odatax.data.service;

import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.model.DataModel;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author hudianwei
 * @Description 数据服务
 * @date 2018/8/2 14:23
 */
public interface DataService {
    public DataStore query(String type, String fields, String filter, String group, String order, Integer start, Integer length, boolean bUnicode);

    public Map<String, DataModel> loadModels();
}
