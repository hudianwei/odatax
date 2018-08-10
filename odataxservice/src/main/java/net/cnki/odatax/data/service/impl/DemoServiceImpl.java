package net.cnki.odatax.data.service.impl;

import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.data.helper.KBaseHelper;
import net.cnki.odatax.data.service.DemoService;
import net.cnki.odatax.model.DataFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 15:32
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public List<Map<String, Object>> findListByFilter(Map<String, String> params) {
        String word = params.get("数据库分类名称");
        StringBuilder sqlBuilder = new StringBuilder("select * from CNKI_DBINFO where ");
        List<String> args = new ArrayList<>();
        if (!DataUtil.isBlank(word)) {
            sqlBuilder.append(" 数据库分类名称 % [1]");
            args.add(word);
        }
        KBaseHelper kBaseHelper = new KBaseHelper();
        List<Map<String, Object>> records = kBaseHelper.executeQuery(sqlBuilder.toString(), new String[]{"代码", "数据库分类名称", "文献来源", "数据库介绍", "资源种类", "文献收全率", "子库个数", "文献产出时间", "文献篇数", "最新发布日期", "最新篇数", "最近读者"}, args.toArray(new String[0]), false);
        return records;
    }


    @Override
    public DataStore findStoreByFilter(Map<String, String> params) {
        String word = params.get("数据库分类名称");
        String startNo = params.get("startNo");
        String pageSize = params.get("pageSize");
        StringBuilder fromBuilder = new StringBuilder(" from CNKI_DBINFO where ");
        List<String> args = new ArrayList<>();
        if (!DataUtil.isBlank(word)) {
            fromBuilder.append(" 数据库分类名称 % [1]");
            args.add(word);
        }
        if (DataUtil.isBlank(startNo)) {
            startNo = "0";
        }
        if (DataUtil.isBlank(pageSize)) {
            pageSize = "10";
        }
        KBaseHelper kBaseHelper = new KBaseHelper();
        String selectSql = "select * " + fromBuilder.toString() + " limit " + startNo + "," + pageSize + "";
        String countSql = "select count(*) as countNum" + fromBuilder.toString();
        String[] selectColumns = new String[]{"代码", "数据库分类名称", "文献来源", "数据库介绍", "资源种类",
                "文献收全率", "子库个数", "文献产出时间", "文献篇数", "最新发布日期", "最新篇数",
                "最近读者"};
        DataStore dataStore = kBaseHelper.executeQueryForDataStore(selectSql, countSql, selectColumns, "countNum", args.toArray(new String[0]), false);
        return dataStore;
    }
}
