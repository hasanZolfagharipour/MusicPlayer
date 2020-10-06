package com.zolfagharipour.musicplayers.controller.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

public class MusicCoverFragment extends Fragment {

    public static final String TAG = "tag";
    public static final String BUNDLE_SONG_INSTANCE = "BundleSongInstance";
    public ImageView mImageViewCover;
    private Song mSong;

    public static MusicCoverFragment newInstance(Song song) {

        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SONG_INSTANCE, song);
        MusicCoverFragment fragment = new MusicCoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSong = (Song) getArguments().getSerializable(BUNDLE_SONG_INSTANCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_cover, container, false);

        findViews(view);
        setCover();

        return view;
    }

    private void findViews(View view) {
        mImageViewCover = view.findViewById(R.id.musicPlayFragmentCover);
    }

    private void setCover() {
        byte[] image = MusicManager.getCoverArt(mSong.getPath());
        Glide.with(getActivity()).asBitmap().load(image).placeholder(R.drawable.ic_cover_play).apply(RequestOptions.bitmapTransform(new RoundedCorners(64))).into(mImageViewCover);
    }
}