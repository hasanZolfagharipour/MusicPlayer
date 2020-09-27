package com.zolfagharipour.musicplayers.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import com.zolfagharipour.musicplayers.model.Song;
import java.util.ArrayList;
import java.util.List;

public class MusicManager {

    public static final String TAG = "tag";

    public static List<Song> getSongList(Context context) {

        List<Song> songList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // todo DATA is Depreciated.

        String[] projection = {MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                songList.add(new Song(cursor.getString(3), cursor.getString(1), cursor.getString(0), cursor.getString(4), cursor.getString(2)));
            }
            cursor.close();
        }

        return songList;
    }

    public static byte[] getCoverArt(String uri) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }




}
