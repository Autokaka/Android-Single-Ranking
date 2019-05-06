package com.example.javascript;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewCall extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_call);
        webView = (WebView) findViewById(R.id.webview);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);  //启动JavaScript
        ws.setDefaultTextEncodingName("UTF-8");  //定义字符集为"UTF-8"
        webView.loadUrl("file:///android_asset/call.html");  //加载html页面
        webView.addJavascriptInterface(this, "demo"); //html页面中的JavaScript
    }
    @JavascriptInterface       //一定要添加这个标签，才能将该方法暴露给javaScript
    public void call(final String phone) {
        if (ActivityCompat.checkSelfPermission(this  //检测权限
                , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;}
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
