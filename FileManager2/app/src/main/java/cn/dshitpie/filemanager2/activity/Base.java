package cn.dshitpie.filemanager2.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import cn.dshitpie.filemanager2.event.BindEventBus;
import cn.dshitpie.filemanager2.utils.ActivityCollector;

public class Base extends AppCompatActivity {

    /**
     * 设置Activity的宽和高的比例, 显示的位置
     * */
    protected void setActivityDisplay(double heightScale, double widthScale, int gravity) {
        //设置Activity宽高
        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        params.height = (int) (metrics.heightPixels * heightScale);
        params.width = (int) (metrics.widthPixels * widthScale);
        getWindow().setAttributes(params);
        getWindow().setGravity(gravity);
    }

    /**
     * 设置Activity内的某个EditText的输入法弹出方法, 调用此方法就能弹出输入法
     * */
    protected void showSoftInputWhenReady(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 250);
    }

    /**
     * 继承了Base的Activity会: 1. 根据BindEventBus注释, 全局性注册EventBus, 2. 添加自身在ActivityCollector的记录
     * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("当前活动: " + this.getClass().getSimpleName() + " 当前this: " + this);
        ActivityCollector.addActivity(this);
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) EventBus.getDefault().register(this);
    }

    /**
     * 继承了Base的Activity会: 1. 根据BindEventBus注释, 全局性解绑EventBus, 2. 删除自身在ActivityCollector的记录
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        super.finish();
        if (!isFinishing()) android.os.Process.killProcess(android.os.Process.myPid());
    }
}
