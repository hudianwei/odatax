package net.cnki.odatax.web;

import net.cnki.odatax.data.service.DemoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 15:43
 */
@Controller
@RequestMapping("/demo")
public class DemoWebController {
    @Resource
    private DemoService demoService;

    @RequestMapping("/list")
    public String list(@RequestParam Map<String, String> params, Model model) {
        model.addAttribute("records", demoService.findListByFilter(params));
        return "demo/demolist";
    }

    @RequestMapping("/store")
    public String dataStore(@RequestParam Map<String, String> params, Model model) {
        model.addAttribute("store", demoService.findListByFilter(params));
        return "demo/demostore";
    }

    @RequestMapping
    public String dataIndex(@RequestParam Map<String, String> params, Model model) {
        return "demo/index";
    }
}
