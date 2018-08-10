package net.cnki.odatax.listenter;

import net.cnki.odatax.core.Caching;
import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.data.service.DataService;
import net.cnki.odatax.model.DataModel;
import net.cnki.odatax.model.DataProperty;
import org.junit.Test;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 15:43
 */
@Component
public class ApplicationUpListener implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private DataService dataService;
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        if (arg0.getApplicationContext().getParent() == null) {
            //modelMap
            Map<String, DataModel> modelMap = dataService.loadModels();
            Caching.put("models", modelMap);
            //modelPropertyMap
            Map<String, Map<String, DataProperty>> modelPropertiesMap = new HashMap<String, Map<String, DataProperty>>();
            DataModel subDataModel = null;
            for (Map.Entry<String, DataModel> entry : modelMap.entrySet()) {
                subDataModel = modelMap.get(entry.getValue().getDomain().toLowerCase());
                final Map<String, DataProperty> propertyMap = new HashMap<String, DataProperty>();
                if (!DataUtil.isBlank(subDataModel) && !DataUtil.isBlank(subDataModel.getProperties())) {
                    subDataModel.getProperties().forEach(
                            record -> propertyMap.put(record.getName(), record)
                    );
                }
                entry.getValue().getProperties().forEach(
                        record -> propertyMap.put(record.getName(), record)
                );
                modelPropertiesMap.put(entry.getKey(), propertyMap);
            }
            Caching.put("modelPropertiesMap", modelPropertiesMap);
        }
    }
}
