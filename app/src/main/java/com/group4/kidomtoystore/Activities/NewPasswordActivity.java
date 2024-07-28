package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Models.User;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityNewPasswordBinding;

public class NewPasswordActivity extends AppCompatActivity {

    ActivityNewPasswordBinding binding;

    DatabaseReference reference;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //        ToolBar
        Toolbar toolbar = findViewById(R.id.tbNewPassword);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        addEvents();
    }

    private void addEvents() {
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = binding.edtCurrentPassword.getEditableText().toString();
                String password = binding.edtNewPass.getEditableText().toString();
                String cfPassword = binding.edtNewPassAgain.getEditableText().toString();
                if (password.isEmpty() || cfPassword.isEmpty()) {
                    Toast.makeText(NewPasswordActivity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                }

                String phone = getIntent().getStringExtra("phone");
                reference = FirebaseDatabase.getInstance().getReference("Users");

                // Get Current User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null && user.getEmail() != null) {
                    // Xác minh lại mật khẩu
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Nếu xác minh thành công, kiểm tra mật khau mới có khớp không
                                        if (!password.equals(cfPassword)) {
                                            Toast.makeText(NewPasswordActivity.this, "Mật khẩu mới không khớp! Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Nếu mật khẩu mới khớp, thực hiện cập nhật
                                            user.updatePassword(password)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Cập nhật mật khẩu thành công
                                                                Toast.makeText(NewPasswordActivity.this, "Mật khẩu đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(NewPasswordActivity.this, AccountSettingActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                // Cập nhật mật khẩu không thành công
                                                                Toast.makeText(NewPasswordActivity.this, "Có lỗi xảy ra khi cập nhật mật khẩu mới!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Xác minh mật khẩu hiện tại không thành công
                                        Toast.makeText(NewPasswordActivity.this, "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}