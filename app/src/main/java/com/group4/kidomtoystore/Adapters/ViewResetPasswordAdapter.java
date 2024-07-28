package com.group4.kidomtoystore.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.group4.kidomtoystore.Fragments.ForgotPassEmailFragment;
import com.group4.kidomtoystore.Fragments.ForgotPassSMSFragment;

import io.reactivex.rxjava3.annotations.NonNull;

public class ViewResetPasswordAdapter  extends FragmentStateAdapter {

    public ViewResetPasswordAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new ForgotPassEmailFragment();
            case 1:
                return new ForgotPassSMSFragment();
            default:
                return new ForgotPassEmailFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

