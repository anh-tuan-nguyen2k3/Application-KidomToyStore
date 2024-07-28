package com.group4.kidomtoystore.Fragments;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.kidomtoystore.Activities.ForgotPasswordOTP;
import com.group4.kidomtoystore.Activities.LoginActivity;
import com.group4.kidomtoystore.Activities.SignUpActivity;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.FragmentForgotPassSMSBinding;

import java.sql.Connection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPassSMSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPassSMSFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentForgotPassSMSBinding binding;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public ForgotPassSMSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPassSMSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPassSMSFragment newInstance(String param1, String param2) {
        ForgotPassSMSFragment fragment = new ForgotPassSMSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPassSMSBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        addEvents();
        return binding.getRoot();
    }


    private void addEvents() {
        binding.edtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtPhone.setBackgroundResource(R.drawable.border_red);
            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.edtPhone.getText().toString().trim();
                if(phone.isEmpty()){
                    Toast.makeText(getActivity(), "Ba mẹ vui lòng điền thông tin!", Toast.LENGTH_SHORT).show();
                }else if(phone.length() < 10 || phone.charAt(0) != '0'){
                    Toast.makeText(getActivity(), "Số điện thoại sai định dạng!", Toast.LENGTH_SHORT).show();
                }else{
                    sendOTP();
                }
            }
        });
    }

    private void sendOTP() {
        binding.progressbar.setVisibility(View.VISIBLE);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Đã gửi mã thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ForgotPasswordOTP.class);
                intent.putExtra("phone", binding.edtPhone.getText().toString().trim());
                intent.putExtra("verificationId", s);
                startActivity(intent);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(binding.edtPhone.getText().toString().trim().replaceFirst("0", "+84") )       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}