package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.MusicListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TrackTabFragment extends Fragment implements MusicListRecyclerViewAdapter.MusicItemListener, TabFragment.OnUpdateMusicListListener {


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

        if (mAdapter == null) {
            mAdapter = new MusicListRecyclerViewAdapter(mSongList, getActivity(), this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSongList(mSongList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMusicItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, MusicPlayFragment.newInstance(song)).addToBackStack(null).commit();
    }

    @Override
    public void onUpdateRecyclerView(){
        setUI();
    }
}