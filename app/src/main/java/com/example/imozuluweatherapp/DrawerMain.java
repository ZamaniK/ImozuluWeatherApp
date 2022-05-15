package com.example.imozuluweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class DrawerMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;

            case R.id.menu_update_profile:
                startActivity(new Intent(getApplicationContext(), Upload_profilePicActivity.class));
                overridePendingTransition(0, 0);
                return true;

            case R.id.weather_bottom:
                startActivity(new Intent(getApplicationContext(), MainWeatherActivity.class));
                overridePendingTransition(0, 0);
                return true;

            case R.id.News_bottom:
                startActivity(new Intent(getApplicationContext(), NewsMainActivity.class));
                overridePendingTransition(0, 0);
                return true;

        }
        return false;
    }
}