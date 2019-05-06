package com.example.socketclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientActivity extends AppCompatActivity {

    TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        tvInfo = (TextView)findViewById(R.id.receivedInfo);
        new Thread() {
            public void run() {
                try{
                    Socket socket = new Socket("192.168.252.1", 4666); //创建Socket
                    socket.setSoTimeout(3000);//超时设置为3秒
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String lien = br.readLine(); //读取接收到的数据
                    tvInfo.setText("Client received：" + lien);
                    br.close();
                    socket.close();
                }
                catch (SocketTimeoutException e){
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
