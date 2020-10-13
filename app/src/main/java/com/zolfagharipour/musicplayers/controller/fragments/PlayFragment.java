package com.zolfagharipour.musicplayers.controller.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.PlayViewPagerAdapter;
import com.zolfagharipour.musicplayers.enums.MusicRepeatMode;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;
import com.zolfagharipour.musicplayers.utils.MusicManager;
import com.zolfagharipour.musicplayers.utils.TimeFormatPlayer;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager2.widget.ViewPager2;

public class PlayFragment extends Fragment implements MediaPlayer.OnCompletionListener {

    public static final String ARG_SONG_INSTANCE = "ArgSongInstance";
    public static final String TAG = "tag";
    private ViewPager2 mViewPager2;
    private PlayViewPagerAdapter mViewPagerAdapter;

    private AnimatedVectorDrawableCompat mAnimatedVectorDrawableCompat;
    private AnimatedVectorDrawable mAnimatedVectorDrawable;
    private TextView mTextViewSongTitle, mTextViewSongAlbum, mTextViewElapsedTime, mTextViewTotalTime;
    private ImageView mImageViewFavorite, mImageViewShuffle, mImageViewBackward,mImageViewPlayPause, mImageViewForward, mImageViewRepeat, mImageViewAddToPlayList;


    private SeekBar mSeekBar;
    private MusicRepository mRepository;
    private List<Song> mSongList;
    private Song mSong;
    private Uri mUri;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private ConstraintLayout mConstraintLayoutRoot;
    private MediaPlayer mMediaPlayer;
    

