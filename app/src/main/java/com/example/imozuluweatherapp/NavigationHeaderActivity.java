/*
package com.example.imozuluweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


public class NavigationHeaderActivity extends AppCompatActivity {
    private TextView textViewUserName;
    private ImageView imageProfile;
    private FirebaseAuth authProfile;
    private String fullName, email, doB, gender, mobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation_header);


        imageProfile = findViewById(R.id.imageMenu);
        textViewUserName = findViewById(R.id.menu_Username);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser == null) {
            Toast.makeText(NavigationHeaderActivity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            showUserProfile(firebaseUser);
        }
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

                    textViewUserName.setText(fullName + "!");

                    //Set user DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();
                    //ImageViewer set ImageURI() should not be used without regular URIs. So we are using Picasso
                    Picasso.with(NavigationHeaderActivity.this).load(uri).into(imageProfile);

                } else {
                    Toast.makeText(NavigationHeaderActivity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}*/
