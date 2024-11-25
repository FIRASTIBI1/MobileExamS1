package com.example.myapplicationjdid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the logo ImageView
        ImageView logo = findViewById(R.id.logo);

        // Load the bounce animation
        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_bounce);
        logo.startAnimation(bounceAnimation); // Start the animation

        // Delay for SPLASH_DISPLAY_LENGTH and then start MainActivity
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish(); // Close SplashActivity
        }, SPLASH_DISPLAY_LENGTH);
    }
}
