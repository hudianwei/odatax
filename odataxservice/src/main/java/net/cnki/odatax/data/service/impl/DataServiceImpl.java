package net.cnki.odatax.data.service.impl;

import net.cnki.odatax.core.Utils;
import net.cnki.odatax.data.service.DataService;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 14:24
 */
public class DataServiceImpl implements DataService {
    @Override
    public DataSource query(String type, String fields, String filter, String group, String order, Integer start, Integer length) {
        Map<String,Object>sqlMap=Utils
        return null;
    }
}
