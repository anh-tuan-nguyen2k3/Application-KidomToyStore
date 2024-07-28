package com.group4.kidomtoystore.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.DetailOrderManagementAdapter;
import com.group4.kidomtoystore.Adapters.PreparingOrderAdapter;
import com.group4.kidomtoystore.Models.Order;
import com.group4.kidomtoystore.Models.OrderManagement;
import com.group4.kidomtoystore.databinding.ActivityPreparingOrderDetailBinding;

import java.util.ArrayList;

public class PreparingOrderDetailActivity extends AppCompatActivity {

    ActivityPreparingOrderDetailBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Toolbar toolbar;
    FirebaseUser user;
    ArrayList<OrderManagement> orderManagements;
    DetailOrderManagementAdapter detailOrderManagementAdapter;
    Order order;
    double discountAmount;
    double deliveryAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreparingOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addEvents();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Khởi tạo ListView và danh sách sản phẩm
        RecyclerView lvProducts = binding.lvProducts;
        lvProducts.setLayoutManager(new LinearLayoutManager(this));
        orderManagements = new ArrayList<>();

        // Retrieve order ID from Intent
        Intent intent = getIntent();

        if (intent != null) {
            String orderId = intent.getStringExtra("orderId");
            Log.d("PreparingOrderDetail", "Order ID: " + orderId);

            // Ensure that orderId is not null before proceeding
            if (orderId != null) {
                // Query Firebase database to fetch all products of the order
                databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.getUid())
                        .child("Orders")
                        .child(orderId);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String address = dataSnapshot.child("address").getValue(String.class);
                            String time = dataSnapshot.child("time").getValue(String.class);
                            double deliveryFee = dataSnapshot.child("deliveryFee").getValue(Double.class);
                            double voucher = dataSnapshot.child("discountAmount").getValue(Double.class);
                            double amount = dataSnapshot.child("amount").getValue(Double.class);

                            binding.txtAddressLocation.setText(address);
                            binding.txtShippingFee.setText(String.format("%.0fđ", deliveryFee));
                            binding.txtVoucher.setText(String.format("-%.0fđ", voucher));
                            binding.txtTotalOrder.setText(String.format("%.0fđ", amount));
                            binding.txtOrderTime.setText(time);
                            binding.txtOrderId.setText(orderId);

                            DataSnapshot itemsSnapshot = dataSnapshot.child("items");
                            for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                                String productId = itemSnapshot.getKey();
                                String productName = itemSnapshot.child("productName").getValue(String.class);
                                double productPrice = itemSnapshot.child("productPrice").getValue(Double.class);
                                String productThumb = itemSnapshot.child("productThumb").getValue(String.class);
                                int productQuantity = itemSnapshot.child("productQuantity").getValue(Integer.class);

                                // Create OrderManagement object for each product and add it to the list
                                OrderManagement orderManagement = new OrderManagement(orderId, productName, productQuantity, productPrice, null, productThumb);
                                orderManagement.setId(productId);
                                orderManagements.add(orderManagement);
                            }


                        // Initialize adapter and set it to the ListView
                        detailOrderManagementAdapter = new DetailOrderManagementAdapter(PreparingOrderDetailActivity.this, orderManagements, "preparing");

                        lvProducts.setAdapter(detailOrderManagementAdapter);
                        detailOrderManagementAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("PreparingOrderDetail", "Order with ID " + orderId + " does not exist");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                        Log.e("PreparingOrderDetail", "Error fetching order details: " + databaseError.getMessage());
                    }
                });
            } else {
                Log.e("PreparingOrderDetail", "Order ID is null");
            }
        } else {
            Log.e("PreparingOrderDetail", "Intent is null");
        }

        binding.tbOrderDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void addEvents() {
        binding.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId = getIntent().getStringExtra("orderId");

                // Kiểm tra nếu orderId không null
                if (orderId != null) {
                    // Thực hiện xóa dữ liệu từ Firebase Realtime Database
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Users")
                            .child(user.getUid())
                            .child("Orders")
                            .child(orderId);

                    orderRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Xóa đơn hàng khỏi danh sách
                                    orderManagements.removeIf(orderManagement -> orderManagement.getId().equals(orderId));
                                    // Cập nhật giao diện
                                    detailOrderManagementAdapter.notifyDataSetChanged();
                                    Toast.makeText(PreparingOrderDetailActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("PreparingOrderDetail", "Error canceling order: " + e.getMessage());
                                    Toast.makeText(PreparingOrderDetailActivity.this, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Log.e("PreparingOrderDetail", "Order ID is null");
                    Toast.makeText(PreparingOrderDetailActivity.this, "Không thể xác định đơn hàng", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}