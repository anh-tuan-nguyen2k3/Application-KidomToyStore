package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.AddressAdapter;
import com.group4.kidomtoystore.Adapters.PaymentCardAdapter;
import com.group4.kidomtoystore.Models.Address;
import com.group4.kidomtoystore.Models.PaymentCard;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityAdressBinding;
import com.group4.kidomtoystore.databinding.ActivityCardManagementBinding;

import java.util.ArrayList;

public class CardManagement extends AppCompatActivity {
    ActivityCardManagementBinding binding;
    PaymentCardAdapter adapter;
    ArrayList<PaymentCard> cards;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DataSnapshot dataSnapshot;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

//        ToolBar
        Toolbar toolbar = findViewById(R.id.tbCardManagement);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CardManagement.this, AccountSettingActivity.class);
                    startActivity(intent);
                }
            });
        }

        initData();
        addEvents();
    }

    private void initData() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            cards = new ArrayList<>();

            reference.child(userId).child("PaymentCards").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot paymentCardSnapshot : snapshot.getChildren()) {
                            PaymentCard paymentCard = paymentCardSnapshot.getValue(PaymentCard.class);
                            cards.add(paymentCard);
                        }
                        displayCard();
                    } else {
                        // Xử lý khi không có dữ liệu địa chỉ
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi xảy ra
                }
            });
        }
    }

    private void displayCard() {
        if (!cards.isEmpty()) {
            binding.rcvPaymentCard.setLayoutManager(new LinearLayoutManager(this));
            adapter = new PaymentCardAdapter(cards, CardManagement.this);
            binding.rcvPaymentCard.setAdapter(adapter);
        } else {
            // Xử lý khi không có dữ liệu địa chỉ
        }
    }

    private void addEvents() {
        binding.btnNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardManagement.this, CardNew.class);
                startActivity(intent);
            }
        });
    }
}