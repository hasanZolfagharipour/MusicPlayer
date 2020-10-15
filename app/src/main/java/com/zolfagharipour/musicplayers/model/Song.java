package com.zolfagharipour.musicplayers.model;

import com.zolfagharipour.musicplayers.utils.MusicUtils;

import java.io.Serializable;

public class Song implements Serializable {

    private String mPath, mTitle, mAlbum, mArtist, mDuration;
    private byte[] mImage;
    private boolean favorite;


    public Song() {
    }

    public Song(String path, String title, String album, String artist, String duration) {
        mPath = path;
        mTitle = title;
        mAlbum = album;
        mArtist = artist;
        mDuration = duration;
        mImage = MusicUtils.getCoverArt(path);
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public byte[] getImage() {
        return mImage;
    }
}
