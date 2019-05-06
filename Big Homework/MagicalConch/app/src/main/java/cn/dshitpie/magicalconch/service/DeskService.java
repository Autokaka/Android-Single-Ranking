package cn.dshitpie.magicalconch.service;

import android.app.*;
import android.os.*;
import com.amap.api.location.*;
import com.amap.api.maps.*;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.amap.api.maps.model.LatLng;
import cn.dshitpie.magicalconch.controller.NoteOperator;
import cn.dshitpie.magicalconch.R;

import java.util.ArrayList;

public class DeskService extends Service {

    private static final String TAG = "testapp";
    //声明数据库操作器
    private NoteOperator operator = null;
    //初始化地点存储容器
    private ArrayList<LatLng> locList = null;
    //初始化事件匹配列表
    ArrayList<LatLng> matchList = null;
    //获取Service启动的全局Intent
    private Intent getIntent = null;
    //初始化定位
    private AMapLocationClient mLocationClient = null;
    public static final int NOTICE_ID = 1;
    public static final String NOTICE_CHANNEL_ID = "LocationListener";

    private class mLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation currentLocation) {
            if (currentLocation != null) {
                if (currentLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    StringBuilder locationInfo = new StringBuilder();
                    locationInfo.append("经度: ").append(currentLocation.getLongitude()).append(" 纬度: ").append(currentLocation.getLatitude()).append(" 方向角: ").append(currentLocation.getBearing());
                    Log.d(TAG, "DeskService -> onLocationChanged -> " + locationInfo);
                    //由当前位置, 设置100m范围内事件匹配列表
                    LatLng curloc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    setMatchList(curloc);
                    if (!matchList.isEmpty() && !getIntent().getBooleanExtra("isRemindDisplayed", false)) {
                        Log.d(TAG, "DeskService -> 监听方法获取的当前matchList -> " + matchList);
                        setRemindDialog(matchList);
                        setLocList();
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + currentLocation.getErrorCode() + ", errInfo:"
                            + currentLocation.getErrorInfo());
                }
            }
        }
    }

    private void setLocOpt(int purpose, int interval, int timeout) {
        //声明AMapLocationClientOption对象
        AMapLocationClientOption option = new AMapLocationClientOption();
        Log.d(TAG, "DeskService -> setAmapOpt");
        //设置定位场景,默认无场景
        switch (purpose) {
            case 1:
                option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
                break;
            case 2:
                option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
                break;
            case 3:
                option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
                break;
            default:
                break;
        }
        //设置定位间隔,默认2s
        option.setInterval(interval);
        //设置gps请求超时时间,默认30s
        option.setHttpTimeOut(timeout);
        mLocationClient.setLocationOption(option);
    }

    private void requestLocation() {
        if(mLocationClient != null){
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
        }
        Log.d(TAG, "DeskService -> requestLocation");
        mLocationClient.startLocation();
    }

    private void setNotification(final String title, final String text) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "DeskService -> setNotification(" + title + ", " + text + ")");
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationManager manager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
                    NotificationChannel channel = new NotificationChannel (NOTICE_CHANNEL_ID, "DeskService", NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                    Notification notification = new Notification.Builder(DeskService.this, DeskService.NOTICE_CHANNEL_ID)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource (getResources (),R.mipmap.ic_launcher))
                            .build();
                    startForeground (NOTICE_ID, notification);
                } else if (Build.VERSION.SDK_INT >= 18 && Build.VERSION.SDK_INT < 26) {
                    Notification.Builder notification = new Notification.Builder(DeskService.this);
                    notification.setSmallIcon(R.mipmap.ic_launcher);
                    notification.setContentTitle(title);
                    notification.setContentText(text);
                    startForeground(NOTICE_ID, notification.build());
                } else {
                    startForeground(NOTICE_ID, new Notification());
                }
            }
        });
    }

    private void removeNotification() {
        //如果觉得常驻通知栏体验不好, 可以通过启动CancelNoticeService将通知移除, oom_adj值不变
        startService(new Intent(this, CancelNoticeService.class));
    }

    private void setMatchList(LatLng curloc) {
        Log.d(TAG, "DeskService -> setMatchList");
        if (matchList == null) matchList = new ArrayList<LatLng>();
        matchList.clear();
        if (locList != null && !locList.isEmpty()) {
            for (int i = 0; i < locList.size(); i++) {
                if (AMapUtils.calculateLineDistance(curloc, locList.get(i)) <= 100) matchList.add(locList.get(i));
            }
        }
    }

    private void setLocList() {
        Log.d(TAG, "DeskService -> setLocList");
        if (locList == null) locList = new ArrayList<LatLng>();
        locList.clear();
        if (operator == null) operator = new NoteOperator(this);
        locList = operator.getLocationList();
        if (locList == null) locList = new ArrayList<LatLng>();
    }

    private void setRemindDialog(ArrayList<LatLng> matchList) {
        Log.d(TAG, "DeskService -> setRemindDialog");
        Intent intent = new Intent();
        intent.setAction("remind_dialog");
        Log.d(TAG, "DeskService -> setRemindDialog -> 传入的matchList: " + matchList);
        intent.putExtra("matchList", matchList);
        startActivity(intent);
    }

    private Intent getIntent() {
        return getIntent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DeskService -> onCreate");
        mLocationClient = new AMapLocationClient(this);
        setLocOpt(3 , 1000, 20000);
        requestLocation();
        //设置定位回调监听
        mLocationClient.setLocationListener(new mLocationListener());
        //连接数据库操作器
        operator = new NoteOperator(this);
        //用操作器拿本地地点列表
        setNotification("[地点提醒服务已开启]", "不必惊慌, 它只是为了能在合适的地点提醒您");
        removeNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "DeskService -> onStartCommand");
        //将获取的intent变为全局的
        getIntent = intent;
        //服务的每次启动都要获取本地数据
        setLocList();
        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "DeskService -> onDestroy");
        super.onDestroy();
        // 如果Service被杀死，干掉通知
        if (Build.VERSION.SDK_INT >= 18) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) manager.deleteNotificationChannel(DeskService.NOTICE_CHANNEL_ID);
            manager.cancel(NOTICE_ID);
        }
        // 重启自己
        startService(new Intent(getApplicationContext(), DeskService.class));
    }

}
