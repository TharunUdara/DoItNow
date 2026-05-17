package com.example.doitnow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

/**
 * Account screen where users can view and edit their profile details.
 * Supports profile photo uploads and session management (Sign Out).
 */
public class AccountActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView ivAvatar;
    private TextView tvName, tvUsername, tvEmail;
    private SharedPreferences prefs;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Setup Drawer Layout for side navigation
        drawerLayout = findViewById(R.id.drawerLayout);
        View btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Side Bar navigation listener
        NavigationView navSideBar = findViewById(R.id.navSideBar);
        navSideBar.setNavigationItemSelectedListener(item -> {
            handleNavigation(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        ivAvatar = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);

        loadUserData();

        // Allow user to pick a profile photo from gallery
        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Show edit profile dialog
        findViewById(R.id.btnEditInfo).setOnClickListener(v -> showEditDialog());

        // Handle logout
        findViewById(R.id.btnSignOut).setOnClickListener(v -> {
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Bottom Navigation setup
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_profile);
        nav.setOnItemSelectedListener(item -> {
            handleNavigation(item.getItemId());
            return true;
        });
    }

    /**
     * Centralized navigation handler for both bottom and side bars.
     */
    private void handleNavigation(int id) {
        if (id == R.id.nav_tasks) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_info) {
            startActivity(new Intent(this, InfoActivity.class));
            overridePendingTransition(0, 0);
        }
    }

    /**
     * Loads user profile data from SharedPreferences.
     */
    private void loadUserData() {
        tvName.setText(prefs.getString("username", "Alex Thompson"));
        tvUsername.setText("@" + prefs.getString("username", "alex_doit"));
        tvEmail.setText(prefs.getString("email", "alex.thompson@curator.io"));
        String imageUri = prefs.getString("profileImage", null);
        if (imageUri != null) {
            ivAvatar.setImageURI(Uri.parse(imageUri));
        }
    }

    /**
     * Displays a dialog to edit name and email.
     */
    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        EditText etName = v.findViewById(R.id.etName);
        EditText etEmail = v.findViewById(R.id.etEmail);
        etName.setText(prefs.getString("username", ""));
        etEmail.setText(prefs.getString("email", ""));

        v.findViewById(R.id.btnSave).setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            if (!name.isEmpty()) {
                prefs.edit().putString("username", name).putString("email", email).apply();
                loadUserData();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                ivAvatar.setImageURI(imageUri);
                // Persist profile image URI
                prefs.edit().putString("profileImage", imageUri.toString()).apply();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_profile);
    }
}