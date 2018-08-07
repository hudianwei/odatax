import net.cnki.odatax.core.Configuration;
import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.core.FileUtil;
import net.cnki.odatax.core.Utils;
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

    @Test
    public void Configuration() {
        String a = Utils.getRandomNumber(5);
        String driverName = Configuration.getConfig("kbase" + "." + Configuration.DRIVER_NAME);
    }

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
}
