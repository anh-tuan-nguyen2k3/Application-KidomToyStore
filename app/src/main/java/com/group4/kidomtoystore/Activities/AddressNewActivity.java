package com.group4.kidomtoystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.kidomtoystore.Adapters.AddressAdapter;
import com.group4.kidomtoystore.Models.Address;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityAddressNewBinding;

import java.util.ArrayList;

public class AddressNewActivity extends AppCompatActivity {

    ActivityAddressNewBinding binding;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    Intent intent;
    Address address;
    AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding.tbNewAddress.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addEvents();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        adapter = new AddressAdapter(this, new ArrayList<Address>(), reference.child(mAuth.getCurrentUser().getUid()).child("Addresses"));    }

    private void addEvents() {
        binding.edtFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtAddress.getText().toString()) && !TextUtils.isEmpty(binding.edtAddressType.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(AddressNewActivity.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtAddress.getText().toString()) && !TextUtils.isEmpty(binding.edtAddressType.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(AddressNewActivity.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtAddress.getText().toString()) && !TextUtils.isEmpty(binding.edtAddressType.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(AddressNewActivity.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });
        binding.edtAddressType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtAddress.getText().toString()) && !TextUtils.isEmpty(binding.edtAddressType.getText().toString())) {
                    binding.btnSave.setBackground(ContextCompat.getDrawable(AddressNewActivity.this, R.drawable.button_available));
                }else{
                    binding.btnSave.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = binding.edtFullName.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String addressDetail = binding.edtAddress.getText().toString();
                String addressType = binding.edtAddressType.getText().toString();

                // Validate các trường thông tin trước khi thêm địa chỉ mới
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(AddressNewActivity.this, "Nhập tên của bạn", Toast.LENGTH_SHORT).show();
                    binding.edtFullName.setError("Không được bỏ trống");
                    binding.edtFullName.requestFocus();
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(AddressNewActivity.this, "Nhập số điện thoại của bạn", Toast.LENGTH_SHORT).show();
                    binding.edtPhone.setError("Không được bỏ trống");
                    binding.edtPhone.requestFocus();
                } else if (TextUtils.isEmpty(addressDetail)) {
                    Toast.makeText(AddressNewActivity.this, "Nhập địa chỉ của bạn", Toast.LENGTH_SHORT).show();
                    binding.edtAddress.setError("Không được bỏ trống");
                    binding.edtAddress.requestFocus();
                } else if (TextUtils.isEmpty(addressType)) {
                    Toast.makeText(AddressNewActivity.this, "Nhập loại địa chỉ của bạn", Toast.LENGTH_SHORT).show();
                    binding.edtAddressType.setError("Không được bỏ trống");
                    binding.edtAddressType.requestFocus();
                }else if (phone.length() != 10 || !phone.startsWith("0")) {
                    Toast.makeText(AddressNewActivity.this, "Số điện thoại sai định dạng", Toast.LENGTH_SHORT).show();
                    binding.edtPhone.setError("Số điện thoại sai định dạng");
                    binding.edtPhone.requestFocus();
                } else {
                    address = new Address(fullName, phone, addressDetail, addressType);
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String userID = user.getUid();
                        reference.child(userID).child("Addresses").push().setValue(address)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AddressNewActivity.this, "Thêm địa chỉ nhận hàng mới thành công", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddressNewActivity.this, AdressActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddressNewActivity.this, "Thêm địa chỉ nhận hàng mới thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }
}