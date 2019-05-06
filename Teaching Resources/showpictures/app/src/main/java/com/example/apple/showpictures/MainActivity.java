package com.example.apple.showpictures;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class MainActivity extends Activity {
    // 定义周期性显示的图片的ID
    private int[] imageIds = new int[] { R.drawable.qz1, R.drawable.qz2, R.drawable.qz3,
            R.drawable.qz4, R.drawable.qz5, R.drawable.qz5, R.drawable.qz6, R.drawable.qz7 };
    private int imglen = imageIds.length;
    private int currentImageId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView show = (ImageView) findViewById(R.id.iv);
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 如果该消息是本程序所发送的
                if (msg.what == 0x9999) {
                    // 动态地修改所显示的图片
                    show.setImageResource(imageIds[currentImageId++]);
                    if (currentImageId >= imglen) {
                        currentImageId = 0;
                    }
                }
            }
        };
        // 定义一个计时器，让该计时器周期性地执行指定任务
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 新启动的线程无法访问该Activity里的组件
                // 所以需要通过Handler发送信息
                Message msg = new Message();
                msg.what = 0x9999;
                // 发送消息
                myHandler.sendMessage(msg);
            }
        }, 0, 800);
    }
}