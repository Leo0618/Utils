package com.leo618.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * function : 应用相关配置信息获取工具类 管理的对应文件：AndroidManifest.xml 管理设备信息.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PackageManagerUtil {
    private static final String TAG = PackageManagerUtil.class.getSimpleName();

    /**
     * 获取AndroidManifest中指定的meta-data字符串
     *
     * @return 如果没有获取成功(没有对应值, 或者异常)，则返回值为空
     */
    public static String getStringMetaData(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = AndroidUtilsCore.getContext().getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(AndroidUtilsCore.getContext().getPackageName(),
                        PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, "", e);
        }
        return resultData;
    }

    /**
     * 获取AndroidManifest中指定的meta-data整形值
     *
     * @return 如果没有获取成功(没有对应值, 或者异常)，则返回默认值
     */
    public static int getIntMeta(final String metaName, final int defaultValue) {
        int meta = defaultValue;
        try {
            Context context = AndroidUtilsCore.getContext();
            ApplicationInfo appinfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appinfo != null) {
                meta = appinfo.metaData.getInt(metaName, defaultValue);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "", e);
        }
        return meta;
    }

    /**
     * 获取AndroidManifest中指定版本号整形值
     *
     * @return 如果没有获取成功(没有对应值, 或者异常)，则返回值为-1
     */
    public static int getVersionCode() {
        int verCode = -1;
        Context context = AndroidUtilsCore.getContext();
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            if (null != pi.versionName) {
                verCode = pi.versionCode;
            }
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, "", e);
        }
        return verCode;
    }

    /**
     * 获取AndroidManifest中指定的版本号名字符串
     *
     * @return 如果没有获取成功(没有对应值, 或者异常)，则返回值为""
     */
    public static String getVersionName() {
        String verName = "";
        Context context = AndroidUtilsCore.getContext();
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            if (null != pi.versionName) {
                verName = pi.versionName;
            }
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, "", e);
        }
        return verName;
    }

    /**
     * 收集设备参数信息,返回json对象
     */
    public static JSONObject collectDeviceInfo() {
        JSONObject result = new JSONObject();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                result.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtil.d(TAG, "an error occured when collect device info " + e.getMessage());
            }
        }

        return result;
    }

    /**
     * 程序是否在前台
     */
    public static boolean isAppOnForeground() {
        Context context = AndroidUtilsCore.getContext();
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (RunningAppProcessInfo appProcess : appProcesses) {
                // The name of the process that this object is associated with.
                if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用
     */
    public static void exitApp() {
        Context context = AndroidUtilsCore.getContext();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (context.getPackageName().equals(service.service.getPackageName())) {
                Intent stopIntent = new Intent();
                ComponentName serviceCMP = service.service;
                stopIntent.setComponent(serviceCMP);
                context.stopService(stopIntent);
                break;
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 判断apk是否被安装
     *
     * @return 已安装返回true，未安装返回false
     */
    public static boolean isInstalled(String packageName) {
        boolean result = false;
        try {
            PackageInfo packageInfo = AndroidUtilsCore.getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                result = true;
            }
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, TAG + "--->Exception:" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取已安装apk版本号
     *
     * @return 指定已安装的包名apk的版本号
     */
    public static int getVersionCode(String packageName) {
        int versionCode = -1;
        if (packageName == null || "".equals(packageName)) {
            return versionCode;
        }
        try {
            PackageInfo info = AndroidUtilsCore.getContext().getPackageManager().getPackageInfo(packageName, 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, TAG + "--->Exception:" + e.getMessage());
        }
        return versionCode;

    }

    /**
     * 获取手机内部安装的非系统应用 只有基本信息，不包含签名等特殊信息
     *
     * @return 手机内部安装的非系统应用
     */
    public static List<PackageInfo> getInstalledUserApps() {
        PackageManager packageManager = AndroidUtilsCore.getContext().getPackageManager();
        List<PackageInfo> result = new ArrayList<>();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);// 获取手机内所有应用
        for (PackageInfo packageInfo : list) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) { // 判断是否为非系统预装的应用程序
                result.add(packageInfo);
            }
        }
        return result;
    }

    /**
     * 检查当前进程名是否是默认的主进程
     *
     * @return 是否是默认的主进程
     */
    public static boolean checkMainProcess() {
        Context context = AndroidUtilsCore.getContext();
        final String curProcessName = getProcessNameByPid(context, android.os.Process.myPid());
        return isMainProcess(context, curProcessName);
    }

    /**
     * 判断进程是否为主进程
     *
     * @param context     上下文
     * @param processName 进程名称（包名+进程名）
     * @return 是否为主进程
     */
    public static boolean isMainProcess(Context context, String processName) {
        final String mainProcess = context.getApplicationInfo().processName;
        return mainProcess.equals(processName);
    }

    /**
     * 获取当前进程的名称<br/>
     * 通过当前进程id在运行的栈中查找获取进程名称（包名+进程名）
     *
     * @param context 上下文
     * @return 当前进程的名称
     */
    public static String getCurProcessName(Context context) {
        final int curPid = android.os.Process.myPid();
        String curProcessName = getProcessNameByPid(context, curPid);
        if (null == curProcessName) {
            curProcessName = context.getApplicationInfo().processName;
            LogUtil.d(TAG, "getCurProcessName,no find process,curPid=", curPid, ",curProcessName=", curProcessName);
        }
        return curProcessName;
    }

    /**
     * 获取进程的名称<br/>
     * 通过进程id在运行的栈中查找获取进程名称（包名+进程名）
     *
     * @param context 上下文
     * @param pid     指定进程id
     * @return 进程的名称
     */
    public static String getProcessNameByPid(Context context, final int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (RunningAppProcessInfo appProcess : appProcesses) {
                if (pid == appProcess.pid) {
                    processName = appProcess.processName;
                    break;
                }
            }
        }
        LogUtil.d(TAG, "getProcessNameByPid,pid=", pid, ",processName=", processName);
        return processName;
    }

    /**
     * 检查当前进程名是否是包含进程名的进程
     *
     * @param context 上下文
     * @return 当前进程名是否是包含进程名的进程
     */
    public static boolean checkTheProcess(final Context context, String endProcessName) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        int myPid = android.os.Process.myPid();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (RunningAppProcessInfo appProcess : appProcesses) {
                if (myPid == appProcess.pid) {
                    LogUtil.d(TAG, "process.pid appProcess.processName=" + appProcess.processName + ", endProcessName=" + endProcessName);
                    if (appProcess.processName.endsWith(endProcessName)) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前堆栈中的低一个activity
     *
     * @return 当前堆栈中的低一个activity
     */
    @SuppressWarnings("deprecation")
    public static ComponentName getTheProcessBaseActivity() {
        ActivityManager activityManager = (ActivityManager) AndroidUtilsCore.getContext().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo task = activityManager.getRunningTasks(1).get(0);
        if (task.numActivities > 0) {
            LogUtil.d(TAG, "runningActivity topActivity=" + task.topActivity.getClassName());
            LogUtil.d(TAG, "runningActivity baseActivity=" + task.baseActivity.getClassName());
            return task.baseActivity;
        }
        return null;
    }
}
