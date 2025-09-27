package com.yoga4arch.academy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3lvZ2E0YXJjaGFjYWRlbXkuY2xvdWQiLCJpYXQiOjE3NTg5NjM5NTIsIm5iZiI6MTc1ODk2Mzk1MiwiZXhwIjoxNzU5NTY4NzUyLCJkYXRhIjp7InVzZXIiOnsiaWQiOiIxIn19fQ.sq1VrleZw8E9cYsvuuos2_NF7p3u1yEk__Vi2vXt1a8";
    private static final String BASE_URL = "https://yoga4archacademy.cloud/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // WebViewClient dengan handler link eksternal
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();

                // http & https tetap di WebView
                if ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())) {
                    return false;
                }

                // selain itu lempar ke aplikasi lain
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Tidak bisa membuka link: " + uri.toString(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // Load URL utama
        if (token != null) {
            webView.loadUrl(BASE_URL);
        } else {
            webView.loadUrl(BASE_URL);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
