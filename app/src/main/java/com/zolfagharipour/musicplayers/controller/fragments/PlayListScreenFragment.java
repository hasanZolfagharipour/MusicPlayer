package com.zolfagharipour.musicplayers.controller.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.adapter.PlayListScreenRecyclerViewAdapter;
import com.zolfagharipour.musicplayers.controller.activity.MusicPlayersActivity;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class PlayListScreenFragment extends Fragment implements PlayListScreenRecyclerViewAdapter.OnPlayListScreenListener {

    public static final String TAG = "tag";
    private RecyclerView mRecyclerView;
    private PlayListScreenRecyclerViewAdapter mAdapter;
    private Toolbar mToolbar;
    private MusicRepository mRepository;

    public static PlayListScreenFragment newInstance() {

        Bundle args = new Bundle();

        PlayListScreenFragment fragment = new PlayListScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = MusicRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_list_screen, container, false);

        findViews(view);
        setToolbar();
        setAdapter();
        return view;
    }

    private void findViews(View view) {
        mToolbar = view.findViewById(R.id.playListScreenToolbar);
        mRecyclerView = view.findViewById(R.id.playListScreenRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setToolbar() {
        ((MusicPlayersActivity) getActivity()).setSupportActionBar(mToolbar);
        ((MusicPlayersActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new PlayListScreenRecyclerViewAdapter(getActivity(), mRepository.getPlayLists(), this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPlayLists(mRepository.getPlayLists());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelectedListener(int position, PlayList playList) {
        if (position == 0) {

            final EditText editText = new EditText(getActivity());
            editText.setSingleLine();
            editText.setHint(R.string.playlist_name);

            SweetAlertDialog sweetAlertDialognew = new SweetAlertDialog(Objects.requireNonNull(getActivity()));
            sweetAlertDialognew
                    .setTitleText(getString(R.string.create_playlist))
                    .setCustomView(editText)
                    .setCancelButton(android.R.string.cancel, null)
                    .setConfirmButton(getString(R.string.create), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            for (int i = 0; i < mRepository.getPlayLists().size(); i++) {
                                if (mRepository.getPlayLists().get(i).getTitle().equalsIgnoreCase(editText.getText().toString())) {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                            .setContentText(editText.getText().toString() + getString(R.string.already_exist))
                                            .setConfirmButton(android.R.string.ok, null)
                                            .show();
                                    return;
                                }
                            }
                                    List<Song> list = new ArrayList<>();
                                    list.add(mRepository.getCurrentSong());
                                    mRepository.getPlayLists().add(new PlayList(editText.getText().toString(), list));
                                    sweetAlertDialog.dismissWithAnimation();
                                    if (getActivity() != null) {
                                        getActivity().onBackPressed();
                                    }

                        }
                    })
                    .show();


        } else {
            for (int i = 0; i < playList.getSongList().size(); i++) {
                if (playList.getSongList().get(i).getPath().equals(mRepository.getCurrentSong().getPath())) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                    return;
                }
            }
            mRepository.getPlayLists().get(--position).getSongList().add(mRepository.getCurrentSong());
            if (position == 0) {
                mRepository.getCurrentSong().setFavorite(true);
                mRepository.updateFavorite(mRepository.getCurrentSong());
            }
            if (getActivity() != null)
                getActivity().onBackPressed();
        }
    }
}