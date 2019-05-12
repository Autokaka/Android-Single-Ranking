package cn.dshitpie.habitsgalaxy;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import cn.dshitpie.habitsgalaxy.View.DragScalePlanetView;

public class PlanetActivity extends AppCompatActivity{
    private static final String TAG = "HabitGalaxy";

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);

        Intent intent = getIntent();
        String tree_num = intent.getStringExtra("tree_num");
        plantTree(Integer.valueOf(tree_num));

        //星球图片初始化
        final DragScalePlanetView planetImageView = findViewById(R.id.planet);
        planetImageView.setOnTouchListener(planetImageView);

        Button tree = findViewById(R.id.AddTree);

        tree.setOnClickListener(new View.OnClickListener() {
            int x = 1;
            @Override
            public void onClick(View view) {
                plantTree(x);
                x++;
            }
        });

        Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(50000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        //让旋转动画一直转，不停顿的重点
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
        planetImageView.startAnimation(rotateAnimation);
        planetImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(PlanetActivity.this,ShowActivity.class);
                startActivity(intent);

                return false;
            }
        });
    }

    public void  plantTree(int x){
        DragScalePlanetView planetImageView = findViewById(R.id.planet);
        switch(x){
            case 0:
                planetImageView.setImageResource(R.drawable.tree0);
                break;

            case 1:
                planetImageView.setImageResource(R.drawable.tree1);
                break;

            case 2:
                planetImageView.setImageResource(R.drawable.tree2);
                break;

            case 3:
                planetImageView.setImageResource(R.drawable.tree3);
                break;

            case 4:
                planetImageView.setImageResource(R.drawable.tree4);
                break;

            case 5:
                planetImageView.setImageResource(R.drawable.tree5);
                break;

            case 6:
                planetImageView.setImageResource(R.drawable.tree6);
                break;

            case 7:
                planetImageView.setImageResource(R.drawable.tree7);
                break;

            case 8:
                planetImageView.setImageResource(R.drawable.tree8);
                break;

            case 9:
                planetImageView.setImageResource(R.drawable.tree9);
                break;

            case 10:
                planetImageView.setImageResource(R.drawable.tree10);
                break;

            case 11:
                planetImageView.setImageResource(R.drawable.tree11);
                break;

            case 12:
                planetImageView.setImageResource(R.drawable.tree12);
                break;

            case 13:
                planetImageView.setImageResource(R.drawable.tree13);
                break;

            case 14:
                planetImageView.setImageResource(R.drawable.tree14);
                break;

            case 15:
                planetImageView.setImageResource(R.drawable.tree15);
                break;

            case 16:
                planetImageView.setImageResource(R.drawable.tree16);
                break;

            case 17:
                planetImageView.setImageResource(R.drawable.tree17);
                break;

            case 18:
                planetImageView.setImageResource(R.drawable.tree18);
                break;

            case 19:
                planetImageView.setImageResource(R.drawable.tree19);
                break;

            case 20:
                planetImageView.setImageResource(R.drawable.tree20);
                break;

            case 21:
                planetImageView.setImageResource(R.drawable.tree21);
                ConstraintLayout layout = findViewById(R.id.layout);
                layout.setBackgroundResource(R.drawable.background);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 10) {
                //Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
            } else if(y2 - y1 > 50) {
                //Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();

            } else if(x1 - x2 > 50) {
                //Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
            } else if(x2 - x1 > 50) {
                //Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);
    }

}




