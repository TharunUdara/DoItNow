package com.example.doitnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class InfoActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        drawerLayout = findViewById(R.id.drawerLayout);
        View btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navSideBar = findViewById(R.id.navSideBar);
        navSideBar.setNavigationItemSelectedListener(item -> {
            handleNavigation(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_info);
        nav.setOnItemSelectedListener(item -> {
            handleNavigation(item.getItemId());
            return true;
        });
    }

    private void handleNavigation(int id) {
        if (id == R.id.nav_tasks) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, AccountActivity.class));
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_info) {
            // Already here
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_info);
    }
}