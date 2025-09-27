package com.yoga4arch.academy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnGoRegister;
    private ProgressBar progress;
    private TextView tvMessage;

    private static final String LOGIN_URL = "https://yoga4archacademy.cloud/wp-json/jwt-auth/v1/token";

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

        // TODO: Integrasi API login
        edtUsername.postDelayed(() -> {
            progress.setVisibility(View.GONE);
            if (username.equals("admin") && password.equals("123456")) {
                tvMessage.setText("Login berhasil.");
                tvMessage.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                tvMessage.setText("Username atau password salah.");
                tvMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }, 1500);
    }
}
