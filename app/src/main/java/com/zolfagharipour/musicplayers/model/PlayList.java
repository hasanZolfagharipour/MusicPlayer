package com.zolfagharipour.musicplayers.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class PlayList implements Serializable {

    private String mTitle;
    private UUID mPlayListId;
    private List<Song> mSongList;

    public PlayList() {
        mPlayListId = UUID.randomUUID();
    }

    public PlayList(String title) {
        this();
        mTitle = title;
    }

    public PlayList(String title, List<Song> songList) {
        this();
        mTitle = title;
        mSongList = songList;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getPlayListId() {
        return mPlayListId;
    }

    public List<Song> getSongList() {
        return mSongList;
    }

    public void setSongList(List<Song> songList) {
        mSongList = songList;
    }
}
