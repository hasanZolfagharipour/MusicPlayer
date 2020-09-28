package com.zolfagharipour.musicplayers.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.MusicListRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import java.util.List;

public class MusicListFragment extends Fragment implements MusicListRecyclerViewAdapter.MusicItemListener {


    public static MusicListFragment newInstance() {

        Bundle args = new Bundle();

        MusicListFragment fragment = new MusicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private MusicListRecyclerViewAdapter mAdapter;
    private MusicRepository mRepository;
    private List<Song> mSongList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = MusicRepository.getInstance();
        mSongList = mRepository.getSongList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_music_list, container, false);

        findViews(view);
        setUI();


        return view;
    }

    private void findViews(View view){
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
    }

    private void setUI() {

        if (mAdapter == null) {
            mAdapter = new MusicListRecyclerViewAdapter(MusicManager.getSongList(getActivity().getApplicationContext()), getActivity(), this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSongList(MusicManager.getSongList(getActivity().getApplicationContext()));
            mAdapter.notifyDataSetChanged();
        }

    }


        @Override
        public void MusicClicked (Song song){

        /*mRepository.setCurrentSong(song);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(),
                        )
        startActivity(SongPlayActivity.newIntent(getActivity(),song),activityOptionsCompat.toBundle());*/

        }
        
}