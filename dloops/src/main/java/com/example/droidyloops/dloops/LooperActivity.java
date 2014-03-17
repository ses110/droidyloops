package com.example.droidyloops.dloops;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LooperActivity extends Activity {

    GridView mGridView;
    SoundPool mPool;
    SoundPoolThread spThread;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_looper);
        mGridView = (GridView) findViewById(R.id.loop_view);
        mPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        //TODO: Make this customizable
        AssetFileDescriptor snare;
        AssetFileDescriptor kick;
        AssetFileDescriptor hat;
        AssetFileDescriptor clap;
        int sounds[] = new int[4];
        try {
            snare = getAssets().openFd("trap" + File.separator + "Snare.wav");
            kick = getAssets().openFd("trap" + File.separator + "HardKick.wav");
            hat = getAssets().openFd("trap" + File.separator + "Hat.wav");
            clap = getAssets().openFd("trap" + File.separator + "Clap.wav");

            sounds[0] = mPool.load(snare, 1);
            sounds[1] = mPool.load(kick, 1);
            sounds[2] = mPool.load(hat, 1);
            sounds[3] = mPool.load(clap, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        spThread = new SoundPoolThread(mPool, mGridView, sounds);
        spThread.start();
    }

    public void playStop(View view)
    {
        mGridView.playStop();
        spThread.toggleRunning();
    }

    class SoundPoolThread extends Thread {

        private final ArrayList<Square> squares;
        private SoundPool mSoundPool;
        private int[] sounds;
        private boolean _run = false;

        private long lastBeat;

        public SoundPoolThread(SoundPool mSoundPool, GridView gridView, int[] sounds)
        {
            this.mSoundPool = mSoundPool;
            this.squares = gridView.squares;
            this.sounds = sounds;
        }

        public void toggleRunning() {
            _run = !_run;
        }

        @Override
        public void run() {
            while (_run) {
                synchronized (mSoundPool) {


                }
            }
        }
    }
}
