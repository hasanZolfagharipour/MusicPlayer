package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.MusicListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.adapter.PlayListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;

import java.util.ArrayList;
import java.util.List;

public class PlayListTabFragment extends Fragment  implements PlayListRecyclerViewAdapter.OnCollectionListener, MusicListRecyclerViewAdapter.MusicItemListener {

    public static final String TAG= "tag";

    public static PlayListTabFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PlayListTabFragment fragment = new PlayListTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MusicRepository mRepository;
    private List<PlayList> mPlayLists;
    private RecyclerView mRecyclerViewCollection, mRecyclerViewSongList;
    private PlayListRecyclerViewAdapter mAdapterCollection;
    private MusicListRecyclerViewAdapter mAdapterSongList;

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
        mRecyclerViewCollection = view.findViewById(R.id.albumTabCollectionRecyclerView);
        mRecyclerViewSongList = view.findViewById(R.id.albumTabSongListRecyclerView);
        mRecyclerViewCollection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewSongList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    public  void setAdapter(){
        if (mAdapterCollection == null){
            mAdapterCollection = new PlayListRecyclerViewAdapter(getActivity(), mPlayLists, this);
            mRecyclerViewCollection.setAdapter(mAdapterCollection);
        }else {
            mAdapterCollection.setPlayLists(mPlayLists);
            mAdapterCollection.notifyDataSetChanged();
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

        mAdapterSongList = new MusicListRecyclerViewAdapter(list, getActivity(), this);
        mRecyclerViewSongList.setAdapter(mAdapterSongList);
        mRecyclerViewSongList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


       /* getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, CollectionsTrackFragment.newInstance())
                .commit();*/

    }

    @Override
    public void onMusicItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, PlayFragment.newInstance(song)).addToBackStack(null).commit();

    }
}