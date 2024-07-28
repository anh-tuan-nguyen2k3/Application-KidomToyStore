package com.group4.kidomtoystore.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group4.kidomtoystore.Models.OrderManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityAccountSettingBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AccountSettingActivity extends AppCompatActivity {
    ActivityAccountSettingBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Dialog dialog;
    boolean openCam;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


//        ToolBar
        Toolbar toolbar = findViewById(R.id.tbAccountSetting);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (openCam) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    binding.imvAvatar.setImageBitmap(photo);
                    // Chuyển đổi Bitmap thành Uri và tải lên Firebase
                    Uri imageUri = getImageUri(AccountSettingActivity.this, photo);
                    uploadNewAvatar(imageUri);
                } else {
                    Uri selectPhotoUri = result.getData().getData();
                    binding.imvAvatar.setImageURI(selectPhotoUri);
                    // Tải lên ảnh mới lên Firebase
                    uploadNewAvatar(selectPhotoUri);
                }
            }
        });


        binding.bottomMenu.setSelectedItemId(R.id.mnProfile);
        binding.bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.mnHome  && !(getApplicationContext() instanceof HomePageFull)){
                    startActivity(new Intent(getApplicationContext(), HomePageFull.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnSearch  && !(getApplicationContext() instanceof SearchTypeActivity)){
                    startActivity(new Intent(getApplicationContext(), SearchTypeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnCart  && !(getApplicationContext() instanceof CartActivity)){
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnOrder  && !(getApplicationContext() instanceof OrderManagementActivity)){
                    startActivity(new Intent(getApplicationContext(), OrderManagementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnProfile  && !(getApplicationContext() instanceof AccountSettingActivity)){
                    startActivity(new Intent(getApplicationContext(), AccountSettingActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


        addEvents();


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra xem người dùng hiện tại có tồn tại trong dữ liệu không
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // Lấy UID của người dùng hiện tại
                    String uid = ((FirebaseUser) currentUser).getUid();

                    // Tìm kiếm nút con có key là UID của người dùng hiện tại
                    DataSnapshot userSnapshot = snapshot.child(uid);

                    // Kiểm tra xem người dùng hiện tại có tồn tại trong dữ liệu không
                    if (userSnapshot.exists()) {
                        // Lấy thông tin của người dùng hiện tại từ dataSnapshot
                        String fullName = userSnapshot.child("fullName").getValue(String.class);
                        String phoneNumb = userSnapshot.child("phoneNumb").getValue(String.class);

                        // Hiển thị thông tin của người dùng trên giao diện
                        binding.txtProfileName.setText(fullName);
                        binding.txtProfilePhoneNumber.setText(phoneNumb);

//                        Hiển thị ảnh đại diện nếu có
                        // Lấy URL ảnh đại diện từ dữ liệu Firebase
                        String avatarUrl = userSnapshot.child("avatarUrl").getValue(String.class);
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Picasso.get().load(avatarUrl).into(binding.imvAvatar);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }


    private void addEvents() {

        binding.imvButtonEdit.setOnClickListener(view -> showBottomSheet());

        binding.flUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        binding.llAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        binding.llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingActivity.this, AdressActivity.class);
                startActivity(intent);
            }
        });

        binding.llPaymentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingActivity.this, CardManagement.class);
                startActivity(intent);
            }
        });

        binding.llNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingActivity.this, NewPasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.llHelpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingActivity.this, HelpCenterActivity.class);
                startActivity(intent);
            }
        });

        binding.llPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingActivity.this, PolicyActivity.class);
                startActivity(intent);
            }
        });

        binding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutSheet();
            }
        });
    }

    private void showLogoutSheet() {
        dialog = new Dialog(AccountSettingActivity.this);
        dialog.setContentView(R.layout.layout_logout);;

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Button btnConfirm = dialog.getWindow().findViewById(R.id.btnConfirm);
        Button btnCancel = dialog.getWindow().findViewById(R.id.btnCancel);

//        Logout
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(dialog.getContext(), "Đăng xuất thành công! Hẹn gặp lại ba mẹ!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(dialog.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.show();
    }

    protected void onDestroy() {
        super.onDestroy();
        // Dismiss the dialog to prevent WindowLeaked error
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void uploadNewAvatar(Uri imageUri) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            uploadNewAvatarToStorage(imageUri, uid);
        } else {
            showToast("Không thể xác định người dùng hiện tại");
        }
    }

    private void uploadNewAvatarToStorage(Uri imageUri, String uid) {
        if (uid != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("avatars").child(uid + ".jpg");
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Lấy URL của ảnh mới tải lên
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String newAvatarUrl = uri.toString();
                            // Cập nhật URL ảnh mới vào Firebase
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            userRef.child("avatarUrl").setValue(newAvatarUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        // Hiển thị ảnh mới trên ImageView
                                        Picasso.get().load(imageUri).into(binding.imvAvatar);
                                        showToast("Ảnh đã được cập nhật thành công");
                                    })
                                    .addOnFailureListener(e -> {
                                        showToast("Đã xảy ra lỗi khi cập nhật ảnh mới: " + e.getMessage());
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        showToast("Đã xảy ra lỗi khi tải lên ảnh mới: " + e.getMessage());
                    });
        } else {
            showToast("Không thể xác định người dùng hiện tại");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                binding.imvAvatar.setImageBitmap(photo);
                // Chuyển đổi Bitmap thành Uri và tải lên Firebase
                Uri imageUri = getImageUri(this, photo);
                if (imageUri != null) {
                    // Nếu có ảnh mới, thực hiện tải lên ảnh mới
                    uploadNewAvatar(imageUri);
                }
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectPhotoUri = data.getData();
                binding.imvAvatar.setImageURI(selectPhotoUri);
                // Tải lên ảnh mới lên Firebase
                if (selectPhotoUri != null) {
                    // Nếu có ảnh mới, thực hiện tải lên ảnh mới
                    uploadNewAvatar(selectPhotoUri);
                }
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void showBottomSheet() {
        // Ẩn bàn phím ảo trước khi hiển thị dialog
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        Dialog dialog = new Dialog(AccountSettingActivity.this);
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

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}