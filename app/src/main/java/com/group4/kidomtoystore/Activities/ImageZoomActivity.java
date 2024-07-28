package com.group4.kidomtoystore.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityImageZoomBinding;

public class ImageZoomActivity extends AppCompatActivity {

    ActivityImageZoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageZoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView imageView = findViewById(R.id.imvImageZoom);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("imageURL")) {
            String imageUrl = intent.getStringExtra("imageURL");
            Glide.with(this).load(imageUrl).into(imageView);
        }

        addEvents();
    }

    private void addEvents() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}