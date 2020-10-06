package com.zolfagharipour.musicplayers.repository;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.zolfagharipour.musicplayers.model.Song;

import java.util.List;

public class MusicRepository {
    private static final String TAG = "tag";
    private static MusicRepository sRepository;
    private List<Song> mSongList;
    private Song mCurrentSong;

    private MediaPlayer mMediaPlayer;
    private MusicRepository() {
    }

    public static MusicRepository getInstance() {
        if (sRepository == null)
            sRepository = new MusicRepository();
        return sRepository;
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
}
