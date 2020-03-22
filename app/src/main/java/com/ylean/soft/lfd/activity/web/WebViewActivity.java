package com.ylean.soft.lfd.activity.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2020/3/6.
 */
public class WebViewActivity extends BaseWebView {

    @BindView(R.id.webview)
    WebView webview;
    private String url;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        url = getIntent().getStringExtra("url");
        initWebView(webview);
        webview.loadUrl(url);
        findViewById(R.id.img_bank).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.clearCache(true);
    }
}
