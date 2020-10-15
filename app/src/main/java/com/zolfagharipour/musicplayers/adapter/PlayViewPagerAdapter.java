package com.zolfagharipour.musicplayers.adapter;

import com.zolfagharipour.musicplayers.controller.fragments.SongCoverFragment;
import com.zolfagharipour.musicplayers.model.Song;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PlayViewPagerAdapter extends FragmentStateAdapter {

    private List<Song> mSongList;

    public PlayViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Song> songList) {
        super(fragmentActivity);
        mSongList = songList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return SongCoverFragment.newInstance(mSongList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
}
