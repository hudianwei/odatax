import net.cnki.odatax.core.HttpUtil;
import org.junit.Test;

/**
 * @Description:get,post方式获取值
 * @author: HU
 * @date: 2018/8/8 16:24
 */
public class HttpUtilTest {
    @Test
    public void testGET() throws Exception {
        System.out.println(HttpUtil.doHttpAndHttps(
                "http://www.baidu.com", "name=yehao&1=1",
                HttpUtil.REQ_METHOD_GET));
    }

    @Test
    public void testPOST() throws Exception {
        System.out.println(HttpUtil.doHttpAndHttps(
                "http://localhost:8080/helloJsp2", "name=lisi&2=2",
                HttpUtil.REQ_METHOD_POST));
    }
}
