package com.example.imozuluweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imozuluweatherapp.Models.NewsHeadlines;
import com.example.imozuluweatherapp.Models.SelectListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class NewsDetailsActivity extends AppCompatActivity {
    NewsHeadlines headlines;
    TextView text_title, text_author, text_time, text_detail, text_content;
    ImageView img_news;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        text_title = findViewById(R.id.text_detail_title);
        text_author = findViewById(R.id.text_detail_author);
        text_time = findViewById(R.id.text_detail_time);
        text_detail = findViewById(R.id.text_detail_detail);
        text_content = findViewById(R.id.text_detail_content);
        img_news = findViewById(R.id.img_detail_news);


        headlines = (NewsHeadlines) getIntent().getSerializableExtra("data");

        text_title.setText(headlines.getTitle());
        text_author.setText(headlines.getAuthor());
        text_time.setText(headlines.getPublishedAt().toLowerCase(Locale.ROOT));
        text_author.setText(headlines.getAuthor());
        text_detail.setText(headlines.getDescription());
        text_content.setText(headlines.getContent());

        Picasso.with(this).load(headlines.getUrlToImage()).into(img_news);
        //ad Mod Initialization

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}