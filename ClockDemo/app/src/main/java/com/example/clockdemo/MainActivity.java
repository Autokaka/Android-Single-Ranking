package com.example.clockdemo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testapp";
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity -> onCreate");
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        // 为TabHost添加标签
        // 新建一个newTabSpec(newTabSpec)用来指定该标签的id（就是用来区分标签）的
        // 设置其标签和图表(setIndicator)
        // 设置内容(setContent)
        /*
         * 设置选项卡 : -- 设置按钮名称 : setIndicator(时钟); -- 设置选项卡内容 : setContent(),
         * 可以设置视图组件, 可以设置Activity, 也可以设置Fragement;
         */
        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("时钟")
                .setContent(R.id.tabTime));
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("闹钟")
                .setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("计时器")
                .setContent(R.id.tabTimer));
        tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("秒表")
                .setContent(R.id.tabStopWatch));
    }
}
