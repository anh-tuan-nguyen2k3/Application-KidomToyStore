package com.group4.kidomtoystore.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group4.kidomtoystore.Adapters.PreparingOrderAdapter;
import com.group4.kidomtoystore.Fragments.ReviewOrderFragment;
import com.group4.kidomtoystore.Models.OrderManagement;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityReviewBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReviewActivity extends AppCompatActivity {

    ActivityReviewBinding binding;
    Toolbar toolbar;
    ActivityResultLauncher<Intent> activityResultLauncher;
    boolean openCam;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private String productId;
    private String fullName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Khởi tạo activity result launcher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Xử lý kết quả trả về từ camera hoặc thư viện ở đây
                            Intent data = result.getData();
                            if (data != null && openCam) {
                                // Xử lý kết quả từ camera
                                Bundle extras = data.getExtras();
                                Bitmap imageBitmap = (Bitmap) extras.get("data");
                                // Đặt ảnh mới vào ImageView
                                binding.imvReviewImg.setImageBitmap(imageBitmap);
                            } else if (data != null && !openCam) {
                                // Xử lý kết quả từ thư viện
                                Uri selectedImageUri = data.getData();
                                // Chuyển đổi Uri thành Bitmap (thư viện Picasso)
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                    // Đặt ảnh mới vào ImageView
                                    binding.imvReviewImg.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });


        addEvents();
        //databaseReference = FirebaseDatabase.getInstance().getReference("Review");

    }

    private void addEvents() {

        binding.btnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Intent intent = getIntent();
        if (intent != null) {
            String orderId = intent.getStringExtra("orderId");
            int itemPosition = intent.getIntExtra("itemPosition", -1);
            if (orderId != null && itemPosition != -1) {
                // Thực hiện truy xuất thông tin sản phẩm từ Firebase
                databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.getUid())
                        .child("Orders")
                        .child(orderId)
                        .child("items")
                        .child(String.valueOf(itemPosition));

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            // Lấy thông tin sản phẩm từ dataSnapshot
                            Log.d("ItemSnapshot", "Key: " + dataSnapshot.getKey());
                            String productName = dataSnapshot.child("productName").getValue(String.class);
                            String productThumb = dataSnapshot.child("productThumb").getValue(String.class);
                            double productPrice = dataSnapshot.child("productPrice").getValue(Double.class);
                            int productQuantity = dataSnapshot.child("productQuantity").getValue(Integer.class);
                            productId = dataSnapshot.child("id").getValue(String.class);
                            // Hiển thị thông tin sản phẩm trong giao diện
                            binding.txtReviewName.setText(productName);
                            binding.txtReviewPrice.setText(String.format("%.0f đ", productPrice));
                            binding.txtReviewQty.setText(String.format("Số lượng: %d", productQuantity));
                            Picasso.get().load(productThumb).into(binding.imvReviewThumb);

                        } else {
                            Toast.makeText(ReviewActivity.this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ReviewActivity.this, "Đã xảy ra lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Không nhận được orderId hoặc vị trí sản phẩm từ Intent", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không nhận được orderId từ Intent", Toast.LENGTH_SHORT).show();
        }

        toolbar = findViewById(R.id.tbReview);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);

                // Thiết lập AlertDialog
                builder.setTitle("Bạn muốn rời trang này?");
                builder.setMessage("Nếu bạn rời trang, những thay đổi của bạn sẽ không được lưu.");
                builder.setPositiveButton("Ở lại trang", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Khi người dùng nhấn "Đồng ý", kết thúc hoạt động
                        dialog.dismiss();

                    }
                });

                // Thiết lập nút "Hủy" cho AlertDialog
                builder.setNegativeButton("Rời trang", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng AlertDialog
                        finish();
                    }
                });

                // Hiển thị AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                builder.setTitle("Bạn muốn rời trang này?");
                builder.setMessage("Nếu bạn rời trang, những thay đổi của bạn sẽ không được lưu.");
                builder.setPositiveButton("Ở lại trang", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Rời trang", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = binding.edtReview.getText().toString();
                String imgURL = "";
                float rating = binding.rtbReviewRating.getRating();

                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(ReviewActivity.this, "Vui lòng nhập nội dung đánh giá", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.imvReviewImg.getDrawable() == null) {
                    Toast.makeText(ReviewActivity.this, "Vui lòng thêm hình ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                Drawable drawable = binding.imvReviewImg.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                    String productId = getIntent().getStringExtra("productId");
                    if (productId != null) {
                        // Sử dụng productId ở đây để lưu đánh giá
                        saveImageToFirebaseStorage(bitmap, content, rating, productId, user.getUid());
                        // Xóa sản phẩm khỏi Firebase
                        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Lấy danh sách các sản phẩm
                                    DatabaseReference itemsRef = databaseReference.getParent();
                                    itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                int currentPosition = 0;
                                                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                                    if (!itemSnapshot.exists()) {
                                                        continue;
                                                    }
                                                    itemsRef.child(String.valueOf(currentPosition)).setValue(itemSnapshot.getValue());
                                                    currentPosition++;
                                                }
//                                                while (currentPosition < dataSnapshot.getChildrenCount()) {
//                                                    itemsRef.child(String.valueOf(currentPosition)).removeValue();
//                                                    currentPosition++;
//                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Xử lý khi có lỗi xảy ra
                                            Log.e("ReviewActivity", "Error updating item positions: " + databaseError.getMessage());
                                        }
                                    });
                                    Toast.makeText(ReviewActivity.this, "Ba mẹ đã gửi đánh giá thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Xử lý khi xóa sản phẩm không thành công
                                    Log.e("ReviewActivity", "Error removing item: " + task.getException().getMessage());
                                    Toast.makeText(ReviewActivity.this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    } else {
                        Toast.makeText(ReviewActivity.this, "Lỗi: Không tìm thấy ID sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    private String saveImageToFirebaseStorage(Bitmap bitmap, final String content, final float rating, String productId, String userId) {
        // Tạo tên ngẫu nhiên cho hình ảnh
        String imageName = UUID.randomUUID().toString() + ".jpg";

        // Lấy tham chiếu đến Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageName);

        // Chuyển đổi Bitmap thành byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Upload hình ảnh lên Firebase Storage
        UploadTask uploadTask = storageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Lấy URL của hình ảnh sau khi upload thành công
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        // Gọi phương thức để lưu đánh giá vào Realtime Database, truyền imageURL vào đó
                        saveReviewToFirebase(content, imageURL, rating, productId, userId);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi upload hình ảnh thất bại
            }
        });
        return imageName;
    }

    private void saveReviewToFirebase(String content, String imgURL, float rating, String productId, String userId) {
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("content", content);
        reviewData.put("imageURL", imgURL);
        reviewData.put("rating", rating);
        reviewData.put("productId", productId);
        reviewData.put("userId", userId);

        // Thêm dữ liệu đánh giá vào Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Review");
        databaseReference.child("Review").push().setValue(reviewData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xử lý khi lưu dữ liệu thành công (nếu cần)
                        Toast.makeText(ReviewActivity.this, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi lưu dữ liệu thất bại (nếu cần)
                        Toast.makeText(ReviewActivity.this, "Đánh giá thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showBottomSheet() {
        Dialog dialog = new Dialog(ReviewActivity.this);
        dialog.setContentView(R.layout.bottom_sheet_profile_photo);

        LinearLayout bsCamera = dialog.findViewById(R.id.bsCamera);
        bsCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCam = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);
                dialog.dismiss();
            }
        });

        LinearLayout bsGallery = dialog.findViewById(R.id.bsGallery);
        bsGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCam = false;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
