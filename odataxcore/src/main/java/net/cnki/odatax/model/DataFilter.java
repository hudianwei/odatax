package net.cnki.odatax.model;

import net.cnki.odatax.core.DataUtil;
import net.cnki.odatax.core.DateTimeUtils;

/**
 * @author hudianwei
 * @date 2018/8/2 15:28
 */
public class DataFilter {

    /**
     * 数据过滤处理
     *
     * @param filters 过滤器，分号分隔
     * @param input   需要处理的数据对象
     * @return 处理后的数据
     */
    public static String process(String filters, Object input) {
        if (DataUtil.isBlank(input)) {
            return "";
        }
        String value = input.toString().replace("\r", "").replace("\n", "")
                .replace("\0", "").trim();
        if (DataUtil.isBlank(filters)) {
            return value.replace("###", "").replace("$$$", "").trim();
        }
        for (String filter : filters.split(";")) {
            String[] args = filter.replace("','", "^").replace("(", "\t")
                    .replace(")", "\t").replace(",", "\t").replace("^", "','")
                    .replace("'", "").trim().split("\t");
            String func = args[0].toUpperCase();

            switch (func.toUpperCase().trim()) {
                case "DATE":
                    value = (args.length > 2) ? filterDate(value, args[1], args[2])
                            : filterDate(value, null, null);
                    break;
                case "INTEGER":
                    value = (args.length > 3) ? filterInteger(value,
                            Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                            Integer.parseInt(args[3])) : filterInteger(value, null,
                            null, null);
                    break;
                case "BOOLEAN":
                    value = (args.length > 2) ? filterBoolean(value, args[1],
                            args[2]) : filterBoolean(value, null, null);
                    break;
                case "MAKEFLAG":
                    value = (args.length > 2) ? filterMakeFlag(value, args[1],
                            args[2]) : filterMakeFlag(value, null, null);
                    break;
                case "MULTIPLE":
                    value = (args.length > 1) ? filterMultiple(value, args[1])
                            : filterMultiple(value, null);
                    break;
                case "FIRST":
                    value = (args.length > 1) ? filterFirst(value, args[1])
                            : filterFirst(value, null);
                    break;
                case "AUTHOR":
                    value = (args.length > 1) ? filterAuthor(value, args[1])
                            : filterAuthor(value, null);
                    break;
                case "ORGANIZATION":
                    value = (args.length > 1) ? filterOrganization(value, args[1])
                            : filterOrganization(value, null);
                    break;
                case "SUMMARY":
                    value = (args.length > 1) ? filterSummary(value,
                            Integer.parseInt(args[1])) : filterSummary(value, null);
                    break;
                case "YEAR":
                    value = (args.length > 3) ? filterYear(value,
                            Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                            Integer.parseInt(args[3])) : filterYear(value, null,
                            null, null);
                    break;
                case "CODENAME":
                    // value = (args.length > 1) ? CodeName(value, args[1])
                    // : String.Empty;
                    break;
                default:
                    break;
            }
        }
        return value.replace("###", "").replace("$$$", "").trim();
    }

    /**
     * <pre>
     * 日期
     * Date(format,default)
     * 示例：
     * Date
     * Date()
     * Date('yyyy-MM-dd', '1900-01-01')
     * Date('yyyy-MM-dd hh:mm:ss', '1900-01-01 00:00:00')
     * @param input 日期字符串
     * @param format 格式，如yyyy-MM-dd
     * @param value 默认值
     * @return 日期
     * </pre>
     */
    private static String filterDate(String input, String format, String value) {
        if (DataUtil.isBlank(format)) {
            format = "yyyy-MM-dd";
        }
        if (DataUtil.isBlank(value)) {
            value = "1900-01-01";
        }

        String formatValue = DateTimeUtils.format(format,
                DateTimeUtils.parseFromUnknownFormateDate(input));
        if (!DataUtil.isBlank(formatValue)) {
            return formatValue;
        }
        return value;
    }

    private static String filterInteger(String input, Integer min, Integer max,
                                        Integer value) {
        Integer result = value;
        if (DataUtil.isBlank(min)) {
            min = Integer.MIN_VALUE;
        }
        if (DataUtil.isBlank(max)) {
            max = Integer.MAX_VALUE;
        }
        if (DataUtil.isBlank(value)) {
            value = 0;
        }
        try {
            result = Integer.parseInt(input);
        } catch (Exception e) {
            return value.toString();
        }
        if (result < min) {
            return value.toString();
        }
        if (result > max) {
            return value.toString();
        }
        return result.toString();

    }

    // / <summary>
    // / 逻辑值
    // / Boolean(true,false)
    // / 示例：
    // / Boolean
    // / Boolean()
    // / Boolean('是','否')
    // / Boolean('Y','N')
    // / </summary>
    // / <param name="input">逻辑值字符串：是,否；Y,N；YesNo；1,0,true,false</param>
    // / <param name="str_true">真值返回的字符串</param>
    // / <param name="str_false">假值返回的字符串</param>
    // / <returns>逻辑值字符串</returns>
    private static String filterBoolean(String input, String str_true,
                                        String str_false) {
        if (DataUtil.isBlank(str_true)) {
            str_true = "true";
        }
        if (DataUtil.isBlank(str_false)) {
            str_false = "false";
        }
        Boolean result = false;

        switch (input.toUpperCase().trim()) {
            case "是":
                result = true;
                break;
            case "YES":
                result = true;
                break;
            case "Y":
                result = true;
                break;
            case "TRUE":
                result = true;
                break;
            case "1":
                result = true;
                break;
            default:
                result = false;
                break;
        }

        if (result)
            return str_true;

        return str_false;
    }

    // / <summary>
    // / 高亮
    // / MakeFlag(prefix,suffix)
    // / 示例：
    // / MakeFlag
    // / MakeFlag()
    // / MakeFlag('<span color=red>','</span>') //参数值不可含有引号
    // / </summary>
    // / <param name="input">字符串</param>
    // / <param name="prefix">高亮时前缀标签</param>
    // / <param name="suffix">高亮时后缀标签</param>
    // / <returns>添加标签后的字符串</returns>
    private static String filterMakeFlag(String input, String prefix,
                                         String suffix) {
        prefix = DataUtil.isBlank(prefix) ? "<span style='color:red'>" : prefix;
        suffix = DataUtil.isBlank(suffix) ? "</span>" : suffix;
        input = input.replace("###", prefix);
        input = input.replace("###", suffix);
        return input;
    }

    // / <summary>
    // / 多值字段
    // / Multiple(separator)
    // / 示例：
    // / Multiple
    // / Multiple(';')
    // / </summary>
    // / <param name="input">多值字符串</param>
    // / <param name="separator">分隔符</param>
    // / <returns>多值字符串</returns>
    private static String filterMultiple(String input, String separator) {
        separator = DataUtil.isBlank(separator) ? ";" : separator;
        String result = input.replace("；", ";").replace("，", ";")
                .replace(",", ";").replace("|", ";").replaceAll("[;]+", ";");
        return result.trim();
    }

    // / <summary>
    // / 第一个值
    // / First(separator)
    // / 示例：
    // / First
    // / First(';')
    // / </summary>
    // / <param name="input">多值字符串</param>
    // / <param name="separator">分隔符</param>
    // / <returns>多值字符串中的第一个值</returns>
    private static String filterFirst(String input, String separator) {
        separator = DataUtil.isBlank(separator) ? ";" : separator;
        String result = input.replace("；", ";").replace("，", ";")
                .replace(",", ";").replace("|", ";").replaceAll("[;]+", ";");
        String value = result.split(";")[0];
        return value.trim();
    }

