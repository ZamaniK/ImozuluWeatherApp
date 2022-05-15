package com.example.imozuluweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UsersList extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_users, UsersFragment.class, null)
                    .commit();
        }
    }
}