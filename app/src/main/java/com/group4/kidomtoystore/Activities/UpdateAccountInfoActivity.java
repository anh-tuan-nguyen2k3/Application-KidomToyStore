package com.group4.kidomtoystore.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityAccountSettingBinding;
import com.group4.kidomtoystore.databinding.ActivityUpdateAccountInfoBinding;
import com.squareup.picasso.Picasso;
import com.group4.kidomtoystore.Models.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

import java.util.ArrayList;


public class UpdateAccountInfoActivity extends AppCompatActivity {
   ActivityUpdateAccountInfoBinding binding;
    private FirebaseAuth authProfile;
    ActivityResultLauncher<Intent> activityResultLauncher;
    DatePickerDialog datePickerDialog;
    ArrayAdapter<String> genderAdapter;
    ArrayList<String> genderData;
    Intent intent;
    private User currentUser;

    boolean openCam;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateAccountInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


//        Toolbar
        Toolbar toolbar = findViewById(R.id.tbUpdateInfo);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (openCam) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    binding.imvAvatar.setImageBitmap(photo);
                    // Chuyển đổi Bitmap thành Uri và tải lên Firebase
                    Uri imageUri = getImageUri(UpdateAccountInfoActivity.this, photo);
                    uploadNewAvatar(imageUri);
                } else {
                    Uri selectPhotoUri = result.getData().getData();
                    binding.imvAvatar.setImageURI(selectPhotoUri);
                    // Tải lên ảnh mới lên Firebase
                    uploadNewAvatar(selectPhotoUri);
                }
            }
        });



