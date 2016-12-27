package com.leo618.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.Window;

/**
 * function: 屏幕工具类.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ScreenUtil {

    /**
     * 获取手机屏幕的宽高--全屏
     *
     * @param activity 当前界面activity
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int[] getScreenWH(Activity activity) {
        int[] dimen = new int[2];
        Display disp = activity.getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        dimen[0] = outP.x;
        dimen[1] = outP.y;
        return dimen;
    }

    /**
     * 获取应用app的宽高--高度为全屏不包含状态栏
     *
     * @param activity 当前界面activity
     */
    public static int[] getAppWH(Activity activity) {
        int[] dimen = new int[2];
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        dimen[0] = outRect.width();
        dimen[1] = outRect.height();
        return dimen;
    }

    /**
     * 获取App应用绘制区域的宽高--app页面不包含系统标题栏高度
     *
     * @param activity 当前界面activity
     */
    public static int[] getAppExcludeTitleBarWH(Activity activity) {
        int[] dimen = new int[2];
        Rect outRect = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        dimen[0] = outRect.width();
        dimen[1] = outRect.height();
        return dimen;
    }

    /**
     * 获取系统状态栏高度
     *
     * @param activity 当前界面activity
     */
    public static int[] getStatusBarHeigth(Activity activity) {
        int[] dimen = new int[2];
        int[] screenWH = getScreenWH(activity);
        int[] appExcludeTitleBarWH = getAppWH(activity);
        dimen[0] = screenWH[0];
        dimen[1] = screenWH[1] - appExcludeTitleBarWH[1];
        return dimen;
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
