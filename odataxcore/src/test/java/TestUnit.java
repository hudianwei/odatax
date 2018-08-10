import net.cnki.odatax.core.*;
import net.cnki.odatax.model.DataModel;
import net.cnki.odatax.model.DataProperty;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static net.cnki.odatax.core.DataUtil.transBean2Map;

/**
 * @Description:
 * @Author: hudianwei
 * @Date:2018/8/4 10:46
 */
public class TestUnit {
    /**
     * @Description:
     * @Param: []
     * @return: void
     * @Author: HU
     * @Date: 2018/8/4
     */

    @Test
    public void MapToBean() {
        PersonBean personBean = new PersonBean();
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("name", "Mike");
        mp.put("age", 28);
        mp.put("mN", "male");
        DataUtil.transMap2Bean(mp, personBean);
        for (Map.Entry<String, Object> entry : mp.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println("Bean Info");
        System.out.println("name: " + personBean.getName());
        // 将javaBean 转换为map
        Map<String, Object> map = transBean2Map(personBean);

        System.out.println("--- transBean2Map Map Info: ");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * @Description: 测试读取配置文件
     * @Param: []
     * @Return: void
     * @Author: HU
     * @Date: 2018/8/8 14:09
     */
    @Test
    public void Configuration() {
        String a = Utils.getRandomNumber(5);
        String driverName = Configuration.getConfig("kbase" + "." + Configuration.DRIVER_NAME);
    }

    /**
     * @Description: 读取文件
     * @Param: []
     * @Return: void
     * @Author: HU
     * @Date: 2018/8/8 14:09
     */
    @Test
    public void FileTest() throws IOException {
        //读取文件I/O分为字符流和字节流
        File file = new File("D:\\logtest\\logtest.log");
        FileReader fr = new FileReader("D:\\logtest\\logtest.log");
        int read = 0;
        while ((read = fr.read()) != -1) {
            System.out.print((char) read);
        }

        String str = FileUtil.read(new FileInputStream("D:\\logtest\\logtest.log"), "UTF-8");
        System.out.print(str);


    }

    /**
     * @Description: 测试生成的sql语句
     * @Param: []
     * @Return: void
     * @Author: HU
     * @Date: 2018/8/8 14:09
     */
    @Test
    public void testFilterByUtil() {
        Caching.put("models", Utils.loadModelsFile());
        //modelMap
        Map<String, DataModel> modelMap = Utils.loadModelsFile();
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
        String filter = "Title@CN   =    11 and (Author@CN eq 456 or Keyword@CN=123 or Author = '123123 12 =  ()()(312312 12312')";
        String order = "Title@CN";
        String group = "Keyword@CN";
        Map<String, Object> sqlMap = Utils.getSqlMap("Conference", null,
                filter, group, order, null, null);
        sqlMap.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        });
    }

    /**
     * @Description:加密测试
     * @Param: []
     * @Return: void
     * @Author: HU
     * @Date: 2018/8/8 14:11
     */
    @Test
    public void CryptographyTest() {

    }
}
