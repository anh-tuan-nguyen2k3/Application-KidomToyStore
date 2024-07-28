package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.AddressAdapter;
import com.group4.kidomtoystore.Models.Address;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityAdressBinding;

import java.util.ArrayList;

public class AdressActivity extends AppCompatActivity implements AddressAdapter.OnItemClickListener{
    ActivityAdressBinding binding;
    AddressAdapter adapter;
    ArrayList<Address> addresses;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DataSnapshot dataSnapshot;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        boolean isFromProfile = getIntent().getBooleanExtra("isFromProfile", false);

        binding.tbAddress.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdressActivity.this,AccountSettingActivity.class);
                startActivity(intent);
            }
        });

        initData();
        addEvents(isFromProfile);

    }
    private void initData() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            userId = user.getUid();
            addresses = new ArrayList<>();

            reference.child(userId).child("Addresses").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                            Address address = addressSnapshot.getValue(Address.class);
                            addresses.add(address);
                        }
                        displayAddresses();
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

    private void displayAddresses() {
        if (!addresses.isEmpty()) {
            binding.rcvAddress.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AddressAdapter(AdressActivity.this, addresses, reference);
            binding.rcvAddress.setAdapter(adapter);
        } else {
            // Xử lý khi không có dữ liệu địa chỉ
        }

    }

    private void addEvents(boolean isFromProfile) {
        binding.btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdressActivity.this, AddressNewActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        if (!getIntent().getBooleanExtra("isFromProfile", false)) {
            Intent intent = new Intent(AdressActivity.this, OrderDetailActivity.class);
            intent.putExtra("selectedAddress", addresses.get(position));
            setResult(RESULT_OK, intent);
            startActivity(intent);
            finish();
        } else {
            adapter.removeItem(position);
        }
    }
}