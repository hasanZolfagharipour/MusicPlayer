package com.zolfagharipour.musicplayers.utils;

public class TimeFormatPlayer {

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

}
