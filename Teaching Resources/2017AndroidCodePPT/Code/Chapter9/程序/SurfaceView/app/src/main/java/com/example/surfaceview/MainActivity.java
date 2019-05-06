package com.example.surfaceview;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements SurfaceHolder.Callback{
    MediaPlayer mp;
    SurfaceView mySurface;
    SurfaceHolder surfaceHolder;
    Button btnPlay,btnPause,btnStop;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay=(Button)findViewById(R.id.btnStart);
        btnPause=(Button)findViewById(R.id.btnPause);
        btnStop=(Button)findViewById(R.id.btnStop);
        mySurface=(SurfaceView)findViewById(R.id.mySurface);
        mp = new MediaPlayer();
        surfaceHolder=mySurface.getHolder();  //SurfaceHolder是SurfaceView的控制接口
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.setFixedSize(320,220);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);     //Surface类型
        surfaceHolder.addCallback(this);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }});
        btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp.pause();
            }});
        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp.stop();
            }});
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mp.isPlaying()){
            mp.stop();
        }
        //Activity销毁时停止播放，释放资源。不做这个操作，即使退出还是能听到视频播放的声音
        mp.release();
    }

    public void play(){
        try {
            mp.reset();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test1.mp4");
            mp.setDataSource(MainActivity.this,uri);
            mp.setDisplay(mySurface.getHolder());
            mp.prepare();
            mp.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            play();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
