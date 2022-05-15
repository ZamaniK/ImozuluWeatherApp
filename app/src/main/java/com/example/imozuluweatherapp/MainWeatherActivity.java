package com.example.imozuluweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;

public class MainWeatherActivity extends AppCompatActivity {

    String CITY1;
    TextView addressT, updated_atT, statusT, tempT, temp_minTxt, temp_maxT, sunriseT,sunsetT, windT, pressureT, humidityT;
    EditText CITY;

    final String APP_ID = "d13eb5c0ddfb97f2590ae5098c86be63";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    private FirebaseAuth authProfile;



    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;


    LocationManager mLocationManager;
    LocationListener mLocationListner;

    private AdView mAdView;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weather);

        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weatherIcon);
        mCityFinder = findViewById(R.id.cityFinder);
        NameofCity = findViewById(R.id.cityName);


//        findViewById(R.id.presd).setVisibility(View.GONE);
//        findViewById(R.id.sund).setVisibility(View.VISIBLE);
//        findViewById(R.id.sunsd).setVisibility(View.VISIBLE);
//        findViewById(R.id.wnd).setVisibility(View.VISIBLE);
//        findViewById(R.id.humd).setVisibility(View.GONE);
//
//
//        CITY=findViewById(R.id.city);
//        addressT = findViewById(R.id.address);
//        updated_atT = findViewById(R.id.updated_at);
//        statusT = findViewById(R.id.status);
//        tempT = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxT = findViewById(R.id.temp_max);
        sunriseT = findViewById(R.id.sunrise);
        sunsetT = findViewById(R.id.sunset);
        windT = findViewById(R.id.wind);
//        pressureT = findViewById(R.id.pressure);
//        humidityT = findViewById(R.id.humidity);

        //ad Mod Initialization

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Custom Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_actionbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Custom Image for action bar start
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_imagelogo, null);
        actionBar.setCustomView(view);


        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWeatherActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        // Bottom Navigation
          BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
          bottomNavigationView.setSelectedItemId(R.id.weather_bottom);
          bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  switch (item.getItemId()) {
                      case R.id.profile_bottom:
                          startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                          overridePendingTransition(0, 0);
                          return true;

                      case R.id.weather_bottom:
                          return true;

                      case R.id.News_bottom:
                          startActivity(new Intent(getApplicationContext(), NewsMainActivity.class));
                          overridePendingTransition(0, 0);
                          return true;

                  }
                  return false;
              }
          });

    }

   /*@Override
   protected void onStart() {
       super.onResume();
       getWeatherForCurrentLocation();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }


    }


    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        letsdoSomeNetworking(params);

    }




    private void getWeatherForCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params =new RequestParams();
                params.put("lat" ,Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                letsdoSomeNetworking(params);




            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.profile_bottom:
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.weather_bottom:
                    return true;

                case R.id.News_bottom:
                    startActivity(new Intent(getApplicationContext(), NewsMainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

            }
            return false;
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainWeatherActivity.this,"Location get Succesful!",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied the permission
            }
        }


    }



    private  void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainWeatherActivity.this,"Location Data Fetch Successful!",Toast.LENGTH_SHORT).show();

                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);


                // super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });



    }

    private  void updateUI(weatherData weather){


        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);

//        addressT.setText(weather.getAddress());
//        updated_atT.setText(weather.getUpdatedAtText());
//        statusT.setText(weather.getWeatherDescription().toUpperCase());
//        tempT.setText(weather.getTemp());
//        temp_minTxt.setText(weather.getTempMin());
//        temp_maxT.setText(weather.getTempMax());
        sunriseT.setText (new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.getSunrise() * 1000)));
        sunsetT.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.getSunset() * 1000)));
        windT.setText(weather.getWindSpeed()+" km/h");
//        pressureT.setText(weather.getPressure());
//        humidityT.setText(weather.getHumidity());






    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null)
        {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }
/*
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
            Intent intent = new Intent(MainWeatherActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_profile) {
            Intent intent = new Intent(MainWeatherActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_news) {
            Intent intent = new Intent(MainWeatherActivity.this, NewsMainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_weather) {
            Intent intent = new Intent(MainWeatherActivity.this, MainWeatherActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_update_email){
            Intent intent = new Intent(MainWeatherActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        }*//*else if (id == R.id.menu_settings){
            Toast.makeText(Upload_profilePicActivity.this, "menu_settings", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.menu_change_password){
            Intent intent = new Intent(Upload_profilePicActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(Upload_profilePicActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
        }*//*
        else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(MainWeatherActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainWeatherActivity.this, MainActivity.class);
            //To prevent user from going back to the Register Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(MainWeatherActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
