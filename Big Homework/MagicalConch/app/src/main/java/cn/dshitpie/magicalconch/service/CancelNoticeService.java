package cn.dshitpie.magicalconch.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import cn.dshitpie.magicalconch.R;

public class CancelNoticeService extends Service {

    private static final String TAG = "testapp";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void removeNotification() {
        Log.d(TAG, "CancelService -> removeNotification");
        if (Build.VERSION.SDK_INT >= 26) {
            Notification notification = new Notification.Builder(this, DeskService.NOTICE_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            startForeground (DeskService.NOTICE_ID, notification);
        } else {
            Notification.Builder notification = new Notification.Builder(this);
            notification.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(DeskService.NOTICE_ID, notification.build());
        }
        // 开启一条线程，去移除DaemonService弹出的通知
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 取消CancelNoticeService的前台
                stopForeground(true);
                // 移除DaemonService弹出的通知
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) manager.deleteNotificationChannel(DeskService.NOTICE_CHANNEL_ID);
                manager.cancel(DeskService.NOTICE_ID);
                // 任务完成，终止自己
                stopSelf();
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "CancelService -> onStartCommand");
        removeNotification();
        return super.onStartCommand(intent, flags, startId);
    }
}