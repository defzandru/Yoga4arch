package com.yoga4arch.academy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends Activity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnGoRegister;
    private ProgressBar progress;
    private TextView tvMessage;

    private static final String LOGIN_URL = "https://yoga4archacademy.cloud/wp-json/jwt-auth/v1/token";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);
        progress = new ProgressBar(this);
        tvMessage = new TextView(this);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void attemptLogin() {
        tvMessage.setText("");
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (!validate(username, password)) return;

        progress.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            showError("JSON error: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .header("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> showError("Network error: " + e.getMessage()));
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                final String respBody = response.body() != null ? response.body().string() : "";
                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    if (response.isSuccessful()) {
                        tvMessage.setText("Login berhasil ✅");
                        // TODO: redirect ke MainActivity setelah login sukses
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String msg = parseMessage(respBody);
                        tvMessage.setText("Gagal: " + msg + " (HTTP " + response.code() + ")");
                    }
                });
            }
        });
    }

    private boolean validate(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Username wajib diisi");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password wajib diisi");
            return false;
        }
        return true;
    }

    private void showError(String msg) {
        runOnUiThread(() -> {
            progress.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            tvMessage.setText(msg);
        });
    }

    private String parseMessage(String body) {
        if (body == null) return "No response body";
        try {
            JSONObject j = new JSONObject(body);
            if (j.has("message")) return j.optString("message");
            if (j.has("error")) return j.optString("error");
            return body;
        } catch (JSONException e) {
            return body.length() > 200 ? body.substring(0, 200) : body;
        }
    }
}
