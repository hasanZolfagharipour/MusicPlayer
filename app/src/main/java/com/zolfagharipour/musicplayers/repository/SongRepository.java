package com.zolfagharipour.musicplayers.repository;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.zolfagharipour.musicplayers.enums.PlayRepeatMode;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongRepository {

    private static SongRepository sRepository;
    private List<Song> mSongList;
    private Song mCurrentSong;
    private List<Song> mCurrentSongList;

    private List<PlayList> mPlayLists;
    private String mCurrentListTitle;

    private PlayRepeatMode mPlayRepeatMode = PlayRepeatMode.REPEAT_ALL;
    private boolean mIsShuffleOn;
    private MediaPlayer mMediaPlayer;

    private SongRepository() {
        mPlayLists = new ArrayList<>();
        mPlayLists.add(new PlayList("Favorite", new ArrayList<Song>()));
    }

    public static SongRepository getInstance() {
        if (sRepository == null)
            sRepository = new SongRepository();
        return sRepository;
    }

    public PlayRepeatMode getPlayRepeatMode() {
        return mPlayRepeatMode;
    }

    public void setPlayRepeatMode(PlayRepeatMode playRepeatMode) {
        mPlayRepeatMode = playRepeatMode;
    }

    public String getCurrentListTitle() {
        return mCurrentListTitle;
    }

    public List<Song> getSongList() {
        return mSongList;
    }

    public void setSongList(List<Song> songList) {
        mSongList = songList;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaPlayer(Context context, Uri uri) {
        mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), uri);
    }

    public Song getCurrentSong() {
        return mCurrentSong;
    }

    public void setCurrentSong(Song currentSong) {
        mCurrentSong = currentSong;
    }

    public boolean isShuffleOn() {
        return mIsShuffleOn;
    }

    public void setShuffleOn(boolean shuffleOn) {
        mIsShuffleOn = shuffleOn;
    }

    public List<Song> getCurrentSongList() {
        return mCurrentSongList;
    }

    public void setCurrentSongList(List<Song> currentSongList, String currentListTitle) {
        mCurrentSongList = currentSongList;
        mCurrentListTitle = currentListTitle;
    }

    public List<PlayList> getPlayLists() {
        return mPlayLists;
    }

    public void updateFavorite(Song song) {
        for (int i = 0; i < mSongList.size(); i++) {
            if (mSongList.get(i).getPath().equals(song.getPath())) {
                mSongList.get(i).setFavorite(song.isFavorite());
                break;
            }
        }
    }
}
