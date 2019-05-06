package com.example.activitylifecycletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class NormalActivity extends AppCompatActivity {

    public static final String TAG2 = "NormalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);
        Intent intent = getIntent();
        if (intent.hasExtra("to_normal_activity")) {//这一行至关重要，因为如果bundle为空，传进来的intent就是空的，不写这一行直接提取不存在的bundle一定会闪退
            Bundle recv = intent.getBundleExtra("to_normal_activity");
            Log.d(TAG2, "bundle & its inside string: \"" + recv.getString("data_key") + "\" from MainActivity is received");
        }
    }
}
