package com.example.doitnow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash screen shown when the app launches.
 * Displays the branding for 2 seconds before redirecting to the login screen.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for 2 seconds (2000ms) then start LoginActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Close SplashActivity so it's not in the back stack
        }, 2000);
    }
}