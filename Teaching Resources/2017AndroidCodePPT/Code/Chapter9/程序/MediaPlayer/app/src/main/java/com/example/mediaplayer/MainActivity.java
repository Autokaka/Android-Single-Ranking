package com.example.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

    private MediaPlayer mediaPlayer;
    private TextView tvState;
    private Button btnPlay,btnPause,btnStop,btnRestart;
    private SeekBar seekBar;
    private boolean playFlag = false,pauseFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay = (Button)this.findViewById(R.id.btnStart);
        btnPause = (Button) this.findViewById(R.id.btnPause);
        btnStop = (Button) this.findViewById(R.id.btnEnd);
        btnRestart= (Button) this.findViewById(R.id.btnRestart);
        tvState= (TextView)this.findViewById(R.id.tvState);
        seekBar = (SeekBar)this.findViewById(R.id.seekBar);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.my_music);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnStart:
                if(pauseFlag==false) {
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            tvState.setText(getResources().getString(R.string.stete_end));           //播放完毕
                            playFlag = false;
                            mp.release();
                        }
                    });
                    seekBar.setMax(mediaPlayer.getDuration());
                    playFlag = true;
                    UpdateSeekBar updateProgress = new UpdateSeekBar();
                    updateProgress.execute(1000);
                    seekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener());
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        tvState.setText(getResources().getString(R.string.start));
                    } catch (Exception e) {
                        this.tvState.setText(getResources().getString(R.string.stete_error));
                    }
                }else {
                    pauseFlag = false;
                    playFlag = true;
                    mediaPlayer.start();
                    UpdateSeekBar updateProgress = new UpdateSeekBar();
                    updateProgress.execute(1000);
                    seekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener());
                    tvState.setText(getResources().getString(R.string.start));
                }
                break;
            case R.id.btnEnd:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    playFlag = false;
                    tvState.setText(getResources().getString(R.string.end));
                }
                break;
            case R.id.btnPause:
                if(mediaPlayer.isPlaying())
                {
                    playFlag = false;
                    pauseFlag=true;
                    mediaPlayer.pause();
                    tvState.setText(getResources().getString(R.string.puase));
                }
                break;
            case R.id.btnRestart:
                if(mediaPlayer.isPlaying()){
                    playFlag = true;
                    mediaPlayer.seekTo(0);
                }
                break;
        }
    }

    private class UpdateSeekBar extends AsyncTask<Integer,Integer,String> {
        @Override
        protected void onPostExecute(String s) {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            seekBar.setProgress(values[0]);
        }
        @Override
        protected String doInBackground(Integer... params) {
            while(playFlag){
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.publishProgress(mediaPlayer.getCurrentPosition());
            }
            return null;
        }
    }

    private class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }
}
