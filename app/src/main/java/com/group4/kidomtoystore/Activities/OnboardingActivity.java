package com.group4.kidomtoystore.Activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.group4.kidomtoystore.Adapters.OnboardingViewPagerAdapter;
import com.group4.kidomtoystore.R;
import com.google.android.material.tabs.TabLayoutMediator;

import me.relex.circleindicator.CircleIndicator3;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private LinearLayout layoutBottom;
    private Button buttonNext;
    private boolean isLastPage = false; // Biến để kiểm tra trạng thái cuối cùng của ViewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initUI();

        OnboardingViewPagerAdapter onboardingViewPagerAdapter = new OnboardingViewPagerAdapter(this);
        viewPager.setAdapter(onboardingViewPagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Kiểm tra xem trang hiện tại có phải là trang cuối cùng không
                isLastPage = position == onboardingViewPagerAdapter.getItemCount() - 1;
                // Ẩn/hiện các phần tử của tab layout tùy theo trạng thái của isLastPage
                layoutBottom.setVisibility(isLastPage ? View.GONE : View.VISIBLE);
            }
        });

        // Xử lý sự kiện khi người dùng cố gắng kéo ngược lại
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Kiểm tra nếu không phải trang cuối cùng thì cho phép lướt ngược lại
                if (!isLastPage) {
                    onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < onboardingViewPagerAdapter.getItemCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true); // Chuyển đến trang tiếp theo
                }
            }
        });

        // Kết nối ViewPager2 với Circle Indicator
        CircleIndicator3 indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        // Đặt Drawable cho Indicator
        indicator.setBackgroundColor(R.drawable.button_available);
    }

    private void initUI(){
        viewPager = findViewById(R.id.view_pager_onboarding);
        layoutBottom = findViewById(R.id.layout_bottom);
        buttonNext = findViewById(R.id.btn_next);
    }
}
