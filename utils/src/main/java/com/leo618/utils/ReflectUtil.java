package com.leo618.utils;

import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * function:反射工具类
 *
 * <p></p>
 * Created by lzj on 2017/3/7.
 */
@SuppressWarnings("ALL")
public final class ReflectUtil {
    private ReflectUtil() {
    }

    /**
     * 反射修改TextView的属性值
     *
     * @param object   被修改对象
     * @param attrName 属性名字
     * @param value    修改后的属性值
     */
    public static void reflectTextViewAttr(Object object, String attrName, Object value) {
        try {
            Field f = TextView.class.getDeclaredField(attrName);
            f.setAccessible(true);
            f.set(object, value);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
