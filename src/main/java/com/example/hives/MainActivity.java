package com.example.hives;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Use a Handler to introduce a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to move to the next page
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                // Optionally finish the current activity if you don't want it to be in the back stack
                finish();
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}