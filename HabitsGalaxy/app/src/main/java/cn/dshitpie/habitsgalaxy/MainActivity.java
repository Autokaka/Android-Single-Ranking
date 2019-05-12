package cn.dshitpie.habitsgalaxy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.dshitpie.habitsgalaxy.DataBase.DBHelper;
import cn.dshitpie.habitsgalaxy.DataBase.Planet;
import cn.dshitpie.habitsgalaxy.View.CustomVideoView;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "HabitsGalaxy";

    //初始化全局数据库
    private DBHelper dbHelper;
    //创建播放视频的控件对象
    private CustomVideoView mVideoView;
    //声明星球视图列表
    private ArrayList<ImageView> planetView = new ArrayList<ImageView>();
    //声明星球数据列表
    private ArrayList<Planet> planetData = new ArrayList<Planet>();

    private int intent_i;
    //初始化动态背景
    private void initBackground() {

        Log.d(TAG, "initView");

        //设置视频背景组件
        mVideoView =  findViewById(R.id.videoview);
        //设置将要播放视频文件的加载路径仅支持 3gp、MP4、avi
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.activity_main_bg));//此处播放/res/raw下，也可以播放其他路径的调用对应的方法设置即可
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = new MediaPlayer();
                }
                mp.setVolume(0f, 0f);//设置0,0为静音时
                mp.setLooping(true);
                mp.start();
            }
        });
        mVideoView.setFocusable(false);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVideoView.start();
            }
        });

    }

    //初始化星球视图
    private void initPlanetView() {

        //先获取数据库内所有星球信息
        planetView.add((ImageView) findViewById(R.id.planet0));
        planetView.add((ImageView) findViewById(R.id.planet1));
        planetView.add((ImageView) findViewById(R.id.planet2));
        planetView.add((ImageView) findViewById(R.id.planet3));
        planetView.add((ImageView) findViewById(R.id.planet4));
        planetView.add((ImageView) findViewById(R.id.planet5));
        updatePlanetView();
        //设置事件监听
        for (intent_i = 0; intent_i < planetView.size(); intent_i++) {
            planetView.get(intent_i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PlanetActivity.class);
                    intent.putExtra("tree_num", planetData.get(intent_i).TREE_NUM);
                    startActivity(intent);
                }
            });
        }
        intent_i = 0;

    }

    //设置视频自适应
    private void setFullScreen() {

        Log.d(TAG, "setFullScreen");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //更新星球状态
    private void updatePlanetView() {

        Log.d(TAG, "updatePlanets");

        //先从数据库获取星球数据
        planetData = dbHelper.getAllPlanets();
        for (int i = 0; i < planetData.size(); i++) {
            int treeNum = Integer.valueOf(planetData.get(i).TREE_NUM);
            if (treeNum != 0) planetView.get(Integer.parseInt(planetData.get(i).PLANET_ID)).setImageResource(R_drawable_planet(treeNum));
        }

    }

    //设置数据库数据
    private void initDataBase() {
        dbHelper = new DBHelper(MainActivity.this);
        for (int i = 0; i < 6; i++) dbHelper.insertPlanet(new Planet(Integer.toString(i), null, "0", null, null));
    }

    //获取drawable文件
    int R_drawable_planet(int No) {

        Log.d(TAG, "R_drawable_planet");

        switch (No) {
            default:
                return -1;
            case 1:
                return R.drawable.tree1;
            case 2:
                return R.drawable.tree2;
            case 3:
                return R.drawable.tree3;
            case 4:
                return R.drawable.tree4;
            case 5:
                return R.drawable.tree5;
            case 6:
                return R.drawable.tree6;
            case 7:
                return R.drawable.tree7;
            case 8:
                return R.drawable.tree8;
            case 9:
                return R.drawable.tree9;
            case 10:
                return R.drawable.tree10;
            case 11:
                return R.drawable.tree11;
            case 12:
                return R.drawable.tree12;
            case 13:
                return R.drawable.tree13;
            case 14:
                return R.drawable.tree14;
            case 15:
                return R.drawable.tree15;
            case 16:
                return R.drawable.tree16;
            case 17:
                return R.drawable.tree17;
            case 18:
                return R.drawable.tree18;
            case 19:
                return R.drawable.tree19;
            case 20:
                return R.drawable.tree20;
            case 21:
                return R.drawable.tree21;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        initDataBase();
        setFullScreen();
        setContentView(R.layout.activity_main);
        initBackground();
        initPlanetView();

    }

    @Override
    protected void onRestart() {

        Log.d(TAG, "onRestart");

        //返回时重新加载视频，防止退出或返回时视频黑屏
        initBackground();
        super.onRestart();
    }

    @Override
    protected void onPause() {

        Log.d(TAG, "onPause");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVideoView.stopNestedScroll();
        }
        super.onPause();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {

        Log.d(TAG, "onStop");

        if(mVideoView !=null) {
            mVideoView.stopPlayback();
        }
        super.onStop();
    }

}
