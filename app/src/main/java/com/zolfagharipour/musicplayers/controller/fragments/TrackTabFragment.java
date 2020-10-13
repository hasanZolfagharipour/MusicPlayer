package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.MusicListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TrackTabFragment extends Fragment implements MusicListRecyclerViewAdapter.MusicItemListener {


    public static final String TAG = "tag";
    private RecyclerView mRecyclerView;
    private MusicListRecyclerViewAdapter mAdapter;
    private MusicRepository mRepository;
    private List<Song> mSongList;

    public static TrackTabFragment newInstance() {

        Bundle args = new Bundle();

        TrackTabFragment fragment = new TrackTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = MusicRepository.getInstance();
        mSongList = mRepository.getSongList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_tab, container, false);

        findViews(view);
        setRecyclerView();
        setUI();

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
    }

    private void setRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
    }

    public void setUI() {
        Log.d(TAG, "setUI:1 ");
        if (mAdapter == null) {
            Log.d(TAG, "setUI:2 ");
            mAdapter = new MusicListRecyclerViewAdapter(mSongList, getActivity(), this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "setUI:3 ");
            mSongList = mRepository.getSongList();
            mAdapter.setSongList(mSongList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMusicItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        mRepository.setCurrentSongList(mRepository.getSongList(), "TrackTab");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, PlayFragment.newInstance(song)).addToBackStack(null).commit();
    }
}