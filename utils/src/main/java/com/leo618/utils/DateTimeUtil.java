package com.leo618.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * function : 日期时间工具类.
 */
@SuppressWarnings({"WeakerAccess", "unused", "deprecation"})
@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {

    /** 日期格式：yyyy-MM-dd HH:mm:ss **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /** 日期格式：yyyy-MM-dd HH:mm **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /** 日期格式：yyyy-MM-dd HH:mm **/
    public static final String DF_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    /** 日期格式：yyyy-MM-dd **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
    /** 日期格式：yyyy-MM **/
    public static final String DF_YYYY_MM = "yyyy-MM";
    /** 日期格式：MM-dd **/
    public static final String DF_MM_DD = "MM-dd";
    /** 日期格式：yyyy-MM-dd **/
    public static final String DF_YYYY_MM_DD_DOT = "yyyy.MM.dd";
    /** 日期格式：HH:mm:ss **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";
    /** 日期格式：HH:mm **/
    public static final String DF_HH_MM = "HH:mm";
    /** 日期格式：MM月dd日 **/
    public static final String DF_MM_YUE_DD = "MM月dd日";
    /** 日期格式：MM月dd HH:mm **/
    public static final String DF_MM_DD_HH_MM = "MM月dd日 HH:mm";
    /** 日期格式：yyyy年MM月dd日 **/
    public static final String DF_YYYYMMDD = "yyyy年MM月dd日";
    /** 日期格式：yyyy年 **/
    public static final String DF_YYYY_N = "yyyy年";
    /** 日期格式：yyyy年 **/
    public static final String DF_YYYY = "yyyy";
    /** 日期格式：dd **/
    public static final String DF_DD = "dd";

    /** 1秒 */
    public final static long second = 1000;
    /** 1分钟 */
    public final static long minute = 60 * 1000;
    /** 1小时 */
    public final static long hour = 60 * minute;
    /** 1天 */
    public final static long day = 24 * hour;
    /** 1月 */
    public final static long month = 31 * day;
    /** 1年 */
    public final static long year = 12 * month;

    /** Log输出标识 **/
    private static final String TAG = "DateTimeUtil";

    /**
     * 获取当前时间,单位为秒
     *
     * @return 返回当前时间 秒单位
     */
    public static long getCurrentTimeSeconds() {
        return getCurrentDate().getTime() / 1000;
    }

    /**
     * 获取当前时间,单位为毫秒
     *
     * @return 返回当前时间 毫秒单位
     */
    public static long getCurrentTimeMills() {
        return getCurrentDate().getTime();
    }

    /**
     * 获取系统当前日期
     *
     * @return 返回当前日期
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取间隔时间
     *
     * @param currentDate 参考时间(当前时间)
     * @param otherDate   比较时间 (>参考时间)
     * @return 格式化后的间隔时间 eg: 1年3个月5天7小时10分钟 、 3个月10分钟
     */
    public static String getIntervalTime(Date currentDate, Date otherDate) {
        String intervalTime = "";
        if (currentDate == null || otherDate == null) {
            return intervalTime;
        }
        long diff = otherDate.getTime() - currentDate.getTime();
        if (diff > year) {
            intervalTime += (diff / year) + "年";
            diff = diff % year;
        }
        if (diff > month) {
            intervalTime += (diff / month) + "个月";
            diff = diff % month;
        }
        if (diff > day) {
            intervalTime += (diff / day) + "天";
            diff = diff % day;
        }
        if (diff > hour) {
            intervalTime += (diff / hour) + "小时";
            diff = diff % hour;
        }
        if (diff > minute) {
            intervalTime += (diff / minute) + "分钟";
        }
        return intervalTime;
    }

    /**
     * 格式化倒计时显示字符串(显示精确到秒)
     *
     * @param time 时间
     * @return 格式化后的倒计时字符串 eg: 1天10分钟30秒
     */
    public static String formatTimeFriendly2Sec(long time) {
        time = time - new Date().getTime();
        if (time <= 1000L) {
            return "00:00:00";
        }
        String result = "";
        if (time > day) {
            result += (time / day) + "天";
            time = time % day;
        }
        if (time > hour) {
            result += (time / hour) + "小时";
            time = time % hour;
        }
        if (time > minute) {
            result += (time / minute) + "分钟";
            time = time % minute;
        }
        if (time > second) {
            result += (time / second) + "秒";
        }
        return result;
    }

    /**
     * 格式化倒计时显示字符串(显示精确到分)
     *
     * @param time 时间
     * @return 格式化后的倒计时字符串 eg: 1天10分钟
     */
    public static String formatTimeFriendly2Minute(long time) {
        time = time - new Date().getTime();
        if (time <= 1000L) {
            return "00:00";
        }
        String result = "";
        if (time > day) {
            result += (time / day) + "天";
            time = time % day;
        }
        if (time > hour) {
            result += (time / hour) + "小时";
            time = time % hour;
        }
        if (time > minute) {
            result += (time / minute) + "分钟";
        }
        return result;
    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return "";
        }
        long diff = new Date().getTime() - date.getTime();
        long r;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 判断是否为今年
     */
    public static boolean isThisYear(long time) {
        Date newDate = new Date(time);
        String dateTime = formatDate(newDate, DF_YYYY_N);
        String nowTime = formatDate(new Date(), DF_YYYY_N);
        return !(TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(nowTime)) && dateTime.equalsIgnoreCase(nowTime);
    }

    /**
     * 判断是否为当天
     */
    public static boolean isToday(long time) {
        Date newDate = new Date(time);
        String dateTime = formatDate(newDate, DF_YYYY_MM_DD);
        String nowTime = formatDate(new Date(), DF_YYYY_MM_DD);
        return !(TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(nowTime)) && dateTime.equalsIgnoreCase(nowTime);
    }

    /**
     * 判断指定时间是否为昨天
     */
    public static boolean isYeasterday(long time) {
        Date newDate = new Date(time);
        newDate.setDate(newDate.getDate() + 1);
        String dateTime = formatDate(newDate, DF_YYYY_MM_DD);
        String nowTime = formatDate(new Date(), DF_YYYY_MM_DD);
        return !(TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(nowTime)) && dateTime.equalsIgnoreCase(nowTime);
    }

    /**
     * 格式化时间
     */
    public static String formatDate(long dateL, String formater) {
        return new SimpleDateFormat(formater).format(new Date(dateL));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(Date date, String formater) {
        return new SimpleDateFormat(formater).format(date);
    }

    /**
     * 将日期字符串转成日期
     */
    public static Date parseDate(String strDate, String formater) {
        DateFormat dateFormat = new SimpleDateFormat(formater);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 验证日期是否比当前日期早
     *
     * @param target1 比较时间1
     * @param target2 比较时间2
     * @return true 则代表target1比target2晚或等于target2，否则比target2早
     */
    public static boolean compareDate(Date target1, Date target2) {
        boolean flag = false;
        try {
            String target1DateTime = DateTimeUtil.formatDate(target1, DF_YYYY_MM_DD_HH_MM_SS);
            String target2DateTime = DateTimeUtil.formatDate(target2, DF_YYYY_MM_DD_HH_MM_SS);
            if (target1DateTime.compareTo(target2DateTime) <= 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 对日期进行增加操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     */
    public static Date addDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }
        return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 对日期进行相减操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     */
    public static Date subDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }
        return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 获取日期字符串对应的星期
     *
     * @param strDate 日期字符串
     * @return 1-7：对应周日-周六，国外周日为开始，周六为结束
     */
    public static int getDayOfWeekFromStrDate(String strDate, String formater) {
        Date date = parseDate(strDate, formater);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }
}
