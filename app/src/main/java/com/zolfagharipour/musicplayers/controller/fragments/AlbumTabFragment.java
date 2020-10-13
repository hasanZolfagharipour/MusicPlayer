package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.AlbumTabRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.adapter.MusicListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;

import java.util.ArrayList;
import java.util.List;


public class AlbumTabFragment extends Fragment implements AlbumTabRecyclerViewAdapter.OnAlbumListener, MusicListRecyclerViewAdapter.MusicItemListener {

    public static AlbumTabFragment newInstance() {

        Bundle args = new Bundle();

        AlbumTabFragment fragment = new AlbumTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerViewCollection, mRecyclerViewSongList;
    private AlbumTabRecyclerViewAdapter mAdapterCollection;
    private MusicListRecyclerViewAdapter mAdapterSongList;
    private MusicRepository mRepository;
    private List<Song> mSongList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = MusicRepository.getInstance();

        getAlbumList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_album_tab, container, false);

        findViews(view);
        setRecyclerView();
        setAdapter();

        return view;
    }

    private void findViews(View view){
        mRecyclerViewCollection = view.findViewById(R.id.albumTabCollectionRecyclerView);
        mRecyclerViewSongList = view.findViewById(R.id.albumTabSongListRecyclerView);
    }

    private void setRecyclerView(){
        mRecyclerViewCollection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewSongList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setAdapter(){
        if (mAdapterCollection == null){
            mAdapterCollection = new AlbumTabRecyclerViewAdapter(getActivity(), mSongList, this );
            mRecyclerViewCollection.setAdapter(mAdapterCollection);
        }else{
            mAdapterCollection.setList(mSongList);
            mAdapterCollection.notifyDataSetChanged();
        }


    }

    private void getAlbumList(){
        List<Song> list = mRepository.getSongList();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAlbum().length() > 0)
                mSongList.add(list.get(i));
        }

        for (int i = 0; i < mSongList.size(); i++) {
            for (int j = 0; j < mSongList.size(); j++) {
                if (mSongList.get(i).getAlbum().equals(mSongList.get(j).getAlbum()) && i != j)
                    mSongList.remove(j--);
            }
        }
    }

    @Override
    public void onAlbumClickedListener(Song song) {

        setSongList(song);
        mAdapterSongList = new MusicListRecyclerViewAdapter(mRepository.getCurrentSongList(), getActivity(), this);
        mRecyclerViewSongList.setAdapter(mAdapterSongList);
        mRecyclerViewSongList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

      /*  getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, CollectionsTrackFragment.newInstance())
                .commit();*/
    }

    private void setSongList(Song song) {

        List<Song> list = new ArrayList<>();

        for (int i = 0; i < mRepository.getSongList().size(); i++) {
            if (mRepository.getSongList().get(i).getAlbum().equals(song.getAlbum()))
                list.add(mRepository.getSongList().get(i));
        }

        mRepository.setCurrentSongList(list, "AlbumTab");
    }


    @Override
    public void onMusicItemClicked(Song song) {
        mRepository.setCurrentSong(song);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, PlayFragment.newInstance(song)).addToBackStack(null).commit();

    }
}