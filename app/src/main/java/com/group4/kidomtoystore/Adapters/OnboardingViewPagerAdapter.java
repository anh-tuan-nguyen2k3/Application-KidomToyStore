package com.group4.kidomtoystore.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.group4.kidomtoystore.Fragments.OnboardingFragment3;
import com.group4.kidomtoystore.Fragments.OnboardingFragment4;
import com.group4.kidomtoystore.Fragments.OnboardingFragment5;
import com.group4.kidomtoystore.Fragments.OnboardingFragment6;

public class OnboardingViewPagerAdapter extends FragmentStateAdapter {
    public OnboardingViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OnboardingFragment3();
            case 1:
                return new OnboardingFragment4();
            case 2:
                return new OnboardingFragment5();
            case 3:
                return new OnboardingFragment6();

            default:
                return new OnboardingFragment3();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
