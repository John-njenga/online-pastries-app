package com.example.hives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail, tvUserPhone;
    private EditText etUserName, etUserEmail, etUserPhone;
    private Button btnEdit, btnSave, btnLogout;
    private LinearLayout userInfoLayout, editUserInfoLayout;
    private androidx.cardview.widget.CardView userInfoCard, editUserInfoCard;
    private TextView tvAdmin;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);

        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPhone = findViewById(R.id.etUserPhone);

        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);

        userInfoLayout = findViewById(R.id.userInfoLayout);
        editUserInfoLayout = findViewById(R.id.editUserInfoLayout);

        userInfoCard = findViewById(R.id.userInfoCard);
        editUserInfoCard = findViewById(R.id.editUserInfoCard);

        tvAdmin = findViewById(R.id.tvAdmin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();  // Initialize database reference
        user = mAuth.getCurrentUser();

        if (user != null) {
            loadUserInfo();
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoCard.setVisibility(View.GONE);
                editUserInfoCard.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, login.class));
                finish();
            }
        });

        tvAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AdminActivity.class));
            }
        });
    }

    private void loadUserInfo() {
        if (user != null) {
            DatabaseReference userRef = db.child("users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);

                        // Display the user email from FirebaseUser
                        String email = user.getEmail();

                        // Update UI with fetched data
                        tvUserName.setText(name);
                        tvUserPhone.setText(phone);
                        tvUserEmail.setText(email);

                        etUserName.setText(name);
                        etUserPhone.setText(phone);
                        etUserEmail.setText(email); // Set email in EditText as well

                        // Save user info to SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userName", name);
                        editor.putString("userEmail", email);
                        editor.putString("userPhone", phone);
                        editor.apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Failed to load user info.", Toast.LENGTH_SHORT).show();
                }
            });
        }
}

    private void saveUserInfo() {
        final String name = etUserName.getText().toString();
        final String phone = etUserPhone.getText().toString();

        if (user != null) {
            DatabaseReference userRef = db.child("users").child(user.getUid());
            userRef.child("name").setValue(name);
            userRef.child("phone").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "User info updated.", Toast.LENGTH_SHORT).show();
                        loadUserInfo();
                        editUserInfoCard.setVisibility(View.GONE);
                        userInfoCard.setVisibility(View.VISIBLE);
                        btnEdit.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to update user info.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
