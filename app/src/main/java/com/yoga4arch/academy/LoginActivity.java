package com.yoga4arch.academy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://yoga4archacademy.cloud";
    private static final String LOGIN_URL = BASE_URL + "/wp-json/jwt-auth/v1/token";
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3lvZ2E0YXJjaGFjYWRlbXkuY2xvdWQiLCJpYXQiOjE3NTg5NjM5NTIsIm5iZiI6MTc1ODk2Mzk1MiwiZXhwIjoxNzU5NTY4NzUyLCJkYXRhIjp7InVzZXIiOnsiaWQiOiIxIn19fQ.sq1VrleZw8E9cYsvuuos2_NF7p3u1yEk__Vi2vXt1a8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Isi username & password!", Toast.LENGTH_SHORT).show();
            return;
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{ \"username\":\"" + username + "\", \"password\":\"" + password + "\" }";
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Login gagal: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JsonObject jsonObject = JsonParser.parseString(resp).getAsJsonObject();
                            String token = jsonObject.get("token").getAsString();

                            // simpan token
                            SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                            prefs.edit().putString(KEY_TOKEN, token).apply();

                            Toast.makeText(LoginActivity.this, "Login sukses!", Toast.LENGTH_SHORT).show();

                            // pindah ke MainActivity
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Login gagal (parse error)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login gagal: " + resp, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
