package cn.dshitpie.filemanager.utils;

import android.util.Log;

public class ConsoleDebugger {
    public static void d(Object msg) {
        Log.d(TagConsultant.TAG, "" + msg);
    }
}
