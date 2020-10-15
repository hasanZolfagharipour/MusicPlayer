package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.Song;

import androidx.fragment.app.Fragment;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

public class SongCoverFragment extends Fragment {

    public static final String BUNDLE_SONG_INSTANCE = "BundleSongInstance";
    public ImageView mImageViewCover;
    private Song mSong;

    public static SongCoverFragment newInstance(Song song) {

        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SONG_INSTANCE, song);
        SongCoverFragment fragment = new SongCoverFragment();
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
        View view = inflater.inflate(R.layout.fragment_song_cover, container, false);

        findViews(view);
        setCover();

        return view;
    }

    private void findViews(View view) {
        mImageViewCover = view.findViewById(R.id.songCoverFragmentImageView);
    }

    private void setCover() {
        Glide.with(getActivity()).asBitmap().load(mSong.getImage()).placeholder(R.drawable.ic_cover_play).apply(RequestOptions.bitmapTransform(new RoundedCorners(64))).into(mImageViewCover);
    }

}