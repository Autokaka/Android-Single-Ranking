package com.as.miniplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 *
 * 暂留5秒跳转
 *
 *
 *
 * */
public class PreActivity extends Activity{
    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==250){
                Intent intent=new Intent(PreActivity.this,MainActivity.class);
                startActivity(intent);//执行跳转页面方法
                finish();//销毁当前页面
            }

        };

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);//加载布局
        WaitThread thread=new WaitThread();//开启线程
        thread.start();
    }
    public class WaitThread extends Thread{//创建线程
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //写一封信
            Message message =new Message();
            message.what=250;
            message.arg1=1;
            handler.sendMessage(message);//发送
        }
    }
}
