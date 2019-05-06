/*
* 个人感悟：
* 服务（Service）是线程（Task）的载体，服务通过建立继承的绑定类（Bind），响应活动的号召（Activity），对线程建立一对一的上下级操作和监听联系
* UI是线程的门面，它接受线程传达的信息，并以服务为载体进行更新，从而反应线程的状态
* */
package com.example.servicebestpractice;

import android.app.*;
import android.os.*;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import java.io.File;

public class DownloadService extends Service {

    private String downloadUrl;

    /*
    * 导入下载线程
    * */
    private DownloadTask downloadTask;

    /*
    * 构建显示下载进度的通知
    * */
    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);//显示进度条方法（通知最大长度，当前进度，是否启动模糊进度条）
        }
        return builder.build();
    }

    /*
    * 构建上述通知的NotificationManager，以供后续方法调用上方通知
    * */
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /*
    * 实现DownloadListener的接口，以实现Service对UI层的更新：
    * 当DownloadTask（线程）返回成功tag时的onSuccess方法：通过NotificationManager调用本地实现的Notification显示方法，显示下载成功
    * 当DownloadTask（线程）返回成功tag时的onFailed方法：通过NotificationManager调用本地实现的Notification显示方法，显示下载失败
    * 当DownloadTask（线程）返回成功tag时的onPaused方法：通过NotificationManager调用本地实现的Notification显示方法，显示下载暂停
    * 当DownloadTask（线程）返回成功tag时的onCanceled方法：通过NotificationManager调用本地实现的Notification显示方法，显示下载取消
    * */
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Download Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    /*
    * 服务和活动（Activity）的绑定（Bind），响应活动（Activity）的操作，从而进一步实现对线程进行对应的操作：
    * 当对服务进行开始操作时，需要实现建立最初显示的通知，并将服务收到的Url传达给线程叫它下载。这时用到的就是startDownload方法
    * 当对服务进行暂停操作时，需要将调用DownloadTask的监听接口DownloadListener从而暂停服务的线程（DownloadTask），并更新UI
    * 当对服务进行取消操作时，需要将调用DownloadTask的监听接口DownloadListener从而取消服务的线程（DownloadTask），本地删除已下载文件，并更新UI
    * */
    class DownloadBinder extends Binder {

        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);//listener是手写的事件监听，负责监听和响应Task的各种事件
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
