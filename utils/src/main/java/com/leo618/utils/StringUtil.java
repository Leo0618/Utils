package com.leo618.utils;

import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.Locale;

/**
 * function : 字符串工具类.
 * <p></p>
 * Created by lzj on 2015/11/4.
 */
@SuppressWarnings({"unused", "WeakerAccess", "deprecation"})
public class StringUtil {

    /** 是否是手号码 */
    public static boolean isTelNumber(String telnum) {
        return telnum.matches("^[1][34578][0-9]{9}$");
    }

    /** 隐藏手号码中间四位 */
    public static String formatPhoneNumber(String telnum) {
        if (telnum == null || telnum.length() == 0) {
            return null;
        }
        if (!isTelNumber(telnum)) {
            return telnum;
        }
        return telnum.substring(0, 3) + "****" + telnum.substring(7, telnum.length());
    }


    /** 格式化字符串 */
    public static String format(int strResId, Object... args) {
        return format(UIUtil.getString(strResId), args);
    }

    /** 格式化字符串 */
    public static String format(String formatStr, Object... args) {
        return String.format(Locale.getDefault(), formatStr, args);
    }

    /**
     * 应用指定的字体到文字显示上
     *
     * @param targetTextView 目标TextView
     * @param fontFileName   需要设置的字体文件名(字体文件需要放置在assets/fonts目录下) 如 HandmadeTypewriter.ttf
     */
    public static void applyFont(TextView targetTextView, String fontFileName) {
        try {
            targetTextView.setTypeface(Typeface.createFromAsset(AndroidUtilsCore.getContext().getAssets(), "fonts/" + fontFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 显示不同颜色字符串 */
    public static SpannableStringBuilder colorSubString(String contentStr, int colorForTarget, String targetString) {
        if (TextUtils.isEmpty(contentStr)) contentStr = "";
        SpannableStringBuilder style = new SpannableStringBuilder(contentStr);
        if (targetString != null && contentStr.contains(targetString)) {
            int start = contentStr.indexOf(targetString);
            style.setSpan(new ForegroundColorSpan(colorForTarget), start, start + targetString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return style;
    }

    /** 显示不同颜色字符串 */
    public static Spanned colorTextString(String prefixString, int colorForTarget, String targetString) {
        return Html.fromHtml(prefixString + "<font color='" + colorForTarget + "'>" + targetString + "</font>");
    }
}
