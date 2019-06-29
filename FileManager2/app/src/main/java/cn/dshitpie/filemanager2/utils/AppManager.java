package cn.dshitpie.filemanager2.utils;

import android.app.Application;
import android.content.Context;

public class AppManager extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
