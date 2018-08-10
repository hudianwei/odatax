package net.cnki.odatax.core;

import net.cnki.odatax.exception.SystemException;
import net.cnki.odatax.model.DataModel;
import net.cnki.odatax.model.DataProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Map<String, DataModel> loadModelsFile() {
        File file = null;
        Map<String, DataModel> modelMaps = new HashMap<String, DataModel>();
        FileInputStream in = null;
        String json = null;
        DataModel dataModel = null;
        try {
            file = new File(Utils.class.getResource("/data/model").toURI());
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
    public  Map<String, DataModel> loadModels() {
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


    public static Map<String, Object>   getSqlMap(String type, String fields, String filter, String group, String order, Integer start, Integer length) {
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
                sqlBuiler.append(entry.getValue().getName());
                sqlBuiler.append(",");
                selectColumns.add(entry.getValue().getName());
            }
        } else {
            String[] fieldArrs = fields.split(",");
            String column = null;
            DataProperty property = null;
            for (String field : fieldArrs) {
                if (DataUtil.isBlank(field)) {
                    continue;
                }
                property = propertiesMap.get(field);
                if (property == null) {
                    String lower = field.toLowerCase();
                    //分组相关函数
                    if (lower.startsWith("groupcodename")) {
                        sqlBuiler.append(lower);
                        sqlBuiler.append("as codeName,");
                        selectColumns.add("codeName");
                        continue;
                    }
                    if (lower.startsWith("count")) {
                        sqlBuiler.append(lower);
                        sqlBuiler.append("as codeName,");
                        selectColumns.add("codeName");
                        continue;
                    }
                    throw new SystemException("查询字段[" + field + "]不存在!");
                }
                column = property.getMapping();
                if (DataUtil.isBlank(column)) {
                    continue;
                }
                sqlBuiler.append(column);
                sqlBuiler.append(" as ");
                sqlBuiler.append(property.getName());
                sqlBuiler.append(",");
                selectColumns.add(column);
            }
        }
        sqlBuiler.deleteCharAt(sqlBuiler.length() - 1);
        resultMap.put("selectColumns", selectColumns);
        resultMap.put("selectSql", sqlBuiler.toString());
    }

    private static void buildFromSql(DataModel dataModel, Map<String, Object> resultMap) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" from ");
        sqlBuilder.append(dataModel.getDataView());
        resultMap.put("fromSql", sqlBuilder.toString());
    }

    private static void buildWhereSql(String filter, Map<String, DataProperty> propertiesMap, Map<String, Object> resultMap) {
        if (DataUtil.isBlank(filter)) {
            return;
        }
        Pattern pattern = Pattern.compile("['|\"].+?['|\"]");
        Matcher matcher = pattern.matcher(filter);
        String group = null;
        String newGroup = null;
        String replaceBlank = UUID.randomUUID().toString();
        String replaceEq = UUID.randomUUID().toString();
        String replaceLeft = UUID.randomUUID().toString();
        String replaceRight = UUID.randomUUID().toString();
        String replaceLike = UUID.randomUUID().toString();
        while (matcher.find()) {
            group = matcher.group();
            newGroup = group.replace(" ", replaceBlank).replace("=", replaceEq).replace("(", replaceLeft).replace(")", replaceRight).replace("%", replaceLike);
            filter = filter.replace(group, newGroup);
            String newSql = filter.replace("=", " = ").replace("% =", " %= ").replace("%", " % ").replace("% =", " %= ").replace("(", " ( ")
                    .replace(")", " ) ");
            List<String> args = new ArrayList<>();
            StringBuilder sqlBuilder = new StringBuilder(" where ");
            for (String s : newSql.split(" ")) {
                // 无效字符
                if (DataUtil.isBlank(s.trim())) {
                    continue;
                }
                // 还原字符串
                s = s.replace(replaceBlank, " ");
                s = s.replace(replaceEq, "=");
                s = s.replace(replaceLeft, "(");
                s = s.replace(replaceRight, ")");
                s = s.replace(replaceLike, "%");
                // 操作符替换
                switch (s.toLowerCase()) {
                    case "eq":
                        s = "=";
                        break;
                    case "ne":
                        s = "!=";
                        break;
                    case "lt":
                        s = "<";
                        break;
                    case "le":
                        s = "<=";
                        break;
                    case "gt":
                        s = ">";
                        break;
                    case "ge":
                        s = ">=";
                        break;
                    case "like":
                        s = "%=";
                        break;
                    case "vq":
                        s = "%";
                        break;
                    case "xls":
                    case "and":
                    case "or":
                    case "(":
                    case ")":
                    case "=":
                    case "%":
                    case "%=":
                        break;
                    default:
                        // 字段替换
                        if (!DataUtil.isBlank(propertiesMap.get(s.toLowerCase()))) {
                            s = propertiesMap.get(s.toLowerCase()).getMapping();
                        }
                        // 数值替换(!='(',')')
                        else {
                            args.add(s);
                            s = "[" + args.size() + "]";
                        }
                        break;
                }
                sqlBuilder.append(s);
                sqlBuilder.append(" ");
            }

            resultMap.put("whereSql", sqlBuilder.toString());
            resultMap.put("args", args);
        }
    }

    private static void buildGroupOrderSql(String group, String orders, Map<String, DataProperty> propertiesMap, Map<String, Object> resultMap) {
        StringBuilder sqlBuilder = new StringBuilder();
        DataProperty dataProperty = null;
        if (!DataUtil.isBlank(group)) {
            dataProperty = propertiesMap.get(group.toLowerCase());
            sqlBuilder.append(" group by (");
            sqlBuilder.append(dataProperty.getMapping());
            sqlBuilder.append(", '");
            sqlBuilder.append(dataProperty.getDictionary());
            sqlBuilder.append("')");
        }
        boolean orderFlag = false;
        if (!DataUtil.isBlank(orders)) {
            String[] orderArr = orders.split(",");
            sqlBuilder.append(" order by ");
            for (String order : orderArr) {
                String[] split = order.split(" ");
                dataProperty = propertiesMap.get(split[0].toLowerCase());
                if (DataUtil.isBlank(dataProperty.getName())) {
                    Logging.error("排序字段:【" + dataProperty.getName() + ":"
                            + dataProperty.getMapping() + "】没有提供词典配置。");
                    continue;
                }
                sqlBuilder.append("(");
                sqlBuilder.append(dataProperty.getMapping());
                sqlBuilder.append(",'");
                sqlBuilder.append(dataProperty.getDictionary());
                sqlBuilder.append("')");
                orderFlag = true;
                if (split.length >= 2) {
                    sqlBuilder.append(" ");
                    for (int i = 1; i < split.length; i++) {
                        sqlBuilder.append(split[1]);
                    }
                }
                sqlBuilder.append(",");
            }
            if (!orderFlag) {
                resultMap.put("groupOrderSql", sqlBuilder.toString().replace("order by", ""));
                return;
            } else {
                sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            }
        }
        resultMap.put("groupOrderSql", sqlBuilder.toString());
    }

    private static void buildLimitSql(Integer start, Integer length, Map<String, Object> resultMap) {
        if (DataUtil.isBlank(start)) {
            start = 0;
        }
        if (DataUtil.isBlank(length)) {
            length = 20;
        }
        resultMap.put("limitSql", " limit " + start + "," + length + " ");
    }
}
