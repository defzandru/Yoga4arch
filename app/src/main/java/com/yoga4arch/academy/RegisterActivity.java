package com.yoga4arch.academy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword, edtFullname;
    private Button btnRegister, btnGoLogin;
    private ProgressBar progress;
    private TextView tvMessage;

    private static final String REGISTER_URL_PLUGIN = "https://yoga4archacademy.cloud/wp-json/yoga4arch/v1/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }        
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullname = findViewById(R.id.edtFullname);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoLogin = findViewById(R.id.btnGoLogin);
        progress = findViewById(R.id.progress);
        tvMessage = findViewById(R.id.tvMessage);

        btnRegister.setOnClickListener(v -> registerUser());
        btnGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            tvMessage.setText("Semua field wajib diisi!");
            tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }

        progress.setVisibility(View.VISIBLE);
        tvMessage.setText("");

        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("email", email);
            json.put("password", password);
            if (!fullname.isEmpty()) {
                json.put("name", fullname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    tvMessage.setText("Registrasi gagal. Cek koneksi!");
                    tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> progress.setVisibility(View.GONE));
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        tvMessage.setText("Registrasi berhasil. Silakan login.");
                        tvMessage.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        tvMessage.setText("Registrasi gagal. Coba lagi.");
                        tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    });
                }
            }
        });
    }
}
