package com.yoga4arch.academy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnGoRegister;
    private ProgressBar progress;
    private TextView tvMessage;

    private static final String LOGIN_URL = "https://yoga4archacademy.cloud/wp-json/jwt-auth/v1/token";
    private static final String PREF_NAME = "MyAppPrefs";

    private OkHttpClient client;
    private MediaType JSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);
        progress = findViewById(R.id.progress);
        tvMessage = findViewById(R.id.tvMessage);

        client = new OkHttpClient();
        JSON = MediaType.get("application/json; charset=utf-8");

        btnLogin.setOnClickListener(v -> loginUser());
        btnGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            tvMessage.setText("Username dan password wajib diisi!");
            tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }

        progress.setVisibility(View.VISIBLE);
        tvMessage.setText("");

        // body JSON untuk API
        String json = "{"
                + "\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\""
                + "}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    tvMessage.setText("Gagal terhubung ke server.");
                    tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> progress.setVisibility(View.GONE));

                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    try {
                        JsonObject jsonObject = JsonParser.parseString(resp).getAsJsonObject();

                        if (jsonObject.has("token")) {
                            String token = jsonObject.get("token").getAsString();

                            // simpan token ke SharedPreferences
                            getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                                    .edit()
                                    .putString("jwt_token", token)
                                    .apply();

                            runOnUiThread(() -> {
                                tvMessage.setText("Login berhasil.");
                                tvMessage.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> {
                                tvMessage.setText("Login gagal: " + resp);
                                tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            });
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            tvMessage.setText("Error parsing response.");
                            tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        tvMessage.setText("Login gagal. Periksa username/password.");
                        tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    });
                }
            }
        });
    }
}