//        Khởi tạo danh sách giới tính và adapter ở mức độ lớp
        genderData = new ArrayList<>();
        genderData.add("Nam");
        genderData.add("Nữ");
        genderData.add("Khác");
        genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderData);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUpdateGender.setAdapter(genderAdapter);

        addEvents();
        fetchUserData();
    }



    private void addEvents() {
        binding.edtUpdateDOB.setOnClickListener(view -> showDatePickerDialog());
        binding.spUpdateGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = genderData.get(position);
                setupGenderSpinner(selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        binding.imvButtonEdit.setOnClickListener(view -> showBottomSheet());

        binding.btnSaveUpdate.setOnClickListener(view -> {
            validateAndUpdate();
        });
    }

    private void setupGenderSpinner(String selectedGender) {
        // Xóa các dữ liệu cũ và thêm dữ liệu mới
        genderData.clear();
        genderData.add("Nam");
        genderData.add("Nữ");
        genderData.add("Khác");

        // Cập nhật adapter
        genderAdapter.notifyDataSetChanged();

        // Chọn giới tính mặc định nếu đã có dữ liệu
        if (selectedGender != null) {
            int genderPosition = genderAdapter.getPosition(selectedGender);
            if (genderPosition != -1) {
                binding.spUpdateGender.setSelection(genderPosition);
            } else {
                // Nếu không tìm thấy giới tính mặc định, chọn mục "Khác"
                binding.spUpdateGender.setSelection(genderAdapter.getCount() - 1);
            }
        }
    }

    private void fetchUserData() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        String fullName = snapshot.child("fullName").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String dob = snapshot.child("dob").getValue(String.class);
                        String phoneNumb = snapshot.child("phoneNumb").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        Uri photoUrl = currentUser.getPhotoUrl();

                        binding.edtUpdateName.setText(fullName);
                        binding.edtUpdateEmail.setText(email);
                        binding.edtUpdatePhone.setText(phoneNumb);
                        binding.edtUpdateDOB.setText(dob);

                        // Update dữ liệu trong genderData
                        genderData.clear();
                        genderData.add("Nam");
                        genderData.add("Nữ");
                        genderData.add("Khác");
                        // Nếu gender từ Firebase không rỗng và nằm trong danh sách genderData
                        if (!TextUtils.isEmpty(gender) && genderData.contains(gender)) {
                            // Set selected item cho Spinner
                            binding.spUpdateGender.setSelection(genderData.indexOf(gender));
                        }

//                        Hiển thị ảnh đại diện nếu có
                        // Lấy URL ảnh đại diện từ dữ liệu Firebase
                        String avatarUrl = snapshot.child("avatarUrl").getValue(String.class);
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Picasso.get().load(avatarUrl).into(binding.imvAvatar);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });
        }
    }


    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(UpdateAccountInfoActivity.this,
                R.style.MyDatePickerStyle,
                (view, year, monthOfYear, dayOfMonth) -> binding.edtUpdateDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year),
                mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getButton(datePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(UpdateAccountInfoActivity.this, R.color.primary_500));
        datePickerDialog.getButton(datePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(UpdateAccountInfoActivity.this, R.color.primary_500));
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
        Dialog dialog = new Dialog(UpdateAccountInfoActivity.this);
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


    private void validateAndUpdate() {
        String newName = binding.edtUpdateName.getText().toString().trim();
        String newPhone = binding.edtUpdatePhone.getText().toString().trim();
        String newEmail = binding.edtUpdateEmail.getText().toString().trim();
        String newDOB = binding.edtUpdateDOB.getText().toString().trim();

        if (TextUtils.isEmpty(newName)) {
            showToast("Nhập tên của ba mẹ");
            binding.edtUpdateName.setError("Không được bỏ trống");
            binding.edtUpdateName.requestFocus();
        } else if (TextUtils.isEmpty(newPhone)) {
            showToast("Nhập số điện thoại của ba mẹ");
            binding.edtUpdatePhone.setError("Không được bỏ trống");
            binding.edtUpdatePhone.requestFocus();
        } else if (newPhone.length() != 10 || !newPhone.startsWith("0")) {
            showToast("Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0");
            binding.edtUpdatePhone.setError("Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0");
            binding.edtUpdatePhone.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            showToast("Nhập địa chỉ email của ba mẹ");
            binding.edtUpdateEmail.requestFocus();
        } else if (TextUtils.isEmpty(newDOB)) {
            showToast("Nhập sinh nhật của ba mẹ");
            binding.edtUpdateDOB.setError("Không được bỏ trống");
            binding.edtUpdateDOB.requestFocus();
        } else {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                updateInfo(currentUser);
            } else {
                showToast("Không thể xác định người dùng hiện tại");
            }

            // Kiểm tra xem người dùng có chọn ảnh mới không
            if (imageUri != null) {
                // Nếu có ảnh mới, thực hiện tải lên ảnh mới
                uploadNewAvatar(imageUri);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                binding.imvAvatar.setImageBitmap(photo);
                // Lưu URI của ảnh từ Camera
                imageUri = getImageUri(UpdateAccountInfoActivity.this, photo);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectPhotoUri = data.getData();
                binding.imvAvatar.setImageURI(selectPhotoUri);
                // Lưu URI của ảnh từ Gallery
                imageUri = selectPhotoUri;
            }
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
                            userRef.child("avatarUrl").setValue(newAvatarUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        showToast("Đã xảy ra lỗi khi tải lên ảnh mới: " + e.getMessage());
                    });
        } else {
            showToast("Không thể xác định người dùng hiện tại");
        }
    }


    private void updateInfo(FirebaseUser currentUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        String newName = binding.edtUpdateName.getText().toString().trim();
        String newPhone = binding.edtUpdatePhone.getText().toString().trim();
        String newEmail = binding.edtUpdateEmail.getText().toString().trim();
        String newDOB = binding.edtUpdateDOB.getText().toString().trim();
        String newGender = binding.spUpdateGender.getSelectedItem().toString();


        Map<String, Object> updates = new HashMap<>();
        updates.put("fullName", newName);
        updates.put("phoneNumb", newPhone);
        updates.put("email", newEmail);
        updates.put("dob", newDOB);
        updates.put("gender", newGender);

        userRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    showToast("Thông tin đã được cập nhật thành công");
                    onBackPressed();
                })
                .addOnFailureListener(e -> showToast("Đã xảy ra lỗi. Vui lòng thử lại!" + e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

