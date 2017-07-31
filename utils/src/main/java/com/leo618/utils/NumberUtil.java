package com.leo618.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * function : 数值相关工具类.
 * <p></p>
 * Created by lzj on 2015/11/4.
 */
@SuppressWarnings("unused")
public class NumberUtil {

    /**
     * 字符串转float
     */
    public static float parseFloat(String content) {
        float result = 0f;
        if (TextUtils.isEmpty(content)) return result;
        try {
            if (content.startsWith(".")) content = "0" + content;
            if (content.contains("*")) content = content.replace("*", "");
            if (content.contains(",")) content = content.replace(",", "");
            if (content.contains("%")) content = content.replace("%", "");

            result = Float.parseFloat(content);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 数字字符串转double(已处理异常)
     */
    public static double parseDouble(String content) {
        if (TextUtils.isEmpty(content)) return -1;
        try {
            return Double.parseDouble(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 数字字符串转int(已处理异常)
     */
    public static int parseInt(String content) {
        if (TextUtils.isEmpty(content)) return -1;
        try {
            return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 数字字符串转long(已处理异常)
     */
    public static long parseLong(String content) {
        if (TextUtils.isEmpty(content)) return -1;
        try {
            return Long.parseLong(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String parseFloatWith2Dec(String info) {
        if (TextUtils.isEmpty(info) || info.contains("--")) {
            return "--";
        }
        return String.valueOf(NumberUtil.parseFloat(info));
    }

    public static String makePrettySizeString(long number) {
        double kiloByte = number / 1024;
        if (kiloByte < 1) {
            return number + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 格式化视频时长
     *
     * @param time 毫秒级 时长
     * @return 格式化后的字符串 如： 12:33
     */
    public static String formatVideoDuration(long time) {
        return StringUtil.format("%02d", time / 60) + ":"
                + StringUtil.format("%02d", time % 60);
    }
}
