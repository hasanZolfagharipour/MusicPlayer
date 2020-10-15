package com.zolfagharipour.musicplayers.adapter;

;

import com.zolfagharipour.musicplayers.controller.fragments.TabAlbumFragment;
import com.zolfagharipour.musicplayers.controller.fragments.TabPlayListFragment;
import com.zolfagharipour.musicplayers.controller.fragments.TabTrackFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabViewPagerAdapter extends FragmentStateAdapter {

    public TabViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return TabTrackFragment.newInstance();
        else if (position == 1)
            return TabAlbumFragment.newInstance();
        else
            return TabPlayListFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
