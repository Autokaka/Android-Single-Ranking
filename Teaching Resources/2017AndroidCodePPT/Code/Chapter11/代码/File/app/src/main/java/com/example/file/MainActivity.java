package com.example.file;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.cqupt.test7_2.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class MainActivity extends Activity {

    private Button btnSave,btnRead,btnSaveSD,btnReadSD;
    private EditText edFilename,edFilecontent;
    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSave = (Button)this.findViewById(R.id.btnSave);
        btnRead = (Button)this.findViewById(R.id.btnRead);
        btnSaveSD = (Button)this.findViewById(R.id.btnSaveSD);
        btnReadSD = (Button)this.findViewById(R.id.btnReadSD);
        edFilename = (EditText)this.findViewById(R.id.edFilename);
        edFilecontent = (EditText)this.findViewById(R.id.edFilecontent);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String filename = edFilename.getText().toString();
                String filecontent = edFilecontent.getText().toString();
                FileOutputStream out = null;
                try {
                    out = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    out.write(filecontent.getBytes("UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally{
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }});
        btnSaveSD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String filename = edFilename.getText().toString();
                String filecontent = edFilecontent.getText().toString();

                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File file = new File(Environment.getExternalStorageDirectory().toString()
                                + File.separator+"cqupt"+File.separator + filename ); //定义存储路径
                    if(!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();  //创建文件夹
                    }
                    PrintStream out = null;
                    try{
                        out = new PrintStream(new FileOutputStream(file,true));
                        out.println(filecontent);   //写入内容
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        if (out!= null){
                            out.close();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "SD卡不存在，存储失败", Toast.LENGTH_SHORT).show();
                }
            }});

        btnRead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String filename = edFilename.getText().toString(); //获得读取的文件的名称
                FileInputStream in = null;
                ByteArrayOutputStream bout = null;
                byte[]buf = new byte[1024];
                bout = new ByteArrayOutputStream();
                int length = 0;
                try {
                    in = context.openFileInput(filename); //获得输入流
                    while((length=in.read(buf))!=-1){
                        bout.write(buf,0,length);
                    }
                    byte[] content = bout.toByteArray();
                    edFilecontent.setText(new String(content,"UTF-8")); //设置文本框为读取的内容
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edFilecontent.invalidate(); //刷新屏幕
                try{
                    in.close();
                    bout.close();
                }
                catch(Exception e){}
            }});

        btnReadSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = edFilename.getText().toString(); //获得读取的文件的名称
                String filecontent = "";
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File file = new File(Environment.getExternalStorageDirectory().toString()
                        + File.separator + "cqupt" + File.separator + filename);
                    if (!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }
                    Scanner scan = null;
                    try{
                        scan = new Scanner(new FileInputStream(file));
                        while(scan.hasNext()){
                            filecontent += scan.next()+"\n";
                        }
                        edFilecontent.setText(filecontent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if (scan!=null){
                            scan.close();
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, "SD卡不存在，存储失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
