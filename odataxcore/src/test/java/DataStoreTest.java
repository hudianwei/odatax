import net.cnki.odatax.core.TextUtil;
import net.cnki.odatax.data.DataStore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: HU
 * @date: 2018/8/8 16:52
 */
public class DataStoreTest {
    @Test
    public void test() {
        //构造DataStore
        DataStore dataStore = new DataStore();
        List<Map<String, Object>> records = new ArrayList() {
            {
                add(new HashMap() {
                    {
                        put("name", "时间");
                        put("employeeNo", System.currentTimeMillis());
                    }
                });
                add(new HashMap() {
                    {
                        put("name", "第二时间");
                        put("employeeNo", System.currentTimeMillis());
                    }
                });
            }
        };
        dataStore.setRecords(records);
        dataStore.setDescription("测试描述信息");
        dataStore.setErrorCode(0);
        dataStore.setErrorMessage("测试错误信息");
        dataStore.setProcessingTime(new BigDecimal(26.23));
        dataStore.setTotalCount(261L);
        System.out.println("java:" + dataStore.toString());
        System.out.println("json:" + TextUtil.toJSON(dataStore));
        System.out.println("xml:" + TextUtil.toXML(dataStore));
    }
}
