package com.example.imozuluweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private EditText editTextPwdCurr, editTextPwdNew, editTextPwdConfirmNew;
    private TextView textViewAuthenticate;
    private ProgressBar progressBar;
    private String userPwdCurr;
    private Button buttonChangePwd, buttonReAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password");

        editTextPwdNew = findViewById(R.id.editText_change_password_new);
        editTextPwdCurr = findViewById(R.id.ediText_change_password_current);
        editTextPwdConfirmNew = findViewById(R.id.editText_change_password_new_confirm);
        textViewAuthenticate = findViewById(R.id.textView_change_pwd_authenticated);
        progressBar = findViewById(R.id.progressBar);
        buttonReAuthenticate = findViewById(R.id.button_authenticate);
        buttonChangePwd = findViewById(R.id.button_change_pwd);

        //Disable ediText for new Pwd, Confirm New Password and Make Change Pwd Button unclickable till user is authenticated
        editTextPwdNew.setEnabled(false);
        editTextPwdConfirmNew.setEnabled(false);
        buttonChangePwd.setEnabled(false);
        
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        
        if(firebaseUser.equals("")){
            Toast.makeText(ChangePassword.this, "Something went wrong! User's details not available", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePassword.this,UserProfileActivity.class);
            startActivity(intent);
            finish();
        } else{
            reAuthenticateUser(firebaseUser);
        }

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwdCurr = editTextPwdCurr.getText().toString();
                if(TextUtils.isEmpty(userPwdCurr)){
                    Toast.makeText(ChangePassword.this,"Password is required", Toast.LENGTH_SHORT).show();
                    editTextPwdCurr.setError("Please enter your current password to authenticate");
                    editTextPwdCurr.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    //ReAuthenticate user now
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwdCurr);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                //Disable editText for Current Password. Enable Editt Text for New Password and Confirm New Password
                                editTextPwdCurr.setEnabled(false);
                                editTextPwdNew.setEnabled(true);
                                editTextPwdConfirmNew.setEnabled(true);

                                //Enable change pwd button, disable reAuthenticate
                                buttonReAuthenticate.setEnabled(false);
                                buttonChangePwd.setEnabled(true);


                                //Set TextView to show User is authenticated/verified
                                textViewAuthenticate.setText("You are authenticated/verified" +
                                        "Change password now");
                                Toast.makeText(ChangePassword.this, "Password has been verified" + "Change password now", Toast.LENGTH_SHORT).show();

                                //Update color of Change Password Button
                                buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePassword.this, R.color.dark_green));

                                buttonChangePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }
                            else {
                                try {
                                    throw task.getException();
                                } catch (Exception e){
                                    Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userPwdNew = editTextPwdNew.getText().toString();
        String userPwdConfirmNew = editTextPwdConfirmNew.getText().toString();

        if(TextUtils.isEmpty(userPwdNew)){
            Toast.makeText(ChangePassword.this,"New Password is required", Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter your new password");
            editTextPwdNew.requestFocus();
        } else if (TextUtils.isEmpty(userPwdConfirmNew)){
            Toast.makeText(ChangePassword.this,"Confirm New Password is required", Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please re-enter your new password");
            editTextPwdNew.requestFocus();
        }
        else if (!userPwdNew.matches(userPwdConfirmNew)){
            Toast.makeText(ChangePassword.this,"Password did not match", Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please re-enter your new password");
            editTextPwdNew.requestFocus();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChangePassword.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassword.this,UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(ChangePassword.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        }
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    }
}