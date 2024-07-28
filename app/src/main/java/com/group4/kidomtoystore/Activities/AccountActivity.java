package com.group4.kidomtoystore.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.group4.kidomtoystore.databinding.ActivityAccountBinding;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityAccountSettingBinding;

import io.reactivex.rxjava3.annotations.NonNull;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

//        ToolBar
        if (binding.tbAccountInfo != null) {
            binding.tbAccountInfo.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        //Show User Profile Data
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
                        String email = userSnapshot.child("email").getValue(String.class);
                        String dob = userSnapshot.child("dob").getValue(String.class);
                        String phoneNumb = userSnapshot.child("phoneNumb").getValue(String.class);
                        String gender = userSnapshot.child("gender").getValue(String.class);
                        Uri photoUrl = currentUser.getPhotoUrl();


                        // Hiển thị thông tin của người dùng trên giao diện
                        binding.txtProfileName.setText(fullName);
                        binding.txtEmail.setText(email);
                        binding.txtPhone.setText(phoneNumb);
                        binding.txtDOB.setText(dob);
                        binding.txtGender.setText(gender);

//                         Hiển thị ảnh đại diện nếu có
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

        binding.btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, UpdateAccountInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}