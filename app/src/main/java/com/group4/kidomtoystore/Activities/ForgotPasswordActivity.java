package com.group4.kidomtoystore.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.group4.kidomtoystore.Adapters.ViewPagerAdapter;
import com.group4.kidomtoystore.Adapters.ViewResetPasswordAdapter;
import com.group4.kidomtoystore.Fragments.ForgotPassEmailFragment;
import com.group4.kidomtoystore.Fragments.ForgotPassSMSFragment;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityForgotPasswordBinding;
import com.group4.kidomtoystore.databinding.FragmentForgotPassEmailBinding;


public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewResetPasswordAdapter viewResetPasswordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding.tbForgotPass.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tabLayout = findViewById(R.id.tlForgotPass);
        viewPager2 = findViewById(R.id.vpForgotPass);
        viewResetPasswordAdapter = new ViewResetPasswordAdapter(this);
        viewPager2.setAdapter(viewResetPasswordAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


    }
}