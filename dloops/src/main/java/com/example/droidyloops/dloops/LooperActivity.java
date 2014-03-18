package com.example.droidyloops.dloops;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import java.io.IOException;

public class LooperActivity extends Activity {

    private GridView mGridView;
    private SoundPool mPool;
    private SoundPoolThread spThread;
    private boolean play;
    private boolean quit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_looper);
        mGridView = (GridView) findViewById(R.id.loop_view);
        mPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        //TODO: Make this customizable
        AssetFileDescriptor snare;
        AssetFileDescriptor kick;
        AssetFileDescriptor hat;
        AssetFileDescriptor clap;
        int sounds[] = new int[4];
        try {
            snare = getAssets().openFd("Snare.ogg");
            kick = getAssets().openFd("Kick.wav");
            hat = getAssets().openFd("Hat.wav");
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
        mPool.release();
        quit = true;
    }

    public void playStop(View view)
    {
        play = !play;
        mGridView.playStop();
        spThread.toggleRunning();
    }

    public void playSound(int row)
    {
        if(!play)
        {
            spThread.previews[row - 1] = true;
        }
    }

    class SoundPoolThread extends Thread {

        private GridView mGridView;
        private SoundPool mSoundPool;
        private int[] sounds;
        private boolean play = false;
        private AudioManager mAudioManager;
        private float maxVolume;
        public boolean[] previews = new boolean[4];

        private long lastBeat;
        private int curCol = 0;
        private int beatTime = 250;

        public SoundPoolThread(SoundPool mSoundPool, GridView gridView, int[] sounds)
        {
            this.mSoundPool = mSoundPool;
            this.mGridView = gridView;
            this.sounds = sounds;
            lastBeat = System.currentTimeMillis() - beatTime;
            mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }

        public void toggleRunning() {
            play = !play;
            curCol = 0;
            lastBeat = System.currentTimeMillis() - beatTime;
        }

        @Override
        public void run() {
            while (true) {
                if(quit)
                    break;
                // Get the volume
                float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                streamVolume = streamVolume / maxVolume;
                if (play) {
                    synchronized (mGridView.squares) {

                        long curTime = System.currentTimeMillis();
                        if (curTime - lastBeat >= beatTime) {

                            curCol++;
                            if (curCol == 9)
                                curCol = 1;
                            for (Square sq : mGridView.squares) {
                                if (sq.col == curCol) {
                                    mSoundPool.play(sounds[sq.row - 1], streamVolume, streamVolume, 1, 0, 1);
                                }
                            }
                            lastBeat = curTime;
                            mGridView.incrementHL();
                        }

                    }
                }
                else // preview sounds as you tap
                {
                    for(int i = 0; i < 4; i++)
                    {
                        if(previews[i]) {
                            mSoundPool.play(i + 1, streamVolume, streamVolume, 1, 0, 1);
                            previews[i] = false;
                        }
                    }
                }
            }
        }
    }
}
