package com.group4.kidomtoystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Adapters.ProductReviewAdapter;
import com.group4.kidomtoystore.Models.CartItem;
import com.group4.kidomtoystore.Models.Product;
import com.group4.kidomtoystore.Models.ProductReview;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityProductDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailActivity extends BaseActivity {

    ActivityProductDetailBinding binding;
    ArrayList<ProductReview> productReviews;
    ProductReviewAdapter productReviewAdapter;
    TextView txtSeeAll;
    String productID;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    CartItem cartItem;
    Product product;
    int quantity = 1;
    FirebaseUser user;
    double productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txtSeeAll = findViewById(R.id.txtSeeAll);
        txtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductReviewActivity.class);
                intent.putExtra("productId", productID);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getIntentExtra();
        initProduct();
        initProductReview();
        loadLayout();
        addEvents();
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
                    String imgURL = dataSnapshot.child("imageURL").getValue(String.class);
                    String userId = dataSnapshot.child("userId").getValue(String.class);

                    DatabaseReference userRef = database.getReference("Users");
                    userRef.child(userId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Kiểm tra xem dữ liệu có tồn tại không
                            if (dataSnapshot.exists()) {
                                // Lấy giá trị của fullname và sử dụng nó ở đây
                                String fullname = dataSnapshot.getValue(String.class);
                                Log.d("Username", "User" + fullname);

                                // Tạo đối tượng ProductReview ở đây, sau khi đã lấy được fullname
                                ProductReview productReview = new ProductReview(rateNumber, reviewContent, imgURL, productID, fullname);
                                productReviews.add(productReview);

                                // Cập nhật RecyclerView sau khi đã thêm đối tượng ProductReview vào danh sách
//                                loadProductReview();
                                productReviewAdapter = new ProductReviewAdapter(getApplicationContext(), productReviews);
                                binding.rcvProductReviewinDetail.setAdapter(productReviewAdapter);
                            } else {
                                // Xử lý khi dữ liệu không tồn tại
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý khi có lỗi xảy ra
                        }
                    });

//                    ProductReview productReview = new ProductReview(rateNumber, reviewContent, imgURL, productID, user);
//                    productReviews.add(productReview);
//                    Log.d("Firebase", "Review " +reviewContent);
                }
                // Tạo adapter và đặt nó cho RecyclerView
//                productReviewAdapter = new ProductReviewAdapter(getApplicationContext(), productReviews);
//                binding.rcvProductReviewinDetail.setAdapter(productReviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }


    private void loadLayout() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvProductReviewinDetail.setLayoutManager(linearLayoutManager1);
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            productID = intent.getStringExtra("productId");
            if (productID != null) {
                Log.d("ProductDetailActivity", "Received productID: " + productID);
            } else {
                Log.d("ProductDetailActivity", "productID is null");
            }
        } else {
            Log.d("ProductDetailActivity", "Intent is null");
        }
        addEvents();
    }

    private void initProduct() {
        DatabaseReference myRef = database.getReference("Product");
        final ArrayList<Product> productList = new ArrayList<>();
        Query query = myRef.orderByChild("id").equalTo(productID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                        // Lấy imgURL từ product và gắn vào ImageView
                        String imgUrl = product.getImgURL();
                        if (imgUrl != null && !imgUrl.isEmpty()) {
                            Picasso.get().load(imgUrl).into(binding.imvProductThumb);
                        }
                        String productName = product.getName();
                        if (productName != null && !productName.isEmpty()) {
                            binding.txtProductName.setText(productName);
                        }
                        String productDescription = product.getDescription();
                        if (productDescription != null && !productDescription.isEmpty()) {
                            binding.txtProductDescription.setText(productDescription);
                        }
                        double productStar = product.getStar();
                        if (productStar != 0) {
                            binding.txtRate.setText(String.valueOf(productStar));
                            binding.txtRate1.setText(String.valueOf(productStar));
                        }
                        productPrice = product.getPrice() - (product.getPrice() * ((double) product.getSale() / 100));
                        if (productPrice != 0) {
                            binding.txtProductPrice.setText(String.format("%.0fđ", productPrice));
                            binding.txtTotal.setText(String.format("%.0fđ", productPrice));
                        }
                        int productStock = product.getStock();
                        if (productStock != 0) {
                            binding.txtLuotban.setText(String.valueOf(productStock));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn
            }
        });
    }


    private void addEvents() {
        binding.imvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1){
                    quantity--;
                    binding.txtProductQuantity.setText(String.valueOf(quantity));
                }
            }
        });
        binding.imvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                binding.txtProductQuantity.setText(String.valueOf(quantity));
            }
        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Users");
                user = mAuth.getCurrentUser();
                String userId = user.getUid();
                String key = reference.child(userId).child("CartItems").push().getKey();
                cartItem = new CartItem(key, product.getId(),product.getName(), productPrice, product.getImgURL(), quantity);
                reference.child(userId).child("CartItems").child(key).setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}