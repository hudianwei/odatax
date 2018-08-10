package net.cnki.odatax.api;

import net.cnki.odatax.core.Caching;
import net.cnki.odatax.model.DataModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 15:43
 */
@RequestMapping
public class ModelApiController {
    /**
     * @Description:读取所有DataModel json文件
     * @Param: []
     * @Return: java.util.List<java.lang.Object>
     * @Author: HU
     * @Date: 2018/8/10 13:46
     */
    public @ResponseBody
    List<Object> getModels() {
        List<Object> models = new ArrayList<Object>();
        Map<String, Object> modelMap = (Map<String, Object>) Caching.get("models");
        for (Map.Entry<String, Object> entry : modelMap.entrySet()) {
            models.add(entry.getValue());
        }
        return models;
    }

    @RequestMapping("/{type}")
    public @ResponseBody
    DataModel getModel(@PathVariable("type") String type) {
        Map<String, DataModel> modelMap = (Map<String, DataModel>) Caching.get("models");
        DataModel model = modelMap.get(type.toLowerCase());
        return model;
    }
}
