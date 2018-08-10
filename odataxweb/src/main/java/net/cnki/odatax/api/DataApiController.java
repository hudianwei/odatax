package net.cnki.odatax.api;

import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.data.service.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description:
 * @author: HU
 * @date: 2018/8/10 10:26
 */
@Controller
@RequestMapping("/api/data")
public class DataApiController {
    @Resource
    private DataService dataService;

    /**
     * @Description: 取某张表全部数据
     * @Param: [type, params]
     * @Return: net.cnki.odatax.data.DataStore
     * @Author: HU
     * @Date: 2018/8/10 11:53
     */
    public @ResponseBody
    DataStore getData(@PathVariable("type") String type, @RequestParam Map<String, String> params) {
        String fields = params.get("fields");
        String filter = params.get("filter");
        String group = params.get("group");
        String order = params.get("order");
        Integer start = DataUtil.isBlank(params.get("start")) ? null : Integer.parseInt(params.get("start"));
        Integer length = DataUtil.isBlank(params.get("length")) ? null : Integer.parseInt(params.get("length"));
        DataStore dataStore = dataService.query(type, fields, filter, group, order, start, length, false);
        return dataStore;
    }

    /**
     * @Description: 取某一天数据
     * @Param: [type, id]
     * @Return: net.cnki.odatax.data.DataStore
     * @Author: HU
     * @Date: 2018/8/10 13:16
     */
    public @ResponseBody
    DataStore getData(@PathVariable("type") String type, @PathVariable("id") String id) {
        DataStore dataStore = dataService.query(type, null, "id=" + id, null, null, null, null, false);
        return dataStore;
    }
}
