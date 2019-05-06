package com.example.clockdemo;

import android.app.TimePickerDialog;
import android.content.Context;

public class TPDialog extends TimePickerDialog {

    public TPDialog(Context context, OnTimeSetListener callBack, int hourOfDay,
                    int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);
    }

    //重写该方法是为了避免调用两次onTimeSet
    //可以参考 该网址http://www.68idc.cn/help/buildlang/ask/20150206210559.html
    @Override
    protected void onStop() {
        //super.onStop();
    }
}
