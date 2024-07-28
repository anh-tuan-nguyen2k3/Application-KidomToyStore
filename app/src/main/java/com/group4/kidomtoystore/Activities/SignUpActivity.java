package com.group4.kidomtoystore.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group4.kidomtoystore.Models.User;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivitySignUpBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    DatePickerDialog datePickerDialog;
    //Set gender
    ArrayAdapter<String> genderAdapter;
    ArrayList<String> genderData;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Intent intent;
    boolean openCam;
    private String userId;


    public static final String TAG = "RegisterActivity";

    public static LoginActivity mLogin_activity;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
                }else{
                    Uri selectPhotoUri = result.getData().getData();
                    binding.imvAvatar.setImageURI(selectPhotoUri);
                }
            }
        });

        binding.edtFullName.requestFocus();

        loadGenderData();
        getData();
        addEvents();
    }

    private void getData() {
        intent = getIntent();

        String email = intent.getStringExtra("email");
        binding.edtEmail.setText(email);
    }

//    private void uploadProfileImage() {
//    }


    private void loadGenderData() {
        String [] genders ={"Nữ", "Nam", "Khác"};
        genderData = new ArrayList<>(Arrays.asList(genders));
        genderAdapter = new ArrayAdapter<String>(SignUpActivity.this, R.layout.spinner_item, genderData);
//        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spGender.setAdapter(genderAdapter);
    }

    private void addEvents() {

        binding.imvButtonEdit.setOnClickListener(view -> showBottomSheet());

        //set Calendar
        binding.edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(SignUpActivity.this,R.style.MyDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                binding.edtDOB.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
                datePickerDialog.getButton(datePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.primary_500));
                datePickerDialog.getButton(datePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.primary_500));
            }
        });

        //set button change color
        binding.edtFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtDOB.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                    binding.btnContinue.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.button_available));
                }else{
                    binding.btnContinue.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtDOB.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                    binding.btnContinue.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.button_available));
                }else{
                    binding.btnContinue.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });
        binding.edtDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtDOB.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                    binding.btnContinue.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.button_available));
                }else{
                    binding.btnContinue.setBackgroundResource(R.drawable.button_disable);
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
                if(!TextUtils.isEmpty(binding.edtFullName.getText().toString()) && !TextUtils.isEmpty(binding.edtDOB.getText().toString()) && !TextUtils.isEmpty(binding.edtPhone.getText().toString()) && !TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                    binding.btnContinue.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.button_available));
                }else{
                    binding.btnContinue.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });


        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = getIntent();
                String pass = intent.getStringExtra("pass");

