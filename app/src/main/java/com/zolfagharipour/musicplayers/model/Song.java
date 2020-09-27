package com.zolfagharipour.musicplayers.model;

import java.io.Serializable;

public class Song implements Serializable {

    private String mPath, mTitle, mAlbum, mArtist, mDuration;


    public Song() {
    }

    public Song(String path, String title, String album, String artist, String duration) {
        mPath = path;
        mTitle = title;
        mAlbum = album;
        mArtist = artist;
        mDuration = duration;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String album) {
        mAlbum = album;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

}
