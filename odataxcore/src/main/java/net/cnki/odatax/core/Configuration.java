package net.cnki.odatax.core;

import org.omg.CORBA.SystemException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author hudianwei
 * @date 2018/8/2 15:25
 */
/*数据库配置文件*/
public class Configuration {
    public static final String DRIVER_NAME = "driverName";
    public static final String URL = "url";
    public static final String USER_NAME = "userName";
    public static final String USER_PWD = "userPwd";

    public static final String KBASE_DRIVER_NAME = "com.kbase.jdbc.Driver";
    private Map<String, String> propertyMap = new HashMap<String, String>();
    private static final Configuration INSTANCE = (Configuration) new Configuration().loadProperties();

    public static String getConfig(String propertyName) {
        return INSTANCE.propertyMap.get(propertyName);
    }

    protected Configuration loadProperties() {
        InputStream is = null;
        try {
            Properties props = new Properties();
            is = Configuration.class.getResourceAsStream("/configuration.properties");
            if (is != null) {
                props.load(is);
                this.loadProperties(props);
            }

        } catch (Throwable e) {
            throw new net.cnki.odatax.exception.SystemException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new net.cnki.odatax.exception.SystemException(e);
                }
            }
        }
        return this;
    }

    private void loadProperties(Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            this.propertyMap.put(entry.getKey().toString(), entry.getValue().toString());
        }
    }
}
