package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityLoginBinding;

import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth authProfile;
    public static String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        authProfile = FirebaseAuth.getInstance();
        binding.txtInputEmail.requestFocus();
        TAG = getIntent().getStringExtra("register");
        
        addEvents();
    }

    private void addEvents() {

        binding.txtInputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.txtInputEmail.getText().toString()) && !TextUtils.isEmpty(binding.txtInputPassword.getText().toString())) {
                    binding.btnLogIn.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_available));
                }else{
                    binding.btnLogIn.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });
        binding.txtInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(binding.txtInputEmail.getText().toString()) && !TextUtils.isEmpty(binding.txtInputPassword.getText().toString())) {
                    binding.btnLogIn.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_available));
                }else{
                    binding.btnLogIn.setBackgroundResource(R.drawable.button_disable);
                }
            }
        });

        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.txtInputEmail.getText().toString().trim();
                String password = binding.txtInputPassword.getText().toString().trim();

                if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
                    binding.txtInputEmail.setError("Email không đúng định dạng");
                    binding.txtInputEmail.requestFocus();
                }else if(password.length() < 6 ) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                    binding.txtInputPassword.setError("Mật khẩu yếu");
                    binding.txtInputPassword.requestFocus();
                }else{
                    binding.progressbar.setVisibility(View.GONE);
                    loginUser(email, password);
                }
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //forgot password
        binding.txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //get Instance of the current user
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomePageFull.class);
                        startActivity(intent);
                        finish();
                    }else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }
                }else{
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        binding.txtInputEmail.setError("Tài khoản không tồn tại hoặc email sai!");
                        binding.txtInputEmail.requestFocus();
                    } catch (FirebaseAuthInvalidUserException e) {
                        binding.txtInputEmail.setError("Tài khoản không tồn tại hoặc email sai!");
                        binding.txtInputEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        Dialog dialog = new Dialog(LoginActivity.this);

        dialog.setContentView(R.layout.alert_dialog_email);

        Button btnContinue = dialog.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                //to email app in new window and not within our app
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean fromSignup = getIntent().getBooleanExtra("fromSignup", false);
        if (!fromSignup) {
            FirebaseUser currentUser = authProfile.getCurrentUser();
            if (currentUser != null) {
                startActivity(new Intent(LoginActivity.this, HomePageFull.class));
                finish();
            }
        }
    }
}