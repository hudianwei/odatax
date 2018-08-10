import net.cnki.odatax.data.service.impl.DemoServiceImpl;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: HU
 * @date: 2018/8/9 19:24
 */
public class TestUnit {
    @Test
    public void findListByFilter() {

        List<Map<String, Object>> findListByFilter=new DemoServiceImpl().findListByFilter(null);
    }
}
