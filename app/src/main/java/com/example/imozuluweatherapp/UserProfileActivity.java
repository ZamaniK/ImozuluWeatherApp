package com.example.imozuluweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textviewWelcome, testViewFullName, textViewMenuName, textViewEmail, textViewDoB, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private String fullName, email, doB, gender, mobile;
    private RoundedImageView imageView;
    private FirebaseAuth authProfile;
    private TextView textViewUserName;
    private RoundedImageView imageProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textviewWelcome = findViewById(R.id.textView_show_Welcome);
        testViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_email);
        textViewDoB = findViewById(R.id.textView_dob);
        textViewGender = findViewById(R.id.textView_gender);
        textViewMobile = findViewById(R.id.textView_mobile);

        progressBar = findViewById(R.id.progressBar);




        //Set OnClickListener on ImageView to Open UploadProfilePicActivity
        imageView = (RoundedImageView)findViewById(R.id.ImageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, Upload_profilePicActivity.class);
                startActivity(intent);
            }
        });

        //Drawer
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_profile:
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_update_profile:
                        startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_update_email:
                        startActivity(new Intent(getApplicationContext(), UpdateEmailActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_weather:
                        startActivity(new Intent(getApplicationContext(), MainWeatherActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_change_password:
                        startActivity(new Intent(getApplicationContext(), ChangePassword.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_delete_profile:
                        startActivity(new Intent(getApplicationContext(), DeleteProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_users:
                        Intent i = new Intent(getApplicationContext(),UsersList.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_logout:
                        authProfile.signOut();
                        Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                        //To prevent user from going back to the Register Activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    case R.id.menu_news:
                        startActivity(new Intent(getApplicationContext(), NewsMainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });





        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.profile_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_bottom:
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
        });


        imageProfile = (RoundedImageView) navigationView.getHeaderView(0).findViewById(R.id.menu_image_profile);
        textViewUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.menu_Username);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser == null) {
            Toast.makeText(UserProfileActivity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }


    private  void checkIfEmailVerified(FirebaseUser firebaseUser){
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;

            case R.id.menu_update_profile:
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;

            case R.id.menu_update_email:
                startActivity(new Intent(getApplicationContext(), UpdateEmailActivity.class));
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
    private void showAlertDialog() {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You cannot login without email verification");

        //Open Email App if user taps Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //extracting User Reference from database "Registered users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    fullName = readUserDetails.fullName;
                    email = firebaseUser.getEmail();
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;

                    textviewWelcome.setText("Welcome " + fullName + "!");
                    testViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewGender.setText(gender);
                    textViewDoB.setText(doB);
                    textViewMobile.setText(mobile);
                    textViewUserName.setText(fullName);


                    //Set user DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();
                    //ImageViewer set ImageURI() should not be used withou regular URIs. So we are using Picasso
                    Picasso.with(UserProfileActivity.this).load(uri).into(imageView);
                    //String link = snapshot.child("menu_image_profile").getValue().toString();
                    Picasso.with(getBaseContext()).load(uri).into(imageProfile);

                } else {
                    Toast.makeText(UserProfileActivity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Creating ActionBar Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //When any menu is selected

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            startActivity(getIntent());
            finish();
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent(UserProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_profile) {
            Intent intent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_news) {
            Intent intent = new Intent(UserProfileActivity.this, NewsMainActivity.class);
            startActivity(intent);
            finish();
        }

        else if (id == R.id.menu_weather) {
            Intent intent = new Intent(UserProfileActivity.this, MainWeatherActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_update_email){
            Intent intent = new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        }/* else if (id == R.id.menu_settings){
            Toast.makeText(Upload_profilePicActivity.this, "menu_settings", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.menu_change_password){
            Intent intent = new Intent(Upload_profilePicActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(Upload_profilePicActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
        }*/
        else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            //To prevent user from going back to the Register Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
