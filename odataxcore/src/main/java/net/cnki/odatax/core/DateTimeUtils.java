package net.cnki.odatax.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cnki.odatax.exception.SystemException;

public class DateTimeUtils {

    private static final SimpleDateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd");

    private static final SimpleDateFormat STANDARD_DATETIME_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static final int[] CALENDARFIELD = new int[]{Calendar.YEAR,
            Calendar.MONTH, Calendar.DATE, Calendar.HOUR, Calendar.MINUTE,
            Calendar.SECOND};

    /**
     * 比较两个日期。
     *
     * @param date1    第一个日期。
     * @param date2    第二个日期。
     * @param dateOnly 是否只计算日期部分。
     * @return 两个日期比较结果，负数表示date1小于date2，0表示date1等于date2，正数表示date1大于date2。
     */
    public static int compare(Date date1, Date date2, boolean dateOnly) {
        if (date1 == null || date2 == null) {
            throw new NullPointerException();
        }

        int[] values = new int[6];
        int len = dateOnly ? 3 : CALENDARFIELD.length; // 如果不考虑时间，则只计算前三位。
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        // 将date1的年月日时分秒存储在临时数组中。
        for (int i = 0; i < len; i++) {
            values[i] = calendar.get(CALENDARFIELD[i]);
        }

        calendar.setTime(date2);
        int result = 0;
        for (int i = 0; i < len; i++) {
            result = Integer.compare(values[i], calendar.get(CALENDARFIELD[i]));
            // 如果不等于0，说明当前位已经比出了大小，返回这个值，如果是0，继续比较下一位。
            if (result != 0) {
                return result;
            }
        }
        // 执行到这里依然没有比较出大小，则两个值相等。
        return 0;
    }

    /**
     * 计算两个日期差
     *
     * @param date1
     * @param date2
     * @param CalendarType 日历对象类型
     * @return date1-date2的绝对值(不足1月的按照1月算)
     * @throws ParseException
     */
    public static Integer getDateDiff(Date date1, Date date2, int CalendarType)
            throws ParseException {

        int result = 0;

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        if (date1 == null || date2 == null) {
            return null;
        }
        c1.setTime(date1);
        c2.setTime(date2);
        result = c2.get(CalendarType) - c1.get(CalendarType);
        return result == 0 ? 1 : Math.abs(result);
    }

    // /**
    // * 判断第一个日期是否晚于第二个日期。
    // *
    // * @param date1
    // * 第一个日期。
    // * @param date2
    // * 第二个日期。
    // * @param dateOnly
    // * 是否只计算日期部分。
    // * @return 第一个日期是否晚于第二个日期。
    // */
    // public static boolean isBefore(Date date1, Date date2, boolean dateOnly)
    // {
    // if (date1 == null || date2 == null) {
    // return false;
    // }
    //
    // if (dateOnly) {
    // int[] values = new int[3];
    // Calendar calendar = Calendar.getInstance();
    // calendar.setTime(date2);
    // values[0] = calendar.get(Calendar.YEAR);
    // values[1] = calendar.get(Calendar.MONTH);
    // values[2] = calendar.get(Calendar.DATE);
    //
    // calendar.setTime(date1);
    // return calendar.get(Calendar.YEAR) < values[0] ||
    // calendar.get(Calendar.MONTH) == values[1]
    // && calendar.get(Calendar.DATE) == values[2];
    // } else {
    // return date1.before(date2);
    // }
    // }
    //
    // /**
    // * 判断第一个日期是否早于第二个日期。
    // *
    // * @param date1
    // * 第一个日期。
    // * @param date2
    // * 第二个日期。
    // * @param dateOnly
    // * 是否只计算日期部分。
    // * @return 第一个日期是否早于第二个日期。
    // */
    // public static boolean isAfter(Date date1, Date date2, boolean dateOnly) {
    // if (date1 == null || date2 == null) {
    // return false;
    // }
    //
    // if (dateOnly) {
    // int[] values = new int[3];
    // Calendar calendar = Calendar.getInstance();
    // calendar.setTime(date1);
    // values[0] = calendar.get(Calendar.YEAR);
    // values[1] = calendar.get(Calendar.MONTH);
    // values[2] = calendar.get(Calendar.DATE);
    //
    // calendar.setTime(date2);
    // return calendar.get(Calendar.YEAR) == values[0] &&
    // calendar.get(Calendar.MONTH) == values[1]
    // && calendar.get(Calendar.DATE) == values[2];
    // } else {
    // return date1.after(date2);
    // }
    // }

