package net.cnki.odatax.web;

import net.cnki.odatax.core.Caching;
import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.data.DataStore;
import net.cnki.odatax.data.service.DataService;
import net.cnki.odatax.exception.SystemException;
import net.cnki.odatax.model.DataModel;
import net.cnki.odatax.model.DataProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.smartcardio.ATR;
import java.util.Map;

/**
 * @Description:
 * @author: HU
 * @date: 2018/8/10 15:31
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    @Resource
    private DataService dataService;

    @RequestMapping
    public String index(@RequestParam(value = "currentModel", required = false) String currentModel, @RequestParam(value = "index", required = false) String index, Model model) {
        Map<String, DataModel> dataModelMap = (Map<String, DataModel>) Caching.get("models");
        if (DataUtil.isBlank(currentModel)) {
            currentModel = "literature";
        }
        if (DataUtil.isBlank(index)) {
            index = "0";
        }
        model.addAllAttributes(dataModelMap);
        model.addAttribute("index", index);
        model.addAttribute("currentModel", currentModel);
        model.addAttribute("currentModelDisplay",
                dataModelMap.get(currentModel.toLowerCase()).getCaption());
        return "home/index";
    }

    @RequestMapping("/group")
    public @ResponseBody
    DataStore group(@RequestParam Map<String, String> params) {
        String currentModel = params.get("currentModel");
        String name = params.get("name");
        if (DataUtil.isBlank(currentModel) || DataUtil.isBlank(name)) {
            throw new SystemException("分组名为空!");
        }
        Map<String, Map<String, DataProperty>> modelPropertiesMap = (Map<String, Map<String, DataProperty>>) Caching.get("modelPropertiesMap");
        DataProperty dataProperty = modelPropertiesMap.get(currentModel.toLowerCase()).get(name);
        if (DataUtil.isBlank(dataProperty.getGroupCodeName())) {
            throw new SystemException("请检查配置文件:" + currentModel + "[" + dataProperty.getName() + "]对应字段未配置groupCodeName");
        }
        DataStore dataStore = dataService.query(currentModel, "groupcodename(" + dataProperty.getGroupCodeName() + "),count(*)", null, name, null, null, null, false);
        return dataStore;
    }
}
