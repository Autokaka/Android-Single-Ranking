package com.example.videoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test5_6.mp4");
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(uri,"video/mp4");
        startActivity(it);
    }
}
