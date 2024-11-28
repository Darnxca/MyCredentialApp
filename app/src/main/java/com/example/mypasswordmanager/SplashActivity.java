package com.example.mypasswordmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.mypasswordmanager.ui.home.HomeFragment;
import com.example.mypasswordmanager.ui.home.HomeViewModel;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        splashScreen.setKeepOnScreenCondition(() -> true );
        Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(myIntent);
        finish();
    }
}