package com.zolfagharipour.musicplayers.controller.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.TabViewPagerAdapter;
import com.zolfagharipour.musicplayers.controller.activity.MusicPlayersActivity;
import com.zolfagharipour.musicplayers.enums.PlayRepeatMode;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.SongRepository;
import com.zolfagharipour.musicplayers.utils.MusicUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


public class TabFragment extends Fragment {

    private SongRepository mRepository;
    private Toolbar mToolbar;
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private CardView mCardView;
    private MusicMenuListener mSelectedListener;

    private ImageView mImageViewPopupLayoutCover, mImageViewPopupLayoutPrevious, mImageViewPopupLayoutPlayPause, mImageViewPopupLayoutNext;
    private TextView mTextViewPopupLayoutTitle;

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
        handleControlLayout();
        setToolbar();
        setViewPager();
        setTabLayout();
        setListener();

        return view;
    }

    private void setStatusBarColor() {
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
        mImageViewPopupLayoutCover = view.findViewById(R.id.fragmentTabPopupImageViewCover);
        mImageViewPopupLayoutPrevious = view.findViewById(R.id.fragmentTabPopupImageViewPrevious);
        mImageViewPopupLayoutPlayPause = view.findViewById(R.id.fragmentTabPopupImageViewPlay);
        mImageViewPopupLayoutNext = view.findViewById(R.id.fragmentTabPopupImageViewNext);
        mTextViewPopupLayoutTitle = view.findViewById(R.id.fragmentTabPopupTextViewTitle);
        mCardView = view.findViewById(R.id.cardViewContainerControlLayout);
        mTextViewPopupLayoutTitle.setSelected(true);

        if (mRepository.getMediaPlayer() != null) {
            mRepository.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mRepository.getMediaPlayer() != null) {
                        prepareCompletionListener();
                        handleControlLayout();
                    }

                }
            });
        }

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

    private void handleControlLayout() {

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mCardView.setVisibility(View.GONE);
        }
        else {
           mCardView.setVisibility(View.VISIBLE);
        }

        Song song;
        if (mRepository.getCurrentSong() == null) {
            if (mRepository.getSongList().size() == 0)
                return;
            song = mRepository.getSongList().get(0);
        } else
            song = mRepository.getCurrentSong();

        mTextViewPopupLayoutTitle.setText(song.getTitle());
        Glide
                .with(this)
                .asBitmap()
                .load(song.getImage())
                .placeholder(R.drawable.ic_cover_list_round)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(64)))
                .into(mImageViewPopupLayoutCover);
    }


    private void prepareCompletionListener() {
        int currentPage = mRepository.getCurrentSongPositionInItsList();
        if (!mRepository.isShuffleOn() && mRepository.getPlayRepeatMode() == PlayRepeatMode.REPEAT_ALL) {
            if (mRepository.getCurrentSongPositionInItsList() < mRepository.getCurrentSongList().size() - 1)
                currentPage++;
            else if (mRepository.getCurrentSongPositionInItsList() == mRepository.getCurrentSongList().size() - 1) {
                currentPage = 0;
            }
        } else if (mRepository.isShuffleOn() && mRepository.getPlayRepeatMode() == PlayRepeatMode.REPEAT_ALL) {
            currentPage = MusicUtils.getRandomNumberForShuffle(mRepository.getCurrentSongPositionInItsList(), mRepository.getCurrentSongList().size());
        } else if (mRepository.getPlayRepeatMode() == PlayRepeatMode.REPEAT_ONE) {
            currentPage = mRepository.getCurrentSongPositionInItsList();
        } else if (mRepository.getPlayRepeatMode() == PlayRepeatMode.REPEAT_OFF) {
            mRepository.getMediaPlayer().seekTo(0);
            return;
        }

        if (currentPage >= 0) {

            Song song = mRepository.getCurrentSongList().get(currentPage);
            mRepository.setCurrentSongPositionInItsList(currentPage);
            mRepository.setCurrentSong(song);
            mRepository.setCurrentPlayingSong(song);

            mRepository.getMediaPlayer().stop();
            mRepository.setMediaPlayer(getActivity(), Uri.parse(song.getPath()));
            mRepository.getMediaPlayer().start();
        }
    }

    public interface MusicMenuListener {
        void onRefreshSongList();

        void onUpdatePlayListsSong();
    }
}