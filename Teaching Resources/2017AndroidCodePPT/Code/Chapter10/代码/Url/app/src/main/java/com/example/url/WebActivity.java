package com.example.url;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.url.R;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class WebActivity extends AppCompatActivity {

    ImageView imgView;
    Bitmap bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        imgView = (ImageView)findViewById(R.id.imgView);
        ShowPicture("http://pic.to8to.com/attch/day_160218/20160218_d968438a2434b62ba59dH7q5KEzTS6OH.png");
    }

    public void ShowPicture(String url){
        new AsyncTask<String, Void , String>() {
            @Override
            protected String doInBackground(String... params) {
               try{
                   URL url = new URL(params[0]);  //定义url为第一个参数
                   InputStream is = url.openStream();   //获取输入流
                   bm = BitmapFactory.decodeStream(is);   //解析图片
                   is.close();             //关闭输入流
               }catch (MalformedURLException e){
                   e.printStackTrace();
               }catch (Exception e){
                   e.printStackTrace();
               }
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                imgView.setImageBitmap(bm);
            }
        }.execute(url);
    }
}
