package net.cnki.odatax.core;

import net.cnki.odatax.exception.SystemException;
import net.cnki.odatax.model.DataModel;
import net.cnki.odatax.model.DataProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hudianwei
 * @Description
 * @date 2018/8/2 15:27
 */
public class Utils {
    /**
     * @Description: 生成随机数（4位：0001~9999）
     * @Param: [pos]
     * @return: java.lang.String
     * @Author: HU
     * @Date: 2018/8/4
     */

    public static String getRandomNumber(int pos) {
        StringBuilder prefixBuilder = new StringBuilder();
        long a = System.currentTimeMillis();
        double b = Math.pow(10, pos - 1);
        long randomNumber = (System.currentTimeMillis() % (int) Math.pow(10, pos - 1));
        for (int i = pos; i > 0; i--) {
            if (randomNumber / (int) Math.pow(10, i - 1) == 0) {
                prefixBuilder.append("0");
            } else {
                break;
            }
        }

        return prefixBuilder.toString() + randomNumber;
    }

    /**
     * @Description: 加载model
     * @Param: []
     * @Author: HU
     * @Date: 2018/8/6
     */

    public static Map<String, DataModel> loadModels() {
        File file = null;
        Map<String, DataModel> modelMaps = new HashMap<String, DataModel>();
        FileInputStream in = null;
        String json = null;
        DataModel dataModel = null;
        try {
            file = new File(Utils.class.getResource("/model").toURI());
            for (File subFile : file.listFiles()) {
                in = new FileInputStream(subFile);
                json = FileUtil.read(in, "utf-8");
                dataModel = TextUtil.parseFromJSON(json, DataModel.class);
                modelMaps.put(dataModel.getName().toLowerCase(), dataModel);
            }
            in.close();
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new SystemException(e);
                }
            }
        }
        return modelMaps;
    }

    public static Map<String, Object> getSqlMap(String type, String fields, String filter, String group, String order, Integer start, Integer length) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, DataModel> dataModelMap = (Map<String, DataModel>) Caching.get("models");
        DataModel dataModel = dataModelMap.get(type.toLowerCase());
        final Map<String, DataProperty> propertiesMap = new HashMap<>();
        //继承父级属性
        String domain = dataModel.getDomain();
        DataModel subDataModel = dataModelMap.get(domain.toLowerCase());
        List<DataProperty> properties = null;
        if (subDataModel != null) {
            properties = subDataModel.getProperties();
            if (properties != null) {
                properties.forEach(record -> {
                    propertiesMap.put(record.getName().toLowerCase(), record);
                });
            }
        }
        //子类重写
        if (dataModel != null) {
            properties = dataModel.getProperties();
            if (properties != null) {
                properties.forEach(record -> {
                    propertiesMap.put(record.getName().toLowerCase(), record);
                });
            }
        }

        buildSelectSql(fields, propertiesMap, resultMap);
        buildFromSql(dataModel, resultMap);
        buildWhereSql(filter, propertiesMap, resultMap);
        buildGroupOrderSql(group, order, propertiesMap, resultMap);
        buildLimitSql(start, length, resultMap);
        resultMap.put("dataModel", dataModel);
        return resultMap;
    }

    private static void buildSelectSql(String fields, Map<String, DataProperty> propertiesMap, Map<String, Object> resultMap) {
        StringBuilder sqlBuiler = new StringBuilder("select ");
        List<String> selectColumns = new ArrayList<String>();
        if (DataUtil.isBlank(fields)) {
            for (Map.Entry<String, DataProperty> entry : propertiesMap.entrySet()) {
                if (DataUtil.isBlank(entry.getValue().getMapping())) {
                    continue;
                }
                sqlBuiler.append(entry.getValue().getMapping());
                sqlBuiler.append(" as ");
                sqlBuiler.append(entry.getValue().getCaption());
                sqlBuiler.append(",");
                selectColumns.add(entry.getValue().getCaption());
            }
        } else {

        }
    }

    private static void buildFromSql(DataModel dataModel, Map<String, Object> resultMap) {
    }

    private static void buildWhereSql(String filter, Map<String, DataProperty> propertiesMap, Map<String, Object> resultMap) {
    }

    private static void buildGroupOrderSql(String group, String order, Map<String, DataProperty> propertiesMap, Map<String, Object> resultMap) {
    }

    private static void buildLimitSql(Integer start, Integer length, Map<String, Object> resultMap) {
    }
}
