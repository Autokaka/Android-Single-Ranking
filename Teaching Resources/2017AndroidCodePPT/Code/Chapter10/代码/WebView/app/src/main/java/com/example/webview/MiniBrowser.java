package com.example.webview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MiniBrowser extends AppCompatActivity implements View.OnClickListener{
    Button btnBack,btnForward,btnRefresh,btnGo;
    EditText etURL;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_browser);
        btnBack = (Button)findViewById(R.id.btnBac);
        btnForward = (Button)findViewById(R.id.btnFow);
        btnRefresh = (Button)findViewById(R.id.btnRef);
        btnGo = (Button)findViewById(R.id.btnGo);
        etURL = (EditText)findViewById(R.id.etUrl);
        webView = (WebView)findViewById(R.id.webView);
        btnBack.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBac:
                    webView.goBack();
                break;
            case R.id.btnFow:
                webView.goForward();
                break;
            case R.id.btnRef:
                webView.reload();
                break;
            case R.id.btnGo:
                if(etURL.getText().toString()!=null){
                    webView.loadUrl("http://"+etURL.getText().toString());
                    //设置Web视图
                    webView.setWebViewClient(new webViewClient());
                }
                break;
            default: break;
        }
    }
    @Override
    //设置回退
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }
    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
