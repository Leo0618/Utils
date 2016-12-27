package com.leo618.utilsexample;

import android.app.Application;

import com.leo618.utils.AndroidUtilsCore;

/**
 * function:
 *
 * <p></p>
 * Created by lzj on 2016/12/27.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidUtilsCore.install(this);
    }
}
