package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.TracksAdapter;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.SongRepository;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TabTrackFragment extends Fragment implements TracksAdapter.TrackItemListener {


    private RecyclerView mRecyclerView;
    private TracksAdapter mAdapter;
    private SongRepository mRepository;
    private List<Song> mSongList;

    public static TabTrackFragment newInstance() {

        Bundle args = new Bundle();
        TabTrackFragment fragment = new TabTrackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = SongRepository.getInstance();
        mSongList = mRepository.getSongList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_tab, container, false);

        findViews(view);
        setRecyclerView();


        setAdapter();

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.trackTabRecyclerview);
    }

    private void setRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new TracksAdapter(getActivity(), mSongList,this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mSongList = mRepository.getSongList();
            mAdapter.setSongList(mSongList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTrackItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        mRepository.setCurrentSongList(mRepository.getSongList(), getString(R.string.song_list_title_tab_track));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activityFragmentContainer, PlayFragment.newInstance()).addToBackStack(null).commit();
    }
}