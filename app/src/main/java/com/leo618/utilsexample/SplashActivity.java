package com.leo618.utilsexample;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.leo618.utils.AndroidUtilsCore;
import com.leo618.utils.IntentLauncher;
import com.leo618.utils.UIUtil;

/**
 * function:启动页
 *
 * <p></p>
 * Created by lzj on 2016/1/28.
 */
public class SplashActivity extends AppCompatActivity {

    private SplashPermissionHelper mSplashPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.bg_splash);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(img);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            handleAfterPermissions();
        } else {
            mSplashPermissionHelper = new SplashPermissionHelper(this);
            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            };
            mSplashPermissionHelper.run(permissions, new SplashPermissionHelper.IPermissionCallback() {
                @Override
                public void onPassed() {
                    handleAfterPermissions();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mSplashPermissionHelper != null) {
            mSplashPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSplashPermissionHelper != null) {
            mSplashPermissionHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleAfterPermissions() {
        AndroidUtilsCore.initAfterCheckPermissions();
        UIUtil.postDelayed(mDelayEnterRunnabe, 1000);//延时进入
    }

    private Runnable mDelayEnterRunnabe = new Runnable() {
        @Override
        public void run() {
            IntentLauncher.with(SplashActivity.this).launch(MainActivity.class);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        // cannot go back
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIUtil.removeCallbacksFromMainLooper(mDelayEnterRunnabe);
        mDelayEnterRunnabe = null;
    }
}