    public static PlayFragment newInstance(Song song) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_SONG_INSTANCE, song);
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialization();
        setReturnTransition(new Fade());
        setEnterTransition(new Explode());
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_play, container, false);


        findViews(view);

        setViewPager();

        updateUI();

        onConfiguration(savedInstanceState);

        setInitializationUI();

        setSeekBar();

        setListener();

        Log.d(TAG, "onCreateView: ");

        return view;
    }

    private void initialization() {
        mRepository = MusicRepository.getInstance();
        mSongList = mRepository.getCurrentSongList();
        mSong = mRepository.getCurrentSong();
        mUri = Uri.parse(mSong.getPath());
    }

    private void findViews(View view) {
        mViewPager2 = view.findViewById(R.id.viewPagerSongPlay);
        mTextViewSongTitle = view.findViewById(R.id.songPlayTitle);
        mTextViewSongAlbum = view.findViewById(R.id.songPlayAlbum);
        mTextViewElapsedTime = view.findViewById(R.id.songPlayElapsedTime);
        mTextViewTotalTime = view.findViewById(R.id.songPlayTotalTime);
        mSeekBar = view.findViewById(R.id.songPlaySeekBar);
        mConstraintLayoutRoot = view.findViewById(R.id.songPlayContainer);

        mImageViewFavorite = view.findViewById(R.id.songPlayFavorite);
        mImageViewShuffle = view.findViewById(R.id.songPlayShuffle);
        mImageViewBackward = view.findViewById(R.id.songPlayBackward);
        mImageViewPlayPause = view.findViewById(R.id.songPlayPause);
        mImageViewForward = view.findViewById(R.id.songPlayForward);
        mImageViewRepeat = view.findViewById(R.id.songPlayRepeat);
        mImageViewAddToPlayList = view.findViewById(R.id.songPlayAddToPlayList);
    }

    private void setViewPager() {
        mViewPagerAdapter = new PlayViewPagerAdapter(getActivity(), mSongList);
        mViewPager2.setAdapter(mViewPagerAdapter);
        mViewPager2.setCurrentItem(getSongPosition(), false);
    }

    private void updateUI() {
        mTextViewSongTitle.setText(mSong.getTitle());
        mTextViewSongAlbum.setText(mSong.getAlbum());
        mTextViewTotalTime.setText(TimeFormatPlayer.elapsedTime(Integer.parseInt(mSong.getDuration())));
        mSeekBar.setMax(Integer.parseInt(mSong.getDuration()));
        setImagePalette();
        setFavorite();
    }

    private void setFavorite(){
        if (mSong.isFavorite())
            mImageViewFavorite.setImageResource(R.drawable.ic_favorite);
        else
            mImageViewFavorite.setImageResource(R.drawable.ic_favorite_border);
    }

    private void onConfiguration(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mMediaPlayer = mRepository.getMediaPlayer();
            mHandler = new Handler();
        } else {
            setMediaPlayer();
            mMediaPlayer.start();
        }
    }

    private void setSeekBar() {


        mRunnable = new Runnable() {
            @Override
            public void run() {

                if (mMediaPlayer != null) {
                    int currentPosition = mMediaPlayer.getCurrentPosition();
                    mSeekBar.setProgress(currentPosition);
                    mTextViewElapsedTime.setText(TimeFormatPlayer.elapsedTime(currentPosition));
                    setSeekBar();
                }
            }
        };
        mHandler.postDelayed(mRunnable, 500);
    }

    private void setListener() {

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mSong = mSongList.get(position);
                mUri = Uri.parse(mSong.getPath());
                mRepository.setCurrentSong(mSong);

                if (mMediaPlayer.isPlaying()) {
                    setMediaPlayer();
                    mMediaPlayer.start();
                }else
                    setMediaPlayer();

                updateUI();
                setInitializationUI();
            }
        });


        mImageViewPlayPause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying()){
                    mMediaPlayer.start();
                    mHandler.removeCallbacks(mRunnable);
                    setDrawableAnimation(mImageViewPlayPause, R.drawable.avd_play_to_pause);
                }else if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    setSeekBar();
                    setDrawableAnimation(mImageViewPlayPause, R.drawable.avd_pause_to_play);
                }
            }
        });

        mImageViewForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.getCurrentPosition() + 5000 < mMediaPlayer.getDuration())
                    mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + 5000);
                else
                    mMediaPlayer.seekTo(mMediaPlayer.getDuration());
                setSeekBar();
            }
        });

        mImageViewBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.getCurrentPosition() - 5000  > 0)
                    mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 5000);
                else
                    mMediaPlayer.seekTo(0);
                setSeekBar();
            }
        });

        mImageViewRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_ALL){
                    mRepository.setMusicRepeatMode(MusicRepeatMode.REPEAT_OFF);
                    mImageViewRepeat.setAlpha(0.5f);
                }else if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_OFF){
                    mImageViewRepeat.setAlpha(1f);
                    mRepository.setMusicRepeatMode(MusicRepeatMode.REPEAT_ONE);
                    setDrawableAnimation(mImageViewRepeat, R.drawable.avd_repeat_to_one);
                }else if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_ONE){
                    mRepository.setMusicRepeatMode(MusicRepeatMode.REPEAT_ALL);
                    setDrawableAnimation(mImageViewRepeat, R.drawable.avd_one_to_repeat);
                }
            }
        });

        mImageViewShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRepository.isShuffleOn()){
                    mImageViewShuffle.setAlpha(0.5f);
                    mRepository.setShuffleOn(false);
                }else
                {
                    mImageViewShuffle.setAlpha(1f);
                    mRepository.setShuffleOn(true);
                }
            }
        });

        mImageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSong.isFavorite()) {
                    mSong.setFavorite(false);

                    for (int i = 0; i <mRepository.getPlayLists().get(0).getSongList().size() ; i++) {
                        if (mRepository.getPlayLists().get(0).getSongList().get(i).getPath().equals(mSong.getPath())) {
                            mRepository.getPlayLists().get(0).getSongList().remove(i);
                            break;
                        }
                    }
                    setDrawableAnimation(mImageViewFavorite, R.drawable.avd_to_un_favorite);
                }
                else {
                    mSong.setFavorite(true);
                    mRepository.getPlayLists().get(0).getSongList().add(mSong);
                    setDrawableAnimation(mImageViewFavorite, R.drawable.avd_to_favorite);
                }
                mRepository.updateFavorite(mSong);

                /*if (mRepository.getCurrentListTitle().equals(mRepository.getPlayLists().get(0).getTitle())){
                    mRepository.setCurrentSongList(mRepository.getPlayLists().get(0).getSongList(), mRepository.getPlayLists().get(0).getTitle());
                    mSongList = mRepository.getCurrentSongList();
                    mViewPagerAdapter.setSongList(mSongList);
                }*/
            }
        });

        mImageViewAddToPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getActivity().getSupportFragmentManager().popBackStack(this.getClass().getName(), 0);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, PlayListScreenFragment.newInstance()).commit();

            }
        });
    }



    private int getRandomNumberForShuffle(){
        int number =  new Random().nextInt(mSongList.size());
        if (number == mViewPager2.getCurrentItem())
            getRandomNumberForShuffle();
        return number;
    }

    private void prepareNextMusic(){
        int currentPage = mViewPager2.getCurrentItem();

        if (!mRepository.isShuffleOn() && mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_ALL) {
            if (currentPage < mSongList.size() - 1)
                mViewPager2.setCurrentItem(++currentPage);
            else if (currentPage == mSongList.size() - 1) {
                currentPage = 0;
                mViewPager2.setCurrentItem(currentPage);
            }
        }else if (mRepository.isShuffleOn() && mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_ALL){
            currentPage = getRandomNumberForShuffle();
            mViewPager2.setCurrentItem(currentPage);
        }
        else if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_ONE){
            mViewPager2.setCurrentItem(currentPage);
        }
        else if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_OFF){
            mHandler.removeCallbacks(mRunnable);
           mMediaPlayer.seekTo(0);
            setInitializationUI();
            setSeekBar();
            return;
        }

        mSong = mSongList.get(currentPage);
        mUri = Uri.parse(mSong.getPath());
        mRepository.setCurrentSong(mSong);


        setMediaPlayer();
        mMediaPlayer.start();
        updateUI();
        setInitializationUI();
        setSeekBar();
    }

    private void setDrawableAnimation(ImageView imageView, int resId) {
        imageView.setImageDrawable(getResources().getDrawable(resId));
        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof AnimatedVectorDrawableCompat) {
            mAnimatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
            mAnimatedVectorDrawableCompat.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            mAnimatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            mAnimatedVectorDrawable.start();
        }
    }

    private void setInitializationUI(){

        mTextViewElapsedTime.setText(TimeFormatPlayer.elapsedTime(mMediaPlayer.getCurrentPosition()));
        mImageViewShuffle.setAlpha(mRepository.isShuffleOn() ? 1f : 0.5f);

        if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_ALL)
            mImageViewRepeat.setImageResource(R.drawable.ic_repeat);
        else if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_ONE)
            mImageViewRepeat.setImageResource(R.drawable.ic_repeat_one);
        else if (mRepository.getMusicRepeatMode() == MusicRepeatMode.REPEAT_OFF){
            mImageViewRepeat.setImageResource(R.drawable.ic_repeat);
            mImageViewRepeat.setAlpha(0.5f);
        }



        mMediaPlayer.setOnCompletionListener(this);

        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()){
            mImageViewPlayPause.setImageResource(R.drawable.ic_play);
            mHandler.removeCallbacks(mRunnable);
        }else if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mImageViewPlayPause.setImageResource(R.drawable.ic_pause);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mHandler.removeCallbacks(mRunnable);
    }

    private void setMediaPlayer() {
        mMediaPlayer = mRepository.getMediaPlayer();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            //mMediaPlayer.release();
        }
        mRepository.setMediaPlayer(getActivity(), mUri);
        mMediaPlayer = mRepository.getMediaPlayer();
    }

    private int getSongPosition() {
        for (int i = 0; i < mRepository.getCurrentSongList().size(); i++) {
            if (mSong.getPath().equals(mRepository.getCurrentSongList().get(i).getPath()))
                return i;
        }
        return -1;
    }

    public void setLayoutGradient(GradientDrawable gradient, Palette.Swatch swatch) {
        mConstraintLayoutRoot.setBackground(gradient);
        if (swatch != null) {
            mTextViewSongTitle.setTextColor(swatch.getTitleTextColor());
            mTextViewSongAlbum.setTextColor(swatch.getBodyTextColor());
        } else {
            mTextViewSongTitle.setTextColor(Color.WHITE);
            mTextViewSongAlbum.setTextColor(Color.WHITE);
        }
    }

    public void setDefaultPalette() {
        mConstraintLayoutRoot.setBackgroundResource(R.drawable.default_gradient);
        mTextViewSongTitle.setTextColor(Color.WHITE);
        mTextViewSongAlbum.setTextColor(Color.WHITE);
    }

    public void setImagePalette() {

        assert mSong != null;
        byte[] art = MusicManager.getCoverArt(mSong.getPath());

        final Bitmap bitmap;


        if (art != null) {
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);

            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {

                    Palette.Swatch swatch = palette.getDominantSwatch();
                    GradientDrawable gradientDrawable;
                    if (swatch != null)
                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), getResources().getColor(R.color.colorDownEnd)});
                    else
                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{getResources().getColor(R.color.colorDownEnd), getResources().getColor(R.color.colorDownStart)});
                    setLayoutGradient(gradientDrawable, swatch);
                }
            });
        } else
            setDefaultPalette();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        prepareNextMusic();
        mMediaPlayer.setOnCompletionListener(this);
    }
}