package com.group4.kidomtoystore.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.group4.kidomtoystore.Adapters.FragmentAdapter;
import com.group4.kidomtoystore.Adapters.ViewHelpCenterAdapter;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityHelpCenterBinding;

public class HelpCenterActivity extends AppCompatActivity {

    ActivityHelpCenterBinding binding;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding.tbHelpCenter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tlHelpCenter);
        viewPager2 = findViewById(R.id.vpHelpCenter);
        ViewHelpCenterAdapter viewHelpCenterAdapter = new ViewHelpCenterAdapter(this);
        viewPager2.setAdapter(viewHelpCenterAdapter);

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