package com.example.balishchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // make sure this exists

        // Delay for 2-3 seconds then start login or main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, login.class); // or MainActivity.class
                startActivity(intent);
                finish(); // closes splash activity so user can't go back to it
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}
