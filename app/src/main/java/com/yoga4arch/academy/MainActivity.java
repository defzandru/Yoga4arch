package com.yoga4arch.academy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String USER_URL = "https://yoga4archacademy.com/wp-json/wp/v2/users/me";

    TextView tvInfo;
    Button btnLogout;
    RequestQueue queue;
    MyAppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tvInfo);
        btnLogout = findViewById(R.id.btnLogout);
        queue = Volley.newRequestQueue(this);
        prefs = new MyAppPrefs(this);

        loadProfile();

        btnLogout.setOnClickListener(v -> {
            prefs.clear();
            finish();
        });
    }

    private void loadProfile() {
        StringRequest req = new StringRequest(Request.Method.GET, USER_URL,
                response -> {
                    tvInfo.setText(response);
                },
                error -> {
                    Log.e(TAG, "profile error", error);
                    Toast.makeText(MainActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = prefs.getToken();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        queue.add(req);
    }
}
