package com.zolfagharipour.musicplayers.adapter;

;

import com.zolfagharipour.musicplayers.controller.fragments.AlbumTabFragment;
import com.zolfagharipour.musicplayers.controller.fragments.TrackTabFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MusicTabViewPagerAdapter extends FragmentStateAdapter {

    public MusicTabViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return TrackTabFragment.newInstance();
        else
            return AlbumTabFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
