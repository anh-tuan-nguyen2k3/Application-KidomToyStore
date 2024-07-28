package com.group4.kidomtoystore.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.group4.kidomtoystore.Activities.LoginActivity;
import com.group4.kidomtoystore.Activities.OnboardingActivity;
import com.group4.kidomtoystore.Activities.RegisterActivity;
import com.group4.kidomtoystore.R;

public class OnboardingFragment6 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OnboardingFragment6() {
        // Required empty public constructor
    }

    public static OnboardingFragment6 newInstance(String param1, String param2) {
        OnboardingFragment6 fragment = new OnboardingFragment6();
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
        View view = inflater.inflate(R.layout.fragment_onboarding6, container, false);
        Button buttonGetStart = view.findViewById(R.id.btngetstart);
        TextView txtSignup = view.findViewById(R.id.txtSignUp);
        buttonGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang Login khi nút được nhấn
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình đăng ký khi nút được nhấn
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}