package com.alpay.codenotes.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.alpay.codenotes.R;

import java.util.HashMap;

public class PlaySound {
    public static final int SUCCESS = R.raw.success;
    public static final int CLICK = R.raw.click;

    private static SoundPool soundPool;
    private static HashMap soundPoolMap;

    /** Populate the SoundPool*/
    public static void initSounds(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap(26);
        soundPoolMap.put(SUCCESS, soundPool.load(context, R.raw.success, 1));
        soundPoolMap.put(CLICK, soundPool.load(context, R.raw.click, 2));

    }

    public static void playSound(Context context, int soundID){
        MediaPlayer mp = MediaPlayer.create(context, soundID);
        mp.start();
    }
}
