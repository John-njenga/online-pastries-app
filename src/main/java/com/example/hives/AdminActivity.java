package com.example.hives;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    private EditText etAdminEmail, etAdminPassword;
    private Button btnAdminLogin;

    private final String ADMIN_EMAIL = "adminhives@gmail.com";
    private final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etAdminEmail.getText().toString().trim();
                String password = etAdminPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etAdminEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    etAdminPassword.setError("Password is required");
                    return;
                }

                if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                    Intent intent = new Intent(AdminActivity.this, AdminDashboard.class);
                    Toast.makeText(AdminActivity.this, "Welcome Back Admin", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminActivity.this, "Invalid email or password\n You are not an ADMIN!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
