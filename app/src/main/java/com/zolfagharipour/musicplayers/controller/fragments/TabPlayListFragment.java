package com.zolfagharipour.musicplayers.controller.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.CollectionsAdapter;
import com.zolfagharipour.musicplayers.adapter.TracksAdapter;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TabPlayListFragment extends Fragment implements CollectionsAdapter.CollectionListener, TracksAdapter.TrackItemListener {

    private SongRepository mRepository;
    private List<PlayList> mPlayLists;
    private RecyclerView mRecyclerViewCollection, mRecyclerViewSongList;
    private CollectionsAdapter mCollectionsAdapter;
    private TracksAdapter mAdapterSongList;

    public static TabPlayListFragment newInstance() {

        Bundle args = new Bundle();
        TabPlayListFragment fragment = new TabPlayListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = SongRepository.getInstance();
        mPlayLists = mRepository.getPlayLists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_tab, container, false);

        findViews(view);

        setPlayListAdapter();

        return view;
    }

    private void findViews(View view) {
        mRecyclerViewCollection = view.findViewById(R.id.collectionTabCollectionRecyclerView);
        mRecyclerViewSongList = view.findViewById(R.id.collectionTabSongListRecyclerView);
        mRecyclerViewCollection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewSongList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setPlayListAdapter() {
        if (mCollectionsAdapter == null) {
            mCollectionsAdapter = new CollectionsAdapter(getActivity(), mPlayLists, this);
            mRecyclerViewCollection.setAdapter(mCollectionsAdapter);
        } else {
            mCollectionsAdapter.setPlayLists(mPlayLists);
            mCollectionsAdapter.notifyDataSetChanged();

            if (mAdapterSongList != null)
                setUpdate();
        }
    }

    private void setUpdate() {

        for (int i = 0; i < mRepository.getPlayLists().size(); i++) {
            if (mRepository.getCurrentListTitle().equals(mRepository.getPlayLists().get(i).getTitle())) {
                List<Song> list = new ArrayList<>();
                list.addAll(mRepository.getPlayLists().get(i).getSongList());
                mRepository.setCurrentSongList(list, mRepository.getCurrentListTitle());
                mAdapterSongList.setSongList(list);
                mAdapterSongList.notifyDataSetChanged();
                break;
            }
        }

    }

    @SuppressLint("CheckResult")
    @Override
    public void onCollectionClicked(PlayList playList) {
        if (playList.getSongList().size() == 0) {
            Toast.makeText(getActivity(), R.string.no_tracks, Toast.LENGTH_SHORT).show();
            return;
        }

        List<Song> list = new ArrayList<>();
        list.addAll(playList.getSongList());
        mRepository.setCurrentSongList(list, playList.getTitle());

        mAdapterSongList = new TracksAdapter(getActivity(), list, this);
        mRecyclerViewSongList.setAdapter(mAdapterSongList);
        mRecyclerViewSongList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onTrackItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activityFragmentContainer, PlayFragment.newInstance()).addToBackStack(null).commit();

    }
}