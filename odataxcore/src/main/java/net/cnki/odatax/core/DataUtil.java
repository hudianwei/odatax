package net.cnki.odatax.core;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hudianwei
 * @Description: 数据工具 propertyDescriptior是对一个bean对象属性的描述类，其中有两个方法：
 * ①getReadMethod ②getWriteMethod
 * 分别返回bean对象中这个属性的get和set方法
 * <p>
 * String getName = propertyDescriptior.getReadMethod();
 * String setName = propertyDescriptior.getWriteMethod();
 * @date 2018/8/2 15:26
 */
public class DataUtil {

    /**
     * 判断目标对象是否为空。
     *
     * <pre>
     * DataUtils.isBlank(null)      = true
     * DataUtils.isBlank("")        = true
     * DataUtils.isBlank(" ")       = true
     * DataUtils.isBlank("bob")     = false
     * DataUtils.isBlank(new ArrayList()) = true
     * DataUtils.isBlank(new String[1]) = true
     * </pre>
     *
     * @param o
     *            目标对象。
     * @return 目标对象是否为空。
     */
    public static boolean isBlank(Object o) {
        if (o == null) {
            return true;
        }

        if (o instanceof String) {
            return isBlankCharSequence((String) o);
        } else if (o instanceof Collection) {
            return ((Collection<?>) o).isEmpty();
        } else if (o instanceof Map) {
            return ((Map<?, ?>) o).isEmpty();
        } else if (o instanceof Object[]) {
            return ((Object[]) o).length == 0;
        }

        return false;
    }

    /**
     * @Description: 判断字符序列是否为空
     * @Param: [cs]
     * @return: boolean
     * @Author: HU
     * @Date: 2018/8/3
     */

    private static boolean isBlankCharSequence(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description: JavaBean转换为Map
     * @Param: [clazz, bean]
     * @return: java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Author: HU
     * @Date: 2018/8/3
     */

    public static Map<String, Object> BeanToMap(Class<?> clazz, Object bean) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取指定类的BeanInfo对象
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        //获取所有的属性描述器
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String key = pd.getName();
            Method method = pd.getReadMethod();
            Object value = method.invoke(bean);
            map.put(key, value);
        }
        return map;
    }

    /**
     * @Description: 将map转换为bean
     * @Param: [map, clazz]
     * @return: T
     * @Author: HU
     * @Date: 2018/8/4
     */

    public static <T> T MapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
        //创建JavaBean对象
        T obj = clazz.newInstance();
        //获取指定类的BeanInfo对象
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        //获取所有的属性描述器
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            Object value = map.get(pd.getName());
            Method setter = pd.getWriteMethod();
            setter.invoke(obj, value);
        }
        return obj;
    }

    // Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
    public static void transMap2Bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }

            }

        } catch (Exception e) {
            System.out.println("transMap2Bean Error " + e);
        }

        return;

    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> transBean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }
    /**
     * 返回两个对象是否相等。<br />
     * 允许传空。
     *
     * @param o1
     *            对象1。
     * @param o2
     *            对象2。
     *
     * @return 两个对象是否相等。
     */
    public static boolean isEquals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }
}
