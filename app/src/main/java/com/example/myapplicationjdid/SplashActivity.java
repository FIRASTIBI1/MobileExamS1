package com.example.myapplicationjdid;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the dots in the layout
        final ImageView dot1 = findViewById(R.id.dot1);
        final ImageView dot2 = findViewById(R.id.dot2);
        final ImageView dot3 = findViewById(R.id.dot3);

        // Set up the dot animation
        ObjectAnimator blinkDot1 = ObjectAnimator.ofFloat(dot1, "alpha", 1f, 0f);
        blinkDot1.setDuration(500);
        blinkDot1.setRepeatCount(ObjectAnimator.INFINITE);
        blinkDot1.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator blinkDot2 = ObjectAnimator.ofFloat(dot2, "alpha", 1f, 0f);
        blinkDot2.setDuration(500);
        blinkDot2.setRepeatCount(ObjectAnimator.INFINITE);
        blinkDot2.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator blinkDot3 = ObjectAnimator.ofFloat(dot3, "alpha", 1f, 0f);
        blinkDot3.setDuration(500);
        blinkDot3.setRepeatCount(ObjectAnimator.INFINITE);
        blinkDot3.setRepeatMode(ObjectAnimator.REVERSE);

        // Start the animations
        blinkDot1.start();
        blinkDot2.start();
        blinkDot3.start();

        // Delay to start MainActivity after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after the delay
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // Close SplashActivity
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
