package com.example.hives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signupTextView; // Add this line
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.edtlogemail);
        passwordEditText = findViewById(R.id.edtlogpass);
        loginButton = findViewById(R.id.btnlog);
        signupTextView = findViewById(R.id.signup); // Initialize this line
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                login(email, password);
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() { // Add this block
            @Override
            public void onClick(View v) {
                // Navigate to Signup activity
                startActivity(new Intent(login.this, sign.class));
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success, navigate to user dashboard
                            startActivity(new Intent(login.this, UserDashboard.class));
                            Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
