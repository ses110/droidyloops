package com.example.droidyloops.dloops;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class LooperActivity extends Activity {

    private GridView mGridView;
    private SoundPoolThread spThread;
    private boolean play;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_looper);
        mGridView = (GridView) findViewById(R.id.loop_view);
        SoundPool mPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        //TODO: Make this customizable
        AssetFileDescriptor snare;
        AssetFileDescriptor kick;
        AssetFileDescriptor hat;
        AssetFileDescriptor clap;
        int sounds[] = new int[4];
        try {
            snare = getAssets().openFd("Snare.ogg");
            kick = getAssets().openFd("Kick.wav");
            hat = getAssets().openFd("Hat.ogg");
            clap = getAssets().openFd("Clap.ogg");

            sounds[0] = mPool.load(snare, 1);
            sounds[1] = mPool.load(kick, 1);
            sounds[2] = mPool.load(hat, 1);
            sounds[3] = mPool.load(clap, 1);
            for(int i : sounds)
            {
                Log.v("sound", Integer.toString(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        spThread=new SoundPoolThread(mPool, mGridView, sounds);

        spThread.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(play)
        {
            spThread.toggleRunning();
            mGridView.playStop();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        spThread.interrupt();
    }

    public void playStop(View view)
    {
        play = !play;
        mGridView.playStop();
        spThread.toggleRunning();
    }

    class SoundPoolThread extends Thread {

        private GridView mGridView;
        private SoundPool mSoundPool;
        private int[] sounds;
        private boolean play = false;
        private AudioManager mAudioManager;

        private long lastBeat;
        private int curCol = 0;
        private int beatTime = 500;

        public SoundPoolThread(SoundPool mSoundPool, GridView gridView, int[] sounds)
        {
            this.mSoundPool = mSoundPool;
            this.mGridView = gridView;
            this.sounds = sounds;
            lastBeat = System.currentTimeMillis() - beatTime;
            mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        }

        public void toggleRunning() {
            play = !play;
            curCol = 0;
            lastBeat = System.currentTimeMillis() - beatTime;
        }

        @Override
        public void run() {
            while (true) {
                if (play) {
                    synchronized (mGridView.squares) {

                        long curTime = System.currentTimeMillis();
                        if (curTime - lastBeat >= beatTime) {
                            // Get the volume
                            float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                            streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                            curCol++;
                            if (curCol == 9)
                                curCol = 1;
                            for (Square sq : mGridView.squares) {
                                if (sq.col == curCol) {
                                    mSoundPool.play(sounds[sq.row - 1], streamVolume, streamVolume, 1, 0, 1);
                                }
                            }
                            lastBeat = System.currentTimeMillis();
                        }

                    }
                }
            }
        }
    }
}
