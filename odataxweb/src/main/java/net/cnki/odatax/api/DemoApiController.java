package net.cnki.odatax.api;

import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.data.service.DemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 15:42
 */
@Controller
@RequestMapping("/api/demolist")
public class DemoApiController {
    @Resource
    private DemoService demoService;

    public @ResponseBody
    List<Map<String, Object>> list(@RequestParam Map<String, String> params) {
        List<Map<String, Object>> records = demoService.findListByFilter(params);
        return records;
    }

    public @ResponseBody
    DataStore dataStore(@RequestParam Map<String, String> params) {
        DataStore dataStore = demoService.findStoreByFilter(params);
        return dataStore;
    }
}
