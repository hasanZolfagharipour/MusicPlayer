package com.zolfagharipour.musicplayers.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zolfagharipour.musicplayers.MusicPlayersActivity;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.MusicTabViewPager;
import com.zolfagharipour.musicplayers.repository.MusicRepository;
import com.zolfagharipour.musicplayers.utils.MusicManager;


public class MusicTabFragment extends Fragment {

    public static MusicTabFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MusicTabFragment fragment = new MusicTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MusicRepository mRepository;
    private Toolbar mToolbar;
    private ViewPager2 mViewPager;
    private MusicTabViewPager mPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = MusicRepository.getInstance();
        mRepository.setSongList(MusicManager.getSongList(getActivity().getApplicationContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_music_tab, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setToolbar();
        setViewPager();
        setTabLayout();

    }

    private void findViews(View view){
        mToolbar        = view.findViewById(R.id.listMenuToolbar);
        mViewPager      = view.findViewById(R.id.viewPagerTabs);
        mTabLayout      = view.findViewById(R.id.tabLayout);
    }

    private void setToolbar(){
        setHasOptionsMenu(true);
        ((MusicPlayersActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    private void setTabLayout(){

        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
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

    private void setViewPager(){
        mPagerAdapter = new MusicTabViewPager(getActivity());
        mViewPager.setAdapter(mPagerAdapter);
    }

}