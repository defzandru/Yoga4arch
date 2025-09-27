package com.yoga4arch.academy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

        // Supaya WebView tetap di aplikasi (tidak buka browser eksternal)
        webView.setWebViewClient(new WebViewClient());

        // Load URL utama (kalau mau bisa diarahkan ke halaman tertentu)
        if (token != null) {
            webView.loadUrl(BASE_URL);
        } else {
            // fallback kalau token null (misalnya langsung balik ke login)
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
