package com.group4.kidomtoystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.Utils.OnboardingUtils;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mAuth = FirebaseAuth.getInstance();

        loadData();
    }

    private void loadData() {
        if (OnboardingUtils.isNetworkAvailable(this)) {
            // Có mạng, load data
            new Handler().postDelayed(() -> {
                checkUserLogin();
            }, 4000);
        } else {
            // Không có mạng
            Toast.makeText(this, "Network is disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu người dùng đã đăng nhập, chuyển hướng đến HomePageFull
            startActivity(new Intent(SplashScreenActivity.this, HomePageFull.class));
            finish();
        } else {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            startActivity(new Intent(SplashScreenActivity.this, SplashScreen2Activity.class));
            finish();
        }
    }
}
