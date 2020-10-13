package com.hackathon.lampyridaeclient;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class PlaySound extends Service {
    final boolean[] isLoop = new boolean[1];
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        isLoop[0] = true;

        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.siren);
        }

        mediaPlayer.start();

        new CountDownTimer(((Lampyridae)getApplication()).getSoundTimer() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                mediaPlayer = null;

                stopSelf();
            }
        }.start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
