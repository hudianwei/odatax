package net.cnki.odatax.web;

import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.data.service.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description:
 * @author: HU
 * @date: 2018/8/10 15:31
 */
@Controller
@RequestMapping("/detail")
public class DetailController {
    @Resource
    private DataService dataService;

    @RequestMapping("/{type}/{id}")
    public String index(@PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("id", id);
        return "detail/index";
    }

    @RequestMapping("/data/{type}/{id}")
    public @ResponseBody
    Map<String, Object> record(@PathVariable("type") String type, @PathVariable("id") String id) {
        DataStore dataStore = dataService.query(type, null, "id=" + id, null, null, null, null, false);
        Map<String, Object> record = null;
        if (!DataUtil.isBlank(dataStore.getRecords())) {

            // 作者特殊处理
            record = dataStore.getRecords().get(0);
            String authors = (String) record.get("作者");
            if (!DataUtil.isBlank(authors)) {
                String[] split = authors.split(";");
                record.put("作者", split);
            }
        }
        return record;
    }
}
