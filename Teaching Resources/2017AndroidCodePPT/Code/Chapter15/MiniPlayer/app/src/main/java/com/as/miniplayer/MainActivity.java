package com.as.miniplayer;

import java.io.IOException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
/***implements是实现监听***/
public class MainActivity extends Activity implements OnClickListener {
    private TextView musicName;//上边显示歌名
    private Button menu;//菜单按钮
    private ImageView image;//显示图片
    private TextView startTime;
    private TextView countTime;
    private SeekBar seekBar;
    private Button play;
    private Button stop;
    private Button next;
    private Button last;
    private MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        player=MediaPlayer.create(this, R.raw.celebration);//初始化音乐播放器
        seekBar.setMax(player.getDuration());//设最大值
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if(fromUser){
                    player.seekTo(progress);
                }
            }
        });
    }
    private void initView() {
        // TODO Auto-generated method stub
        musicName=    (TextView) findViewById(R.id.musicname);
        menu=(Button) findViewById(R.id.meun);
        image=(ImageView) findViewById(R.id.image);
        startTime=(TextView) findViewById(R.id.starttime);
        countTime=(TextView) findViewById(R.id.overtime);
        seekBar=(SeekBar) findViewById(R.id.seekbar);
        play=(Button) findViewById(R.id.stop);
        stop=(Button) findViewById(R.id.swit);
        next=(Button) findViewById(R.id.right);
        last=(Button) findViewById(R.id.left);
        menu.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        last.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id=v.getId();//点击按钮的id
        switch (id) {
            case R.id.meun:

                break;
            case R.id.stop:
                player.start();
                handler.post(run);//开启
                break;
            case R.id.swit:
                player.stop();
                try {
                    player.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.right:

                break;
            case R.id.left:

                break;
        }
    }
    Handler handler=new Handler();
    Runnable run= new Runnable() {

        @Override
        public void run() {
            seekBar.setProgress(player.getCurrentPosition());//音乐播放器当前值
            setTime();
            handler.postDelayed(run, 200);//延时调用
        }
    };
    public void setTime(){//设置时间的方法
        int now =player.getCurrentPosition();//获取当前播放速度
        int count =player.getDuration();//获取总进度
        int second = now/1000;//当前有多少秒
        int csecond =count/1000;//总共有多少秒
        startTime.setText(second/60+":"+second%60);
        countTime.setText(csecond/60+":"+csecond%60);
    }
    @Override   //按键监听事件  keyCode是按下键的编码
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK){
            player.stop();
            player.release();//释放资源
            handler.removeCallbacks(run);//线程移除
            finish();		}
        return super.onKeyDown(keyCode, event);
    }
}