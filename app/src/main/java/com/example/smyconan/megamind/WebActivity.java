package com.example.smyconan.megamind;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
    private WebView m_webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        setTitle("电影名字");
        m_webView = (WebView) findViewById(R.id.webView);
        m_webView.setWebViewClient(new WebViewClient() {
        });
        m_webView.loadUrl("http://m.video.baidu.com/#detail/movie/119633");
    }
}
