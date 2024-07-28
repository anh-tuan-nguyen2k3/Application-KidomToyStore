package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityForgotPasswordOtpBinding;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordOTP extends AppCompatActivity {

    private ActivityForgotPasswordOtpBinding binding;

    private String verificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String phone = getIntent().getStringExtra("phone");
        binding.txtReceivedPhone.setText(phone);

        verificationId = getIntent().getStringExtra("verificationId");

        codeFocus();
        addEvents();
    }

    private void addEvents() {
        binding.txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //depricated
                Toast.makeText(ForgotPasswordOTP.this, "Đã gửi lại mã OTP cho ba mẹ", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.progressbar.setVisibility(View.VISIBLE);

                String otp1 = binding.opt1.getText().toString().trim();
                String otp2 = binding.opt2.getText().toString().trim();
                String otp3 = binding.opt3.getText().toString().trim();
                String otp4 = binding.opt4.getText().toString().trim();
                String otp5 = binding.opt5.getText().toString().trim();
                String otp6 = binding.opt6.getText().toString().trim();

                if (otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() ||
                    otp4.isEmpty() || otp5.isEmpty() || otp6.isEmpty()){
                    Toast.makeText(ForgotPasswordOTP.this, "Mã OTP không đúng!", Toast.LENGTH_SHORT).show();
                }else{
                    if (verificationId != null) {
                        String code = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    binding.progressbar.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(ForgotPasswordOTP.this, LoginActivity.class);
                                    intent.putExtra("phone", binding.txtReceivedPhone.getText().toString());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else{
                                    binding.progressbar.setVisibility(View.GONE);
                                    Toast.makeText(ForgotPasswordOTP.this, "Mã OTP không đúng!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    private void codeFocus() {
        binding.opt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.opt2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.opt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.opt3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.opt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.opt4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.opt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.opt5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.opt5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.opt6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

