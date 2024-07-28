package com.group4.kidomtoystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.Utils.OnboardingUtils;

public class SplashScreen2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loadData();
    }

    private void loadData() {
        if (OnboardingUtils.isNetworkAvailable(this)) {
//            Có mạng
//        load data
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen2Activity.this, OnboardingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }
}