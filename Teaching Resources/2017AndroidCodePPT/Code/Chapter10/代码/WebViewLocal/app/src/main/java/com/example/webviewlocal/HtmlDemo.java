package com.example.webviewlocal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class HtmlDemo extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_demo);
        webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/index.html");
    }
}
