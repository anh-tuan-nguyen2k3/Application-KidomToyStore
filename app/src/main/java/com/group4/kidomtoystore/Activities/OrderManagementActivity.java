package com.group4.kidomtoystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.group4.kidomtoystore.Adapters.ViewPagerAdapter;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.ActivityOrderManagementBinding;

public class OrderManagementActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    ActivityOrderManagementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding.bottomMenu.setSelectedItemId(R.id.mnOrder);
        binding.bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.mnHome  && !(getApplicationContext() instanceof HomePageFull)){
                    startActivity(new Intent(getApplicationContext(), HomePageFull.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnSearch  && !(getApplicationContext() instanceof SearchTypeActivity)){
                    startActivity(new Intent(getApplicationContext(), SearchTypeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnCart  && !(getApplicationContext() instanceof CartActivity)){
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnOrder  && !(getApplicationContext() instanceof OrderManagementActivity)){
                    startActivity(new Intent(getApplicationContext(), OrderManagementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == R.id.mnProfile  && !(getApplicationContext() instanceof AccountSettingActivity)){
                    startActivity(new Intent(getApplicationContext(), AccountSettingActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.vpOrder.setAdapter(viewPagerAdapter);

        binding.tlOrder.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vpOrder.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.vpOrder.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tlOrder.getTabAt(position).select();
            }
        });

    }



}