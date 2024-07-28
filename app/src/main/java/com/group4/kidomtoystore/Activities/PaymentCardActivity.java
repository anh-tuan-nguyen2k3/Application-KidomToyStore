package com.group4.kidomtoystore.Activities;


import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityPaymentCardBinding;

public class PaymentCardActivity extends AppCompatActivity {
    ActivityPaymentCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        addEvents();
    }

    private void addEvents() {
        binding.txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentCardActivity.this, TransactionHistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}