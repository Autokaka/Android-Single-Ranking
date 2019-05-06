package com.example.gpstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.example.gpstest.DbDebugEnvironment.NoteOperator;

import java.util.ArrayList;

public class RemindDialog extends AppCompatActivity {

    private static final String TAG = "testapp";
    private TextView title;
    private TextView text;
    private Button btnOk;
    private Button btnCancel;
    //声明匹配列表
    private ArrayList<LatLng> matchList = null;
    //声明事件详细
    private ArrayList<String> matchAffair = null;
    //声明数据库操作器
    private NoteOperator operator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ReminDialog -> onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_dialog);
        //设置Activity宽高
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (getWindowManager().getDefaultDisplay().getHeight() * 0.3); // 高度设置为屏幕的0.3
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.65); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(params);
        //告诉DeskService: 前台Activity正在显示
        notifyDeskService(true);
        //连接数据库操作器
        operator = new NoteOperator(this);
        //获取从DeskService传入的匹配列表
        setMatchList();
        //设置匹配列表中查到的对应事件详细
        setMatchAffair(matchList);

        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        title.setText("[附近有事务待办]");

        setAffairDisplay(matchAffair);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator.deleteAffair(matchList);
                notifyDeskService(false);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void setMatchList() {
        if (matchList == null) matchList = new ArrayList<LatLng>();
        matchList.clear();
        matchList = (ArrayList<LatLng>) getIntent().getSerializableExtra("matchList");
        Log.d(TAG, "RemindDialog -> setMatchList -> 接收matchList: " + matchList);
        if (matchList == null) matchList = new ArrayList<LatLng>();
    }

    private void setMatchAffair(ArrayList<LatLng> matchList) {
        if (matchAffair == null) matchAffair = new ArrayList<String>();
        matchAffair.clear();
        if (operator == null) operator = new NoteOperator(this);
        if (matchList != null && !matchList.isEmpty()) matchAffair = operator.getAffair(matchList);
    }

    private void setAffairDisplay(ArrayList<String> matchAffair) {
        if (matchAffair == null) matchAffair = null;
        StringBuilder tmpText = new StringBuilder();
        if (matchAffair.isEmpty()) tmpText.append("获取事件失败, 这应该不会发生, 请联系开发者解决问题");
        for (int i = 0; i < matchAffair.size(); i++) tmpText.append(matchAffair.get(i) ).append("\n");
        text.setText(tmpText);
    }

    private void notifyDeskService(boolean status) {
        Intent intent = new Intent(RemindDialog.this, DeskService.class);
        intent.putExtra("isRemindDisplayed", status);
        startService(intent);
    }
}
