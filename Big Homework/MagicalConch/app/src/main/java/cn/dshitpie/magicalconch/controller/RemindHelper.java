package cn.dshitpie.magicalconch.controller;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;

public class RemindHelper {

    private static String TAG = "testapp";

    private boolean isVibrating;

    public RemindHelper() {
        Log.d(TAG, "RemindHelper -> RemindHelper");
        isVibrating = false;
    }

    //检查硬件是否有振动器
    public boolean hasVibrator (final Activity activity) {
        Log.d(TAG, "ReminHelper -> hasVibrator");
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        return vib.hasVibrator();
    }

    //控制手机制动milliseconds毫秒
    public void vibrate (final Activity activity, long milliseconds) {
        Log.d(TAG, "ReminHelper -> vibrate");
        if (!isVibrating) {
            isVibrating = true;
            Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(milliseconds);
        }
    }

    //让手机以指定pattern模式震动
    public void vibrate (final Activity activity, long wait1, long keep1, long wait2, long keep2, int repeat) {
        Log.d(TAG, "ReminHelper -> vibrate");
        if (!isVibrating) {
            isVibrating = true;
            long[] pattern = {wait1, keep1, wait2, keep2};
            Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(pattern,repeat);
        }
    }

    //关闭震动
    public void vibrateCancel (final Activity activity){
        Log.d(TAG, "ReminHelper -> vibrateCancel");
        if (isVibrating) {
            isVibrating = false;
            Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
            vib.cancel();
        }
    }
}
