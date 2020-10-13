package com.zolfagharipour.musicplayers.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.PlayListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;

import java.util.ArrayList;
import java.util.List;

public class PlayListTabFragment extends Fragment  implements PlayListRecyclerViewAdapter.OnCollectionListener {

    public static final String TAG= "tag";

    public static PlayListTabFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PlayListTabFragment fragment = new PlayListTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MusicRepository mRepository;
    private List<PlayList> mPlayLists;
    private RecyclerView mRecyclerView;
    private PlayListRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = MusicRepository.getInstance();
        mPlayLists = mRepository.getPlayLists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_tab, container, false);

        findViews(view);

        setAdapter();

        return view;
    }

    private void findViews(View view){
        mRecyclerView = view.findViewById(R.id.albumRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }
    public  void setAdapter(){
        if (mAdapter == null){
            mAdapter = new PlayListRecyclerViewAdapter(getActivity(), mPlayLists, this);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setPlayLists(mPlayLists);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCollectionClicked(PlayList playList) {
        if (playList.getSongList().size() == 0) {
            Toast.makeText(getActivity(), R.string.no_tracks, Toast.LENGTH_SHORT).show();
            return;
        }

        List<Song> list = new ArrayList<>();
        list.addAll(playList.getSongList());
        mRepository.setCurrentSongList(list, playList.getTitle());
        
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, CollectionsTrackFragment.newInstance())
                .commit();

    }
}