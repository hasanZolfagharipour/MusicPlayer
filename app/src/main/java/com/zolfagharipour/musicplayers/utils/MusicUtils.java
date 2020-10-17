package com.zolfagharipour.musicplayers.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.zolfagharipour.musicplayers.model.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class MusicUtils {

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

    public static String elapsedTime(int currentPosition){
        currentPosition /= 1000;
        String totalOld = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOld = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1)
            return totalNew;
        else
            return totalOld;
    }

    public static int getRandomNumberForShuffle(int currentPosition, int limitation) {
        int number = new Random().nextInt();
        if (number == currentPosition)
            getRandomNumberForShuffle(currentPosition ,limitation);
        return number;
    }

    public static  void setDrawableAnimation(Context context, ImageView imageView, int resId) {

        AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
        AnimatedVectorDrawable       animatedVectorDrawable;

        imageView.setImageDrawable(context.getResources().getDrawable(resId));
        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof AnimatedVectorDrawableCompat) {
            animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
            animatedVectorDrawableCompat.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            animatedVectorDrawable.start();
        }
    }
}
