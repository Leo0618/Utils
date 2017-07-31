package com.leo618.utils;

import android.content.Context;

/**
 * function:工具集入口类
 */
@SuppressWarnings("ALL")
public final class AndroidUtilsCore {
    private static Context mContext;

    public static void install(Context context) {
        mContext = context;
    }

    public static void install(Context context, String date) {
        mContext = context;
        FileStorageUtil.checkDate(date);
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new IllegalArgumentException("you must install AndroidUtilsCore with method install(Context context) first.");
        }
        return mContext;
    }

    /**
     * 在检查了动态申请权限之后调用
     */
    public static void initAfterCheckPermissions() {
        // 初始化应用文件目录
        FileStorageUtil.initAppDir();
        // 异常捕获初始化
        CrashHandler.init(getContext());
        // 初始化设备信息
        DeviceInfo.init(getContext());
    }
}
