package net.cnki.odatax.data.service.impl;

import net.cnki.odatax.core.*;
import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.data.helper.KBaseHelper;
import net.cnki.odatax.data.service.DataService;
import net.cnki.odatax.exception.SystemException;
import net.cnki.odatax.model.DataFilter;
import net.cnki.odatax.model.DataModel;
import net.cnki.odatax.model.DataProperty;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author hudianwei
 * @date 2018/8/2 14:24
 */
public class DataServiceImpl implements DataService {
    @Override
    public DataStore query(String type, String fields, String filter, String group, String order, Integer start, Integer length, boolean bUnicode) {
        Map<String, Object> sqlMap = Utils.getSqlMap(type, fields, filter, group, order, start, length);
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder countSelectBuilder = new StringBuilder();
        String selectSql = (String) sqlMap.get("selectSql");
        String fromSql = (String) sqlMap.get("fromSql");
        List<Object> selectColumns = (List<Object>) sqlMap.get("selectColumns");
        List<Object> args = (List<Object>) sqlMap.get("args");
        String whereSql = (String) sqlMap.get("whereSql");
        String groupOrderSql = (String) sqlMap.get("groupOrderSql");
        String limitSql = (String) sqlMap.get("limitSql");
        if (!DataUtil.isBlank(selectSql)) {
            sqlBuilder.append(selectSql);
        }
        if (!DataUtil.isBlank(fromSql)) {
            sqlBuilder.append(fromSql);
        }
        if (!DataUtil.isBlank(whereSql)) {
            sqlBuilder.append(whereSql);
        }
        if (!DataUtil.isBlank(groupOrderSql)) {
            sqlBuilder.append(groupOrderSql);
        }
        if (!DataUtil.isBlank(limitSql)) {
            sqlBuilder.append(limitSql);
        }
        DataModel dataModel = (DataModel) sqlMap.get("dataModel");
        String dataSourece = dataModel.getDataSource();
        String driver = Configuration.getConfig(dataSourece + "." + Configuration.DRIVER_NAME);
        if (DataUtil.isBlank(driver)) {
            Logging.error("驱动加载失败:驱动为空。");
            throw new SystemException("驱动加载失败:驱动为空。");
        }
        DataStore dataStore = null;
        switch (driver) {
            case Configuration.KBASE_DRIVER_NAME:
                KBaseHelper kBaseHelper = new KBaseHelper(dataSourece);
                if (selectColumns == null) {
                    selectColumns = new ArrayList<Object>();
                }
                if (args == null) {
                    args = new ArrayList<Object>();
                }
                countSelectBuilder.append(" select count(*) as countNum ");
                countSelectBuilder.append(fromSql);
                if (!DataUtil.isBlank(whereSql)) {
                    countSelectBuilder.append(whereSql);
                }
                dataStore = kBaseHelper.executeQueryForDataStore(sqlBuilder.toString(), countSelectBuilder.toString(),
                        selectColumns.toArray(new String[0]), "countNum", args.toArray(new String[0]), bUnicode);
                break;
            default:
                break;
        }
        Logging.info("selectSql:" + sqlBuilder.toString());
        Logging.info("countSql:" + countSelectBuilder.toString());
        Logging.info("params:" + args);
        this.filterDataStore(dataStore, dataModel);
        return dataStore;
    }

    private void filterDataStore(DataStore dataStore, DataModel dataModel) {
        if (DataUtil.isBlank(dataStore) || DataUtil.isBlank(dataStore.getRecords())) {
            return;
        }
        List<Map<String, Object>> records = dataStore.getRecords();
        final Map<String, String> nameFilterMapping = new HashMap<>();
        DataModel subDataModel = (DataModel) ((Map<String, Object>) Caching.get("models")).get(dataModel.getDomain().toLowerCase());
        if (subDataModel != null) {
            final List<DataProperty> properties = subDataModel.getProperties();
            properties.addAll(dataModel.getProperties());
            properties.forEach(property -> {
                if (!DataUtil.isBlank(property.getFilter())) {
                    nameFilterMapping.put(property.getName(), property.getFilter());
                }
            });
        }
        for (Map<String, Object> record : records) {
            for (Map.Entry<String, String> entry : nameFilterMapping.entrySet()) {
                if (DataUtil.isBlank(record.get(entry.getKey()))) {
                    continue;
                }
                String value = DataFilter.process(entry.getValue(), record.get(entry.getKey()));
                record.put(entry.getKey(), value);
            }
        }
    }

    @Override
    public Map<String, DataModel> loadModels() {
        Map<String, DataModel> modelMaps = new HashMap<String, DataModel>();
        InputStream in = null;
        String json = null;
        DataModel dataModel = null;
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(this.getClass().getProtectionDomain()
                    .getCodeSource().getLocation().getPath());
            Enumeration<JarEntry> entries = jarFile.entries();
            JarEntry jarEntry = null;
            while (entries.hasMoreElements()) {
                jarEntry = entries.nextElement();
                if (jarEntry.getName().endsWith(".json")
                        && jarEntry.getName().startsWith("data/model")) {
                    in = this.getClass().getClassLoader()
                            .getResourceAsStream(jarEntry.getName());
                    json = FileUtil.read(in, "utf-8");
                    dataModel = TextUtil.parseFromJSON(json, DataModel.class);
                    modelMaps.put(dataModel.getName().toLowerCase(), dataModel);

                }
            }
            jarFile.close();
            in.close();

        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (jarFile != null) {
                    jarFile.close();

                }
            } catch (IOException e) {
                throw new SystemException(e);
            }
        }
        return modelMaps;
    }
}
