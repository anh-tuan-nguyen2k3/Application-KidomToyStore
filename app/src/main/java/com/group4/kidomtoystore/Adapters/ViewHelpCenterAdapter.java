package com.group4.kidomtoystore.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.group4.kidomtoystore.Fragments.ContactFragment;
import com.group4.kidomtoystore.Fragments.FaqFragment;
public class ViewHelpCenterAdapter extends FragmentStateAdapter {

    public ViewHelpCenterAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new FaqFragment();
            case 1:
                return new ContactFragment();

            default:
                return new FaqFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
