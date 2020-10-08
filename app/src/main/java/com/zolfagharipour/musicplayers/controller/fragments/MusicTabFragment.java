package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.MusicTabViewPagerAdapter;
import com.zolfagharipour.musicplayers.repository.MusicRepository;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


public class MusicTabFragment extends Fragment {

    private MusicRepository mRepository;
    private Toolbar mToolbar;
    private ViewPager2 mViewPager;
    private MusicTabViewPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    public static MusicTabFragment newInstance() {

        Bundle args = new Bundle();

        MusicTabFragment fragment = new MusicTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = MusicRepository.getInstance();
        mRepository.setSongList(MusicManager.getSongList(getActivity().getApplicationContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_tab, container, false);

        findViews(view);
        setToolbar();
        setViewPager();
        setTabLayout();

        return view;
    }


    private void findViews(View view) {
        mToolbar = view.findViewById(R.id.listMenuToolbar);
        mViewPager = view.findViewById(R.id.viewPagerTabs);
        mTabLayout = view.findViewById(R.id.tabLayout);
    }

    private void setToolbar() {
        setHasOptionsMenu(true);
    }

    private void setTabLayout() {

        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    default:
                        tab.setText(getString(R.string.tracks));
                        break;
                    case 1:
                        tab.setText(getString(R.string.albums));
                        break;
                    case 2:
                        tab.setText(getString(R.string.singers));
                }
            }
        }).attach();
    }

    private void setViewPager() {
        mPagerAdapter = new MusicTabViewPagerAdapter(getActivity());
        mViewPager.setAdapter(mPagerAdapter);
    }

}