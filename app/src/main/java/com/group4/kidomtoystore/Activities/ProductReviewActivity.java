package com.group4.kidomtoystore.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.ProductRateAdapter;
import com.group4.kidomtoystore.Adapters.ProductReviewAdapter;
import com.group4.kidomtoystore.Models.ProductRate;
import com.group4.kidomtoystore.Models.ProductReview;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityProductReviewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductReviewActivity extends BaseActivity {

    ActivityProductReviewBinding binding;
    ArrayList<ProductReview> productReviews;
    ProductReviewAdapter productReviewAdapter;
    String imgURL;
    String productID, fullname;
    ImageView imageReview;
    private LinearLayout selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (binding.tbReview != null) {
            binding.tbReview.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        getintent();
        initProductReview();
//        loadProductReview();
        loadLayout();
        addEvents();
    }

    private void getintent() {
        Intent intent = getIntent();
        productID = intent.getStringExtra("productId");
    }


    private void addEvents() {
        binding.btn5.setOnClickListener(v -> handleButtonClick(binding.btn5, 5));
        binding.btn4.setOnClickListener(v -> handleButtonClick(binding.btn4, 4));
        binding.btn3.setOnClickListener(v -> handleButtonClick(binding.btn3, 3));
        binding.btn2.setOnClickListener(v -> handleButtonClick(binding.btn2, 2));
        binding.btn1.setOnClickListener(v -> handleButtonClick(binding.btn1, 1));
    }
    private void handleButtonClick(LinearLayout button, int rating) {
        // Kiểm tra nếu có button được chọn trước đó, thì đặt lại background là border_red
        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.border_red);
        }

        // Kiểm tra nếu button hiện tại là button đã được chọn, thì không thực hiện gì cả
        if (selectedButton == button) {
            selectedButton = null;
            loadProductReview(); // Hiển thị lại toàn bộ review
            return;
        }

        // Thiết lập button mới được chọn
        selectedButton = button;
        selectedButton.setBackgroundResource(R.drawable.button_border_chosen_double);

        // Lọc và hiển thị review với rating tương ứng
        if (productReviews.size() > 0) {
            filterReviewsByRating(rating);
        }
    }
    private void filterReviewsByRating(int rating) {
        ArrayList<ProductReview> filteredReviews = new ArrayList<>();
        for (ProductReview review : productReviews) {
            if (review.getRating() == rating) {
                filteredReviews.add(review);
            }
        }
        productReviewAdapter.setProductReviews(filteredReviews);
        productReviewAdapter.notifyDataSetChanged();
    }

    private void initProductReview() {
        DatabaseReference myRef = database.getReference("Review").child("Review");
        productReviews = new ArrayList<>();
        Query query = myRef.orderByChild("productId").equalTo(productID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int rateNumber = dataSnapshot.child("rating").getValue(Integer.class);
                    String reviewContent = dataSnapshot.child("content").getValue(String.class);
                    String userId = dataSnapshot.child("userId").getValue(String.class);
                    String imgURL = dataSnapshot.child("imageURL").getValue(String.class);

                    DatabaseReference userRef = database.getReference("Users");
                    userRef.child(userId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Kiểm tra xem dữ liệu có tồn tại không
                            if (dataSnapshot.exists()) {
                                // Lấy giá trị của fullname và sử dụng nó ở đây
                                fullname = dataSnapshot.getValue(String.class);
                                Log.d("Username", "User" + fullname);

                                // Tạo đối tượng ProductReview ở đây, sau khi đã lấy được fullname
                                ProductReview productReview = new ProductReview(rateNumber, reviewContent, imgURL, productID, fullname);
                                productReviews.add(productReview);

                                // Cập nhật RecyclerView sau khi đã thêm đối tượng ProductReview vào danh sách
                                loadProductReview();
                            } else {
                                // Xử lý khi dữ liệu không tồn tại
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý khi có lỗi xảy ra
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void loadProductReview() {
        productReviewAdapter = new ProductReviewAdapter(getApplicationContext(), productReviews);
        if (!productReviews.isEmpty()) {
            // Gắn Adapter cho RecyclerView
            binding.rcvProductReview.setAdapter(productReviewAdapter);
        } else {
            // Xử lý trường hợp danh sách dữ liệu rỗng
        }
    }

    private void loadLayout() {

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvProductReview.setLayoutManager(linearLayoutManager1);
    }
}