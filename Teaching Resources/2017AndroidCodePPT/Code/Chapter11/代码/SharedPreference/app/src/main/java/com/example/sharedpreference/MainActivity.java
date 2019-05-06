package com.example.sharedpreference;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    EditText edName,edAge;
    TextView tvInfo;
    Button btnCommit;

    private static final String FILENAME = "info";  //定义文件名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = (EditText)findViewById(R.id.edname);
        edAge = (EditText)findViewById(R.id.edage);
        btnCommit = (Button)findViewById(R.id.btncommit);
        tvInfo = (TextView)findViewById(R.id.tvinfo);

        SharedPreferences preferences = super.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
        tvInfo.setText("用户信息 姓名：" + preferences.getString("name", "未定义") + "，年龄：" + preferences.getInt("age", 0) + "。");

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("name", edName.getText().toString());
                edit.putInt("age", Integer.parseInt(edAge.getText().toString()));
                edit.commit();
                tvInfo.setText("用户信息 姓名："+edName.getText().toString()+"，年龄："+edAge.getText().toString());
            }
        });
    }
}