//                Integer ava = binding.imvAvatar.getImageAlpha();
                String name = binding.edtFullName.getText().toString().trim();
                String phone = binding.edtPhone.getText().toString().trim();
                String email = binding.edtEmail.getText().toString().trim();
                String gender = binding.spGender.getSelectedItem().toString();
                String dob = binding.edtDOB.getText().toString().trim();


                //validate
                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(SignUpActivity.this, "Nhập tên của ba mẹ", Toast.LENGTH_SHORT).show();
                    binding.edtFullName.setError("Không được bỏ trống");
                    binding.edtFullName.requestFocus();
                }else if(TextUtils.isEmpty(phone)){
                    Toast.makeText(SignUpActivity.this, "Nhập số điện thoại của ba mẹ", Toast.LENGTH_SHORT).show();
                    binding.edtPhone.setError("Không được bỏ trống");
                    binding.edtPhone.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignUpActivity.this, "Nhập địa chỉ email của ba mẹ", Toast.LENGTH_SHORT).show();
                    binding.edtEmail.requestFocus();
                }else if(TextUtils.isEmpty(dob)) {
                    Toast.makeText(SignUpActivity.this, "Nhập sinh nhật của ba mẹ", Toast.LENGTH_SHORT).show();
                    binding.edtDOB.setError("Không được bỏ trống");
                    binding.edtDOB.requestFocus();
                }else if(phone.length() < 10 && !phone.substring(0,1).equals("0")) {
                    Toast.makeText(SignUpActivity.this, "Số điện thoại sai định dạng", Toast.LENGTH_SHORT).show();
                    binding.edtPhone.setError("Sai định dạng");
                    binding.edtPhone.requestFocus();
                }else{
                    registerUser(name, email, phone, dob, gender, pass);
                }
            }
        });
    }

    private void registerUser(String name, String email, String phone, String dob, String gender, String pass) {

        auth = FirebaseAuth.getInstance();

        //Create user profile
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                userId = firebaseUser.getUid();

                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                firebaseUser.updateProfile(profileChangeRequest);

                                //Enter user data into the Firebase Realtime Database
                                User user = new User(name, phone, email, dob, gender);

                                //extract user reference from Database for registered user
                                reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser.sendEmailVerification();

                                            // Upload và lưu avatarUrl
                                            if (selectedImageUri != null) {
                                                // Tạo một tên file ngẫu nhiên cho ảnh
                                                String fileName = "avatar_" + System.currentTimeMillis() + ".jpg";
                                                // Gọi phương thức uploadAndSetAvatar với cả hai đối số
                                                uploadAndSetAvatar(selectedImageUri, fileName, userId);
                                            }

                                            showSuccessDialog();
                                            auth.signOut();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent1 = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    intent1.putExtra("register", true);
                                                    startActivity(intent1);
                                                    finish();
                                                }
                                            }, 1000);

                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại. Ba mẹ vui lòng đăng ký lại!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            } else {
                                Log.e(TAG, "FirebaseUser is null.");
                            }
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                binding.edtEmail.setError("Email của ba mẹ không hợp lệ hoặc đã tồn tại. Vui lòng nhập email khác");
                                binding.edtEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                binding.edtEmail.setError("Email của ba mẹ không hợp lệ hoặc đã tồn tại. Vui lòng nhập email khác");
                                binding.edtEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (openCam) {
                // Xử lý ảnh từ camera
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                selectedImageUri = getImageUri(this, imageBitmap);
            } else {
                // Xử lý ảnh từ thư viện ảnh
                selectedImageUri = data.getData();
            }
            // Hiển thị ảnh lên ImageView
            Glide.with(this).load(selectedImageUri).into(binding.imvAvatar);
        }
    }

    private void uploadAndSetAvatar(Uri imageUri, String fileName, String userId) {
        // Tạo tham chiếu đến Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("avatars").child(fileName);

        // Tải ảnh lên Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL của ảnh sau khi upload thành công
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String avatarURL = uri.toString();
                        // Gọi phương thức để lưu thông tin người dùng vào Realtime Database, truyền avatarURL vào đó
                        saveUserDataToFirebase(avatarURL, userId);
                    }).addOnFailureListener(e -> {
                        // Xử lý khi không thể lấy URL của ảnh
                        Toast.makeText(this, "Không thể lấy URL của ảnh", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi upload ảnh không thành công
                    Toast.makeText(this, "Không thể tải ảnh lên, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                });
    }



    private void saveUserDataToFirebase(String avatarURL, String userId) {
        // Lấy tham chiếu đến Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        // Lưu URL của ảnh avatar vào Firebase Realtime Database
        userRef.child("avatarUrl").setValue(avatarURL).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Hiển thị thông báo khi lưu thành công (nếu cần)
                Toast.makeText(this, "Lưu ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý khi lưu không thành công (nếu cần)
                Toast.makeText(this, "Lưu ảnh đại diện không thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }



    // Hàm hỗ trợ để lấy URI từ Bitmap
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
        Dialog dialog = new Dialog(SignUpActivity.this);
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




    private void showSuccessDialog() {
        Dialog dialog = new Dialog(SignUpActivity.this);

        dialog.setContentView(R.layout.dialog_successful);

        TextView text = dialog.findViewById(R.id.txtDialogText);
        text.setText("Để hoàn thành đăng ký tài khoản. Ba mẹ vui lòng xác thực email được hệ thống gửi.");

        ProgressBar progressBar = dialog.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}