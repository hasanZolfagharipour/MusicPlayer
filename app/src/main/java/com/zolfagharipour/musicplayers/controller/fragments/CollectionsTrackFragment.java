package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.MusicListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.controller.activity.MusicPlayersActivity;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionsTrackFragment extends Fragment implements MusicListRecyclerViewAdapter.MusicItemListener {

    private static final String TAG = "tag";
    CollapsingToolbarLayout mCollapsingToolbar;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ImageView mImageViewToolbar;
    private TextView mTextViewEmpty;
    private MusicListRecyclerViewAdapter mAdapter;
    private MusicRepository mRepository;
    private List<Song> mSongList;

    public static CollectionsTrackFragment newInstance() {

        Bundle args = new Bundle();
        CollectionsTrackFragment fragment = new CollectionsTrackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new Explode());
        mRepository = MusicRepository.getInstance();
        mSongList = mRepository.getCurrentSongList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections_track, container, false);
        findViews(view);
        setToolbar();
        setUpdate();
        setAdapter();
        return view;
    }

    private void findViews(View view) {
        mToolbar = view.findViewById(R.id.albumsTrackToolbar);
        mCollapsingToolbar = view.findViewById(R.id.collapseToolbarAlbumsTrack);
        mRecyclerView = view.findViewById(R.id.albumsTrackRecyclerView);
        mTextViewEmpty = view.findViewById(R.id.albumsTrackEmpty);
        mImageViewToolbar = view.findViewById(R.id.albumsTrackToolbarImageView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setToolbar() {
        if (mSongList.size() == 0) {
            setEmptySong();
            return;
        }
        Song song = mSongList.get(0);
        ((MusicPlayersActivity) getActivity()).setSupportActionBar(mToolbar);
        mCollapsingToolbar.setTitle(song.getTitle());
        
        Glide.with(getActivity()).asBitmap().load(MusicManager.getCoverArt(song.getPath())).placeholder(R.drawable.ic_cover_list).into(mImageViewToolbar);
    }

    private void setEmptySong(){
        Glide.with(getActivity()).load(0).placeholder(R.drawable.ic_cover_list).into(mImageViewToolbar);
        mCollapsingToolbar.setTitle("");
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void setAdapter() {
            mAdapter = new MusicListRecyclerViewAdapter(mSongList, getActivity(), this);
            mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpdate(){

        for (int i = 0; i < mRepository.getPlayLists().size(); i++) {
            if (mRepository.getCurrentListTitle().equals(mRepository.getPlayLists().get(i).getTitle())){
                List<Song> list = new ArrayList<>();
                list.addAll(mRepository.getPlayLists().get(i).getSongList());
                mRepository.setCurrentSongList(list, mRepository.getCurrentListTitle());
                mSongList = list;
                setToolbar();
                break;
            }
        }

    }

    @Override
    public void onMusicItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, PlayFragment.newInstance(song)).addToBackStack(null).commit();
    }

}