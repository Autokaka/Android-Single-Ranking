package com.example.videoview;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    VideoView myVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaController myMc = new MediaController(MainActivity.this);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test.mp4");
        myVideo = (VideoView)this.findViewById(R.id.videoView);
        myVideo.setVideoURI(uri);
        myVideo.setMediaController(myMc);
        myVideo.requestFocus();
        try{
            myVideo.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
