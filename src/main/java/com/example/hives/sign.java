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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        // Initialize UI components
        nameEditText = findViewById(R.id.edtnamesign);
        emailEditText = findViewById(R.id.edemailsign);
        phoneEditText = findViewById(R.id.edtphonesign);  // New phone EditText
        passwordEditText = findViewById(R.id.edtpasssign);
        confirmPasswordEditText = findViewById(R.id.edtconpass);
        signupButton = findViewById(R.id.btnsign);
        loginTextView = findViewById(R.id.logtxt);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();  // Initialize database reference

        // Handle signup button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();  // Get phone number
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(sign.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals(confirmPassword)) {
                    signup(name, email, phone, password);
                } else {
                    Toast.makeText(sign.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle login text view click
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Login activity
                startActivity(new Intent(sign.this, login.class));
            }
        });
    }

    private void signup(final String name, final String email, final String phone, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Save user information to Realtime Database
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference userRef = db.child("users").child(userId);

                            userRef.child("name").setValue(name);
                            userRef.child("email").setValue(email);
                            userRef.child("phone").setValue(phone)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Sign up success, navigate to user dashboard
                                                startActivity(new Intent(sign.this, login.class));
                                                Toast.makeText(sign.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                // If saving user info fails, display a message to the user.
                                                Toast.makeText(sign.this, "Failed to save user info.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(sign.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
