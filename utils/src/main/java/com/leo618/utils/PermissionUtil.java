package com.leo618.utils;

import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * function:权限工具类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PermissionUtil {
    /**
     * 校验权限列表中是否存在被拒绝的权限
     *
     * @param permissions 权限列表
     * @return 权限列表中还未获取允许的的权限
     */
    public static String[] needCheckPermissions(String[] permissions) {
        List<String> deniedPermissionList = new ArrayList<>();
        if (permissions == null || permissions.length < 1) {
            return deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0 or uper
            for (String permission : permissions) {
                if (PackageManager.PERMISSION_DENIED == AndroidUtilsCore.getContext().checkSelfPermission(permission)) {
                    deniedPermissionList.add(permission);
                }
            }

        }
        return deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
    }

    /**
     * 校验是否需要获取权限认证
     *
     * @param permission 权限名
     * @return 是否需要获取该权限
     */
    public static boolean needCheckPermission(String permission) {
        if (permission == null) {
            return false;
        }
        //6.0 or uper
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                PackageManager.PERMISSION_DENIED == AndroidUtilsCore.getContext().checkSelfPermission(permission);
    }

    /**
     * 校验授权结果是否都是允许
     *
     * @param grantResults 授权结果
     * @return true-全部允许; false-存在拒绝
     */
    public static boolean passPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return true;
        }
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
