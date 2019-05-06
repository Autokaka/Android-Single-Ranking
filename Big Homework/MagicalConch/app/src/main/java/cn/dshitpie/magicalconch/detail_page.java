package cn.dshitpie.magicalconch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.dshitpie.magicalconch.R;

import cn.dshitpie.magicalconch.controller.Note;
import cn.dshitpie.magicalconch.controller.NoteOperator;

import java.text.DecimalFormat;
import java.util.HashMap;


public class detail_page extends AppCompatActivity {

    private TextView titleEditText;
    private TextView affairEditText;
    private TextView deadlineEditText;
    private TextView locationEditText;
    private double nowLatitude;
    private double nowLongitude;
    private String nowgeoInfo;
    private boolean isValid = false;
    private Intent intent;
    private String get_title;
    private String get_context;
    private HashMap<String,String> note=new HashMap<String,String>();
    private int num;
    final NoteOperator noteOperator = new NoteOperator(detail_page.this);
    private static final String TAG = "apptest";
    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        mToolBar = (Toolbar)findViewById(R.id.mytoolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        titleEditText = findViewById(R.id.titleEditText);
        affairEditText = findViewById(R.id.affairEditText);
        deadlineEditText = findViewById(R.id.deadlineEditText);
        locationEditText = findViewById(R.id.locationEditText);
        Button saveBT = findViewById(R.id.saveBT);
        ImageView deleteIV = findViewById(R.id.deleteIV);
        ImageView locationIV = findViewById(R.id.locationIV);

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteOperator noteOperator = new NoteOperator(detail_page.this);
                get_title = titleEditText.getText().toString().trim();
                get_context = affairEditText.getText().toString().trim();
                if (v == findViewById(R.id.saveBT)) {
                    if (TextUtils.isEmpty(get_title) || TextUtils.isEmpty(get_context) || TextUtils.isEmpty(nowgeoInfo)) {
                        Toast.makeText(detail_page.this, "添加信息不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if(intent.getBooleanExtra("isRead",false)) {
                            int x;
                            x=intent.getIntExtra("num",-1);
                            noteOperator.delete(x);
                        }
                        Note note = new Note();
                        note.title = get_title;
                        note.location = nowgeoInfo;
                        note.context = get_context;
                        note.latitude = nowLatitude;
                        note.longitude = nowLongitude;
                        if(noteOperator.checkDatabase(note.title)){
                            Toast.makeText(detail_page.this, "数据库已存在该标题，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            boolean add = noteOperator.insert(note);
                            //如果添加数据成功，跳到待办事项界面，并通过传值，让目标界面进行刷新
                            if (add) {
                                //Toast.makeText(AddActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(detail_page.this, "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else if(v == findViewById(R.id.deleteIV)){
                    titleEditText.setText(null);
                    affairEditText.setText(null);
                    deadlineEditText.setText(null);
                    locationEditText.setText(null);
                    isValid = false;

                }
            }
        };
        saveBT.setOnClickListener(listener1);
        deleteIV.setOnClickListener(listener1);

        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳地图
                startMap();
            }
        });

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳地图
                startMap();
            }
        });

        intent = getIntent();
        Log.d(TAG,"isRead ->"+ intent.getBooleanExtra("isRead",false));
        Log.d(TAG,"num->"+intent.getIntExtra("num",-1));
        read();
    }


    private void read()
    {
        if(intent.getBooleanExtra("isRead",false)){
            num=intent.getIntExtra("num",-1);
            note=noteOperator.getonehashmap(num);

            titleEditText.setText(note.get("title"));
            affairEditText.setText(note.get("context"));
            deadlineEditText.setText("世界末日");
            locationEditText.setText(note.get("location"));
            nowLatitude = Double.valueOf(note.get("latitude"));
            nowLongitude = Double.valueOf(note.get("longitude"));
            isValid = true;
            nowgeoInfo = note.get("location");
        }
    }


    public void startMap() {
        Intent intent = new Intent(detail_page.this, MapActivity.class);
        intent.putExtra("Latlng_Latitude_Return", nowLatitude);
        intent.putExtra("Latlng_Longitude_Return", nowLongitude);
        intent.putExtra("geoInfo", nowgeoInfo);
        intent.putExtra("location_is_valid", isValid);
        startActivityForResult(intent, 101);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 101:
                if(resultCode==RESULT_OK){
                    double latitude = data.getDoubleExtra("Latlng_Latitude_Return",91.0);
                    double longitude = data.getDoubleExtra("Latlng_Longitude_Return",181.0);
                    String geoInfo = data.getStringExtra("geoInfo");
                    DecimalFormat myFormat = new DecimalFormat(".000000");
                    String x = myFormat.format(latitude);
                    String y = myFormat.format(longitude);

                    nowLatitude = latitude;
                    nowLongitude = longitude;
                    nowgeoInfo = geoInfo;
                    isValid = true;

                    locationEditText.setText(nowgeoInfo);
                }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

