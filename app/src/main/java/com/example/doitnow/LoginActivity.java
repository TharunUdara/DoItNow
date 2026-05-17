package com.example.doitnow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Handles user authentication.
 * Checks for an existing session or validates entered credentials.
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Auto-login if session exists
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        // Simple mock login logic using SharedPreferences
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            String savedUser = prefs.getString("username", "");
            String savedPass = prefs.getString("password", "");

            if (username.equals(savedUser) && password.equals(savedPass)) {
                prefs.edit().putBoolean("isLoggedIn", true).apply();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // Redirect to SignUpActivity
        findViewById(R.id.tvSignUp).setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }
}