    /**
     * 计算第一个日期是否等于第二个日期。
     *
     * @param date1    第一个日期。
     * @param date2    第二个日期。
     * @param dateOnly 是否只计算日期部分。
     * @return 第一个日期是否等于第二个日期。
     */
    public static boolean isEquals(Date date1, Date date2, boolean dateOnly) {
        if (dateOnly) {
            int[] values = new int[3];
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            values[0] = calendar.get(Calendar.YEAR);
            values[1] = calendar.get(Calendar.MONTH);
            values[2] = calendar.get(Calendar.DATE);

            calendar.setTime(date2);
            return calendar.get(Calendar.YEAR) == values[0]
                    && calendar.get(Calendar.MONTH) == values[1]
                    && calendar.get(Calendar.DATE) == values[2];
        } else {
            return DataUtil.isEquals(date1, date2);
        }
    }

    /**
     * 根据传入的格式格式化日期。
     *
     * @param date 希望格式化的日期对象。
     * @return 日期对应的字符串。
     */
    public static String format(String pattern, Date date) {
        if (DataUtil.isBlank(date)) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 根据传入的格式转换日期对象
     *
     * @param parrern
     * @param source
     * @return
     * @throws ParseException
     */
    public static Date parse(String pattern, String source)
            throws ParseException {
        return new SimpleDateFormat(pattern).parse(source);
    }

    public static String formatStdDate(Date date) {
        return STANDARD_DATE_FORMAT.format(date);
    }

    public static String formatStdDatetime(Date date) {
        return STANDARD_DATETIME_FORMAT.format(date);
    }

    public static Date parseStdDate(String source) {
        try {
            return STANDARD_DATE_FORMAT.parse(source);
        } catch (Throwable e) {
            throw new SystemException(e);
        }
    }

    public static Date parseStdDatetime(String source) {
        try {
            return STANDARD_DATETIME_FORMAT.parse(source);
        } catch (Throwable e) {
            throw new SystemException(e);
        }
    }

    /**
     * 转换未知格式： 字符串支持以下三种: <br>
     * yyyy <br>
     * yyyy*MM <br>
     * yyyy*MM*dd <br>
     *
     * @param source
     * @return
     */
    public static Date parseFromUnknownFormateDate(Object source) {
        if (DataUtil.isBlank(source)) {
            return null;
        }
        if (source instanceof Date) {
            return (Date) source;
        }
        source = ((String) source).replaceAll("[^0-9]", "-");
        // 标准格式类型
        if (((String) source).length() == 10) {
            return DateTimeUtils.parseStdDate((String) source);
        }
        String regexYearMonth = "\\d{4}.\\d{2}";
        String regexYear = "\\d{4}";
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile(regexYearMonth);
        m = p.matcher((String) source);
        if (m.find()) {
            try {
                source = DateTimeUtils.parse("yyyy-MM", (String) source);
            } catch (ParseException e) {
                throw new SystemException(e);
            }
        } else {
            p = Pattern.compile(regexYear);
            m = p.matcher((String) source);
            if (m.find()) {
                try {
                    source = DateTimeUtils.parse("yyyy", (String) source);
                } catch (ParseException e) {
                    throw new SystemException(e);
                }
            }
        }
        return (Date) source;
    }

}
