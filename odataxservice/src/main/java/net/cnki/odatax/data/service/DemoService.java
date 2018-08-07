package net.cnki.odatax.data.service;

import net.cnki.odatax.data.DataStore;

import java.util.List;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 15:31
 */
public interface DemoService {
    List<Map<String, Object>> findListByFilter(Map<String, String> params);

    DataStore findStoreByFilter(Map<String, String> params);
}
