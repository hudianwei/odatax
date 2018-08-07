package net.cnki.odatax.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @ClassName TextUtil
 * @Description 文本工具
 */
public class TextUtil {
    /**
     * email:xxxx@cnki.net
     */
    public static final String REG_EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    /**
     * 手机
     */
    public static final String REG_MOBILE_PHONE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    /**
     * 固话
     */
    public static final String REG_TELE_PHONE = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
    /**
     * URL : http://www.cnki.net
     */
    public static final String REG_URL = "[a-zA-z]+://[^\\s]*";
    /**
     * QQ
     */
    public static final String REG_QQ = "[1-9][0-9]{4,}";
    /**
     * 邮政编码
     */
    public static final String REG_ZIP_CODE = "[1-9]\\d{5}(?!\\d)";

    /**
     * 身份证号
     */
    public static final String REG_ID_CARD = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";

    public static String toJSON(Object obj) {
        return JSON.toJSON(obj).toString();
    }

    public static <T> T parseFromJSON(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr, clazz);
    }

    public static String toXML(Object obj) {
        XStream xstream = new XStream(new DomDriver("utf8"));
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    public static <T> T parseFromXML(String xmlStr, Class<T> clazz) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(clazz);
        @SuppressWarnings("unchecked")
        T t = (T) xstream.fromXML(xmlStr);
        return t;
    }

    /**
     * 正则匹配 ： 校验输入字符串是否符合正则规范
     *
     * @param reg
     * @param str
     * @return
     */
    public static boolean match(String reg, String str) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
