package com.example.imozuluweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class MainActivity extends AppCompatActivity {
    private Target logoTarget;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_main);
        //
        //
        // getActionBar().setTitle("Imozulu");

        //Custom Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_actionbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Custom Logo animation
        showActionBarLogo(this, true);
        //Custom Image for action bar start
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_imagelogo, null);
        actionBar.setCustomView(view);

        //actionbar.setTitle("your title goes here.");
        //Set Title
        //getSupportActionBar().setTitle("Imozulu Weather App");
        //Open login Activity
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        });
        //Open Register Activity
        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });
    }

    private void showActionBarLogo(MainActivity mainActivity, boolean show) {
        if(show){
            //Calculate Action Bar Height
            int actionBarHeight = 200;
            TypedValue tv = new TypedValue();
            if(mainActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,mainActivity.getResources().getDisplayMetrics());
            }

            //Using action bar background drawable
            logoTarget = new Target(){
                @Override
                public Class<? extends Annotation> annotationType() {
                    return null;
                }

                @Override
                public ElementType[] value() {
                    return new ElementType[0];
                }

                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from){
                    Drawable[] layers = new Drawable[2];
                    layers[0] = new ColorDrawable(Color.RED);
                    BitmapDrawable bd = new BitmapDrawable(mainActivity.getResources(), bitmap);
                    bd.setGravity(Gravity.CENTER);
                    Drawable drawlogo = bd;
                    LayerDrawable layerDrawable = new LayerDrawable(layers);

                    layers[1].setAlpha(0);
                    ((AppCompatActivity) mainActivity).getSupportActionBar().setBackgroundDrawable(layerDrawable);

                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(layers[1], PropertyValuesHolder.ofInt("alpha", 255));
                    animator.setTarget(layers[1]);
                    animator.setDuration(2000);
                    animator.start();
                }
            };
            Picasso.with(mainActivity).load(R.drawable.app_icon);
        } else {
            ((AppCompatActivity) mainActivity).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));

        }

    }

}