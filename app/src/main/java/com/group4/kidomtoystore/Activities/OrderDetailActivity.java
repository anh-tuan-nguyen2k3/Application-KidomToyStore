package com.group4.kidomtoystore.Activities;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.START;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.kidomtoystore.Adapters.OrderAdapter;
import com.group4.kidomtoystore.Models.Address;
import com.group4.kidomtoystore.Models.CartItem;
import com.group4.kidomtoystore.Models.Order;
import com.group4.kidomtoystore.Models.OrderMethod;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityOrderDetailBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    ActivityOrderDetailBinding binding;
    ArrayList<CartItem> cartItems;
    OrderAdapter orderAdapter;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseUser user;
    OrderMethod selectedOrderMethod;
    private ActivityResultLauncher<Intent> startActivityIntent;
    double totalAmount;
    String userId;
    Order order;
    String delivery;
    String discount;
    double discountAmount;
    double deliveryAmount;
    boolean deliverySelected = false;
    String payment;
    Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (binding.toolbar != null) {
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        initAddress();
        initOrderList();
        setupStartActivityForResult();
        addEvents();
    }

    private void initAddress() {

    }

    private void initOrderList() {
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = mAuth.getCurrentUser();

        // Get cart items from intent
        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        binding.rcvOrderList.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
        orderAdapter = new OrderAdapter(cartItems);
        binding.rcvOrderList.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();

        totalAmount = 0;

        for (CartItem item : cartItems) {
            totalAmount += item.getProductPrice() * item.getProductQuantity();
        }
        binding.txtAmount.setText(String.format("%.0fđ", totalAmount));
        binding.txtTotal.setText(String.format("%.0fđ", totalAmount));
    }

    private void setupStartActivityForResult() {
        startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedOrderMethod = data.getParcelableExtra("selectedMethod");
                        String tag = data.getStringExtra("tag");
                        if (selectedOrderMethod != null) {
                            if (tag.equals("discount")){
                                discount = selectedOrderMethod.getTitle();
                                binding.txtDiscount.setText(discount);
                                discountAmount = selectedOrderMethod.getPrice();
                                binding.txtDiscountAmount.setText(String.format("-%.0fđ", discountAmount));
                                if (!selectedOrderMethod.getTitle().equals("Nhập mã giảm giá")) {
                                    binding.txtDiscount.setBackgroundResource(R.drawable.button_available);
                                    binding.txtDiscount.setTextColor(getResources().getColor(R.color.white));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        binding.txtDiscount.setTypeface(getResources().getFont(R.font.baloopaaji2semibold));
                                    }
                                    binding.txtDiscount.setGravity(CENTER);
                                    totalAmount -= selectedOrderMethod.getPrice();
                                } else {
                                    binding.txtDiscount.setBackgroundResource(R.drawable.input_field);
                                }
                            }

                            if (tag.equals("delivery")){
                                deliverySelected = true;
                                delivery = selectedOrderMethod.getTitle();
                                binding.txtDelivery.setText(delivery);
                                deliveryAmount = selectedOrderMethod.getPrice();
                                totalAmount += selectedOrderMethod.getPrice();
                            }

                            if (tag.equals("payment")){
                                payment = selectedOrderMethod.getTitle();
                                binding.txtBank.setText(payment);
                            }
                        }
                        if (deliverySelected){
                            binding.txtShipAmount.setText(String.format("%.0fđ", deliveryAmount));
                        }else{
                            deliveryAmount = 15000;
                            delivery = "Thông thường";
                            binding.txtShipAmount.setText(String.format("%.0fđ", deliveryAmount));
                        }
                        binding.txtTotal.setText(String.format("%.0fđ", totalAmount));
                    }
                }
            }
        });
    }

    private void addEvents() {

        binding.layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, AdressActivity.class);
                intent.putExtra("fromOrder", true);
                startActivity(intent);
            }
        });

        binding.layoutDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, DeliveryMethodActivity.class);
                startActivityIntent.launch(intent);
            }
        });
        binding.btnAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, DiscountListActivity.class);
                startActivityIntent.launch(intent);
            }
        });
        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuccessDialog();
                reference = FirebaseDatabase.getInstance().getReference("Users");
                user = mAuth.getCurrentUser();
                userId = user.getUid();
                Date currentTimestamp = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                String timestampString = dateFormat.format(currentTimestamp);
                order = new Order(cartItems, "Nhà", payment, delivery, deliveryAmount, discount, discountAmount, totalAmount, "Chờ xác nhận", timestampString);
                reference.child(userId).child("Orders").push().setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(userId).child("CartItems").removeValue();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(OrderDetailActivity.this, OrderManagementActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }
        });

        binding.layoutPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, PaymentMethodActivity.class);
                startActivityIntent.launch(intent);
            }
        });

    }
    private void showSuccessDialog() {
        Dialog dialog = new Dialog(OrderDetailActivity.this);

        dialog.setContentView(R.layout.dialog_successful);

        ImageView icon = dialog.findViewById(R.id.imvDgIcon);
        icon.setImageResource(R.drawable.ic_buy_success);

        TextView title = dialog.findViewById(R.id.dgTitle);
        title.setText("Đặt hàng thành công!");

        TextView text = dialog.findViewById(R.id.txtDialogText);
        text.setText("Ba mẹ đã đặt hàng thành công. Hệ thống sẽ chuyển ba mẹ đến trang quản lý đơn hàng!");

        ProgressBar progressBar = dialog.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}