package com.yoga4arch.academy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String LOGIN_URL = "https://yoga4archacademy.com/wp-json/jwt-auth/v1/token";

    EditText etUsername, etPassword;
    Button btnLogin;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if already logged in -> go to main
        MyAppPrefs prefs = new MyAppPrefs(this);
        if (prefs.getToken() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        queue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username & password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject body = new JSONObject();
            body.put("username", username);
            body.put("password", password);

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, body,
                    response -> {
                        try {
                            String token = response.optString("token", null);
                            long exp = response.optLong("exp", 0);

                            if (token != null) {
                                // if server doesn't return exp, default to 1 day
                                if (exp == 0) {
                                    exp = (System.currentTimeMillis() / 1000) + 86400;
                                }
                                MyAppPrefs prefs = new MyAppPrefs(LoginActivity.this);
                                prefs.saveToken(token, exp);
                                Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login gagal: token kosong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "parse error", e);
                            Toast.makeText(LoginActivity.this, "Login gagal: parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e(TAG, "request error", error);
                        Toast.makeText(LoginActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    });

            queue.add(req);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error building request", Toast.LENGTH_SHORT).show();
        }
    }
}
