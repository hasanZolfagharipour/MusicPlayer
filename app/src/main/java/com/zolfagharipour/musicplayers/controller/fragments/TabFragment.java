package com.zolfagharipour.musicplayers.controller.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.TabViewPagerAdapter;
import com.zolfagharipour.musicplayers.controller.activity.MusicPlayersActivity;
import com.zolfagharipour.musicplayers.repository.SongRepository;
import com.zolfagharipour.musicplayers.utils.MusicUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


public class TabFragment extends Fragment {

    private SongRepository mRepository;
    private Toolbar mToolbar;
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private MusicMenuListener mSelectedListener;

    public static TabFragment newInstance() {

        Bundle args = new Bundle();

        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = SongRepository.getInstance();
        mRepository.setSongList(MusicUtils.getSongList(getActivity().getApplicationContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        setStatusBarColor();
        findViews(view);
        setToolbar();
        setViewPager();
        setTabLayout();
        setListener();

        return view;
    }


    private void setStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorDefaultTabGray));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void findViews(View view) {
        mToolbar = view.findViewById(R.id.tabFragmentToolbar);
        mViewPager = view.findViewById(R.id.tabFragmentViewPager);
        mTabLayout = view.findViewById(R.id.tabFragmentTabLayout);
    }

    private void setToolbar() {
        setHasOptionsMenu(true);
        ((MusicPlayersActivity) getActivity()).setSupportActionBar(mToolbar);
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
                        tab.setText(getString(R.string.playlist));
                }
            }
        }).attach();
    }

    private void setViewPager() {
        mViewPager.setAdapter(new TabViewPagerAdapter(getActivity()));
    }

    private void setListener() {
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mSelectedListener.onUpdatePlayListsSong();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.music_tab_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tabMenuRefresh:
                mRepository.setSongList(MusicUtils.getSongList(getActivity()));
                mSelectedListener.onRefreshSongList();
                return true;
            case R.id.tabMenuExitApp:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MusicMenuListener)
            mSelectedListener = (MusicMenuListener) context;

    }

    public interface MusicMenuListener {
        void onRefreshSongList();

        void onUpdatePlayListsSong();
    }
}