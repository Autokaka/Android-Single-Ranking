package com.example.frameanimation;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImg;
    private AnimationDrawable mAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImg = (ImageView) findViewById(R.id.imageView1);
        mImg.setBackgroundResource(R.drawable.my_frameanimation);
        mAd = (AnimationDrawable) MainActivity.this.mImg.getBackground();
        mAd.start();
    }
}
