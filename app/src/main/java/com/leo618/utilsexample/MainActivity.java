package com.leo618.utilsexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.leo618.utils.UIUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                UIUtil.showToastShort("hello");
            }
        }, 2000);
    }
}
