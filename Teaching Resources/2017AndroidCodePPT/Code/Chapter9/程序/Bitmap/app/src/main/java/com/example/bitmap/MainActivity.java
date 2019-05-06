package com.example.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView img = null;
    Button but = null;
    int count = 0;
    int[] imgRes = {R.drawable.leaf1,R.drawable.leaf2,R.drawable.leaf3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),imgRes[count]);
        img = (ImageView)findViewById(R.id.imageView);
        but = (Button)findViewById(R.id.but_next);
        img.setImageBitmap(bm);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count == 3) {
                    count = 0;
                }
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 2;                   /*2陪缩放图片*/
                opt.inDither = false;                     /*不进行图片抖动处理*/
                opt.inPreferredConfig = null;             /*设置让解码器以最佳方式解码*/
                Bitmap bm2 = BitmapFactory.decodeResource(MainActivity.this.getResources(), imgRes[count], opt);
                img.setImageBitmap(bm2);
            }
        });
    }
}
