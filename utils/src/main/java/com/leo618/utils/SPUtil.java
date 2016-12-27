package com.leo618.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * function : SharedPreferences管理类.
 */
@SuppressWarnings("unused")
public class SPUtil {
    private static SharedPreferences sp;

    private static SharedPreferences getSp() {
        if (sp == null) {
            Context context = AndroidUtilsCore.getContext();
            sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 获取boolean 数据
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getSp().getBoolean(key, defValue);
    }

    /**
     * 存boolean缓存
     */
    public static void putBoolean(String key, boolean value) {
        getSp().edit().putBoolean(key, value).apply();
    }

    /**
     * 获取boolean 数据
     */
    public static boolean getBoolean(String spFileName, String key, boolean defValue) {
        return AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    /**
     * 存boolean缓存
     */
    public static void putBoolean(String spFileName, String key, boolean value) {
        AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    /**
     * 获取String 数据
     */
    public static String getString(String key, String defValue) {
        return getSp().getString(key, defValue);
    }

    /**
     * 存String缓存
     */
    public static void putString(String key, String value) {
        getSp().edit().putString(key, value).apply();
    }

    /**
     * 获取String 数据
     */
    public static String getString(String spFileName, String key, String defValue) {
        return AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 存String缓存
     */
    public static void putString(String spFileName, String key, String value) {
        AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    /**
     * 获取Long 数据
     */
    public static Long getLong(String key, Long defValue) {
        return getSp().getLong(key, defValue);
    }

    /**
     * 存long缓存
     */
    public static void putLong(String key, Long value) {
        getSp().edit().putLong(key, value).apply();
    }


    /**
     * 获取Long 数据
     */
    public static Long getLong(String spFileName, String key, Long defValue) {
        return AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * 存long缓存
     */
    public static void putLong(String spFileName, String key, Long value) {
        AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putLong(key, value).apply();
    }

    /**
     * 存int缓存
     */
    public static void putInt(String key, int value) {
        getSp().edit().putInt(key, value).apply();
    }

    /**
     * 取int缓存
     */
    public static int getInt(String key, int defValue) {
        return getSp().getInt(key, defValue);
    }


    /**
     * 存int缓存
     */
    public static void putInt(String spFileName, String key, int value) {
        AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    /**
     * 取int缓存
     */
    public static int getInt(String spFileName, String key, int defValue) {
        return AndroidUtilsCore.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getInt(key, defValue);
    }
}