    // / <summary>
    // / 作者
    // / Author(separator)
    // / 示例：
    // / Author
    // / Author(';')
    // / </summary>
    // / <param name="input">作者，多值</param>
    // / <param name="separator">分隔符</param>
    // / <returns>过滤后的作者信息，多值</returns>
    private static String filterAuthor(String input, String separator) {
        if (DataUtil.isBlank(input)) {
            return "";
        }
        separator = DataUtil.isBlank(separator) ? ";" : separator;
        String result = input.replace("；", ";").replace("，", ";")
                .replace(",", ";").replace("|", ";").replaceAll("[;]+", ";");

        String[] arr = result.split(";");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String author = arr[i];
            author = author.replace(" ", "");
            author = author.replace("?", "");
            arr[i] = author.trim();
            if (DataUtil.isBlank(arr[i])) {
                continue;
            }
            stringBuilder.append(arr[i]);
            stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    // / <summary>
    // / 机构
    // / Organization(separator)
    // / 示例：
    // / Organization
    // / Organization(';')
    // / </summary>
    // / <param name="input">机构，多值</param>
    // / <param name="separator">分隔符</param>
    // / <returns>过滤后的机构信息，多值</returns>
    private static String filterOrganization(String input, String separator) {
        if (DataUtil.isBlank(input)) {
            return "";
        }
        separator = DataUtil.isBlank(separator) ? ";" : separator;
        input = input.replace("；", ";").replace("，", ";").replace(",", ";")
                .replace("|", ";").replaceAll("[;]+", ";");
        String[] arr = input.split(";");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].trim();

            if (DataUtil.isBlank(arr[i])) {
                continue;
            }

            String org = arr[i];

            // 中文开头的机构，截断空格后的数据；英文不处理。此处处理不严谨，但是无法顾全所有数据情况。
            // u4e00-u9fa5
            if (org.indexOf(' ') > 0) {
                org = org.substring(0, org.indexOf(' ')).trim();
            }
            if (org.indexOf('!') > 0)
                org = org.substring(0, org.indexOf('!')).trim();

            arr[i] = org.trim();
            stringBuilder.append(arr[i]);
            stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    // / <summary>
    // / 摘要
    // / Summary(length)
    // / 示例：
    // / Summary
    // / Summary(256)
    // / </summary>
    // / <param name="input">摘要</param>
    // / <param name="length">最大长度</param>
    // / <returns>摘要</returns>
    private static String filterSummary(String input, Integer length) {
        length = DataUtil.isBlank(length) ? 200 : length;
        if (input.length() > length)
            input = input.substring(0, length) + "...";

        return input;
    }

    // / <summary>
    // / 年
    // / Year(min,max,default)
    // / 示例：
    // / Year
    // / Year()
    // / Year(1900,2999,1900)
    // / </summary>
    // / <param name="input">年</param>
    // / <param name="min">最小值</param>
    // / <param name="max">最大值</param>
    // / <param name="value">超出范围后返回的默认值</param>
    // / <returns>年</returns>
    private static String filterYear(String input, Integer min, Integer max,
                                     Integer value) {
        min = DataUtil.isBlank(min) ? 1900 : min;
        max = DataUtil.isBlank(max) ? 2999 : max;
        value = DataUtil.isBlank(value) ? 1900 : value;
        return filterInteger(input, min, max, value);
    }

    // /// <summary>
    // /// 通过代码获取名称
    // /// </summary>
    // /// <param name="input">代码，多值，分号分隔</param>
    // /// <param
    // name="treeName">词典名称，依赖于~\App_Data\ConfigFiles\Data\{Dict}.Tree</param>
    // /// <returns>名称，多值，分号分隔</returns>
    // private static String CodeName(String input, String dict)
    // {
    // var tree = Configuration.Get<DataTree<String>>("Dict", dict, "Tree");
    //
    // String result = String.Empty;
    //
    // foreach(String name in input.Split(';'))
    // result = result +";" + tree.GetValue(name);
    //
    // return result.Trim(';');
    // }
}
