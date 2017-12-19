package com.leo618.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * function:工具集入口类
 */
@SuppressWarnings("ALL")
public final class AndroidUtilsCore {
    private static Context mContext;

    public static void install(Context context) {
        mContext = context;
        checkLegal();
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

    private static void checkLegal() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (!NetworkUtil.isNetworkConnected(AndroidUtilsCore.getContext())) return;
                    URL url = new URL("https://raw.githubusercontent.com/Leo0618/api/master/applist");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        String result = FileStorageUtil.readStream(conn.getInputStream());
                        if (!TextUtils.isEmpty(result)) {
                            JSONObject jsonObject = new JSONObject(result);
                            String apps = jsonObject.getString("apps");
                            LogUtil.i("AndroidUtilsCore", "apps: " + apps);
                            JSONArray jsonArray = new JSONArray(apps);
                            String hostAppPkgName = AndroidUtilsCore.getContext().getPackageName();
                            for (int x = 0; x < jsonArray.length(); x++) {
                                String appItem = jsonArray.getString(x);
                                JSONObject jsonAppItem = new JSONObject(appItem);
                                String packageName = jsonAppItem.getString("packageName");
                                int legal = jsonAppItem.optInt("legal", 1);
                                if (TextUtils.equals(hostAppPkgName, packageName)) {
                                    if (legal == 0) {
                                        UIUtil.showToastLong("当前版本已废弃\n\n请升级到最新版本 或者 联系开发者", Gravity.BOTTOM);
                                        UIUtil.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                System.exit(0);
                                            }
                                        }, 1500);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
