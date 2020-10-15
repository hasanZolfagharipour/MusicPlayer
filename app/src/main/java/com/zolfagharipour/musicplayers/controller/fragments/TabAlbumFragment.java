package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.CollectionsAdapter;
import com.zolfagharipour.musicplayers.adapter.TracksAdapter;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TabAlbumFragment extends Fragment implements TracksAdapter.TrackItemListener, CollectionsAdapter.CollectionListener {

    private RecyclerView mRecyclerViewAlbum, mRecyclerViewSongList;
    private CollectionsAdapter mCollectionsAdapter;
    private SongRepository mRepository;

    public static TabAlbumFragment newInstance() {

        Bundle args = new Bundle();
        TabAlbumFragment fragment = new TabAlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = SongRepository.getInstance();
        getAlbumList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_tab, container, false);

        findViews(view);
        setRecyclerView();
        setAlbumAdapter();

        return view;
    }

    private void findViews(View view) {
        mRecyclerViewAlbum = view.findViewById(R.id.collectionTabCollectionRecyclerView);
        mRecyclerViewSongList = view.findViewById(R.id.collectionTabSongListRecyclerView);
    }

    private void setRecyclerView() {
        mRecyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewSongList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setAlbumAdapter() {
        if (mCollectionsAdapter == null) {
            mCollectionsAdapter = new CollectionsAdapter(getActivity(), getAlbumList(), this);
            mRecyclerViewAlbum.setAdapter(mCollectionsAdapter);
        } else {
            mCollectionsAdapter.setPlayLists(getAlbumList());
            mCollectionsAdapter.notifyDataSetChanged();
        }
    }

    private List<PlayList> getAlbumList() {
        List<Song> list = mRepository.getSongList();
        List<PlayList> playLists = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAlbum().length() > 0) {
                List<Song> songs = new ArrayList<>();
                songs.add(list.get(i));
                playLists.add(new PlayList(list.get(i).getAlbum(), songs));
            }
        }

        for (int i = 0; i < playLists.size(); i++) {
            for (int j = 0; j < playLists.size(); j++) {
                if (playLists.get(i).getSongList().get(0).getAlbum().equals(playLists.get(j).getSongList().get(0).getAlbum()) && i != j)
                    playLists.remove(j--);
            }
        }

        return playLists;
    }

    @Override
    public void onTrackItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.activityFragmentContainer, PlayFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onCollectionClicked(PlayList playList) {

        List<Song> list = new ArrayList<>();

        for (int i = 0; i < mRepository.getSongList().size(); i++) {
            if (mRepository.getSongList().get(i).getAlbum().equals(playList.getSongList().get(0).getAlbum()))
                list.add(mRepository.getSongList().get(i));
        }

        mRepository.setCurrentSongList(list, "AlbumTab");

        TracksAdapter adapter = new TracksAdapter(getActivity(), list, this);
        mRecyclerViewSongList.setAdapter(adapter);
        mRecyclerViewSongList.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));
    }
}