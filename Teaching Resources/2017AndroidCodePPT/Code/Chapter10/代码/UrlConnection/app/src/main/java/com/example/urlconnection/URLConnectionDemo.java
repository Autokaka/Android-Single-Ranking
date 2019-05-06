package com.example.urlconnection;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionDemo extends AppCompatActivity {

    EditText et_username, et_password;
    Button btn_submit;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlconnection_demo);
        et_username = (EditText) findViewById(R.id.ed_username);
        et_password = (EditText) findViewById(R.id.ed_password);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck(et_username.getText().toString()
                        , et_password.getText().toString());
            }
        });
    }

    //Use URLConnection
    public void loginCheck(String username, String password) {
        final String url = "http://172.0.0.1:8888/MyServer/Servlet/LoginServlet"; //访问的url
        final String param = "username=" + username + "&" + "password=" + password; //设置参数
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                PrintWriter out = null;
                BufferedReader in = null;
                String result = null;
                try {
                    URL url = new URL(params[0]);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("accept", "/*");  //设置URLConnection参数
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                    conn.setDoInput(true);     //允许输入
                    conn.setDoOutput(true);    //允许输出
                    out = new PrintWriter(conn.getOutputStream());
                    out.print(param);   //设置参数
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line; //获取返回结果
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    tv_result.setText("验证结果：" + result); //显示返回结果
                }
                super.onPostExecute(result);
            }
        }.execute(url);
    }

    //Use HttpURLConnection
    public void loginCheck2(String username, String password) {
        final String url = "http://172.0.0.1:8888/MyServer/Servlet/LoginServlet"; //访问的url
        final String param = "username=" + username + "&" + "password=" + password; //设置参数
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                PrintWriter out = null;
                BufferedReader in = null;
                String result = null;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setDoInput(true);     //允许输入
                    httpConn.setDoOutput(true);    //允许输出
                    httpConn.setRequestMethod("post"); //设置为POST请求方式
                    httpConn.setUseCaches(false);   //使用Post方式不能使用缓存
                    out = new PrintWriter(httpConn.getOutputStream());
                    out.print(param);   //设置参数
                    int responseCode = httpConn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            result += line; //获取返回结果
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    tv_result.setText("验证结果：" + result); //显示返回结果
                }
                super.onPostExecute(result);
            }
        }.execute(url);
    }
}
