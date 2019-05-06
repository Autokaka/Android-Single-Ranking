package com.example.tweenanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button btnAlpha,btnScale,btnTrans,btnRotate,btnAll;
    private ImageView mImg;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAll = (Button) findViewById(R.id.btnAll);
        btnAlpha = (Button) findViewById(R.id.btnAlpha);
        btnScale = (Button) findViewById(R.id.btnScale);
        btnRotate = (Button) findViewById(R.id.btnRotate);
        btnTrans = (Button) findViewById(R.id.btnTrans);
        mImg = (ImageView) findViewById(R.id.imageView1);
        btnAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimation = AnimationUtils.loadAnimation(MainActivity.this
                        , R.anim.my_alpha);
                mImg.startAnimation(mAnimation);
            }
        });
        btnScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimation = AnimationUtils.loadAnimation(MainActivity.this
                        , R.anim.my_scale);
                mImg.startAnimation(mAnimation);
            }
        });
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimation = AnimationUtils.loadAnimation(MainActivity.this
                        , R.anim.my_rotate);
                mImg.startAnimation(mAnimation);
            }
        });
        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimation = AnimationUtils.loadAnimation(MainActivity.this
                        , R.anim.my_translate);
                mImg.startAnimation(mAnimation);
            }
        });
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimation = AnimationUtils.loadAnimation(MainActivity.this
                        , R.anim.my_all);
                mImg.startAnimation(mAnimation);
            }
        });
    }
}
