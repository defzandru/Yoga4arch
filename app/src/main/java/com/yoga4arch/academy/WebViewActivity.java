package com.yoga4arch.academy;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import androidx.annotation.Nullable;

public class WebViewActivity extends Activity {
    private static final String URL = "https://yoga4archacademy.cloud";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView wv = new WebView(this);
        setContentView(wv);

        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(URL);
    }
}
