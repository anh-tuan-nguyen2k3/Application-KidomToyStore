package com.group4.kidomtoystore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.group4.kidomtoystore.Activities.LoginActivity;
import com.group4.kidomtoystore.Activities.MainActivity;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.FragmentForgotPassEmailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPassEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPassEmailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentForgotPassEmailBinding binding;
    private FirebaseAuth authProfile;

    public ForgotPassEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPassEmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPassEmailFragment newInstance(String param1, String param2) {
        ForgotPassEmailFragment fragment = new ForgotPassEmailFragment();
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
        binding = FragmentForgotPassEmailBinding.inflate(inflater, container, false);

        addEvents();

        return binding.getRoot();
    }

    private void addEvents() {
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.edtEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), "Ba mẹ vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                    binding.edtEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getActivity(), "Email sai định dạng!", Toast.LENGTH_SHORT).show();
                }else{
                    binding.progressbar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Kiểm tra hộp thư của ba mẹ để xác nhận liên kết đổi mật khẩu", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "Gửi liên kết thất bại!", Toast.LENGTH_SHORT).show();
                }
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

}