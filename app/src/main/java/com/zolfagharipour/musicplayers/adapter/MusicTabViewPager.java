package com.zolfagharipour.musicplayers.adapter;

import com.zolfagharipour.musicplayers.fragment.MusicListFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MusicTabViewPager extends FragmentStateAdapter {
    public MusicTabViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MusicListFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
