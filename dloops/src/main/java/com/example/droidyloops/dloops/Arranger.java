package com.example.droidyloops.dloops;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.ArrayList;

/*
* Arranger will simply serve as a container for the TrackView class, which is a class that extends
* SurfaceView; it handles the graphical side of drawing of track rectangles.
* Arranger also creates a Thread subclass to handle playing the sounds and updating
* */

public class Arranger extends ActionBarActivity {

    private static TrackView mTrackView;
    private SoundPool mSndPool;
    private playThread mSpThread;
    private boolean quit;
    private static ArrayList<Pair<Integer, boolean[][]>> trackQueue;
    private static boolean queueUpdate;
    private boolean play;

    private int beatTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrange);

        if(mTrackView == null) {
            mTrackView = new TrackView(this);
        }

        if(trackQueue == null)
            trackQueue = new ArrayList<Pair<Integer, boolean[][]>>();

        // Remove the status bar, title bar and make the application fullscreen
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        FrameLayout frmLayout = (FrameLayout)findViewById(R.id.frmlayout);

        frmLayout.setFocusable(true);
        frmLayout.addView(mTrackView);

        mSndPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSpThread = new playThread(mSndPool, mTrackView);
        mSpThread.setPlay(true);
        mSpThread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.arranger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        super.onPause();
        if(play) {
            mSpThread.setPlay(false);
            this.play = false;
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.quit = true;
        mSpThread.interrupt();
        mSndPool.release();

    }

    @Override
    public void onResume(){
        super.onResume();

        //Update the tracks!
        if(queueUpdate)
        {
            Log.v("queueUpdate time!", Integer.toString(trackQueue.size()));
            for (Pair<Integer, boolean[][]> pair : trackQueue)
            {
                mTrackView.addTrack(pair.first - 1, pair.second);
            }
            trackQueue.clear();
            queueUpdate = false;
        }
        else
        {
            Log.v("queueUpdate", "is false");
        }
    }

    // Create a new sound
    public void newSound(int instrument) {
        Intent newTrack = new Intent(this, LooperActivity.class);
        newTrack.putExtra("instrument", instrument);
        startActivityForResult(newTrack, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                int instrument = data.getIntExtra("instrument", 1);
                String grid = data.getStringExtra("grid");
                beatTime = data.getIntExtra("beatTime", 500);
                if(grid != null)
                {
                    Log.v("Got Grid", grid);
                    boolean[][] curGrid = new boolean[8][4];
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (grid.charAt(i * 4 + j) == '1')
                                curGrid[i][j] = true;
                        }
                    }
                    this.addTrack(instrument, curGrid);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void addTrack(int instrument, boolean[][] curGrid)
    {
        Pair<Integer, boolean[][]> temp = new Pair<Integer, boolean[][]>(instrument, curGrid);
        trackQueue.add(temp);
        queueUpdate = true;
        Log.v("queueUpdate", "is true!");
    }

    class playThread extends Thread {
        private TrackView mTrackView;
        private SoundPool mSp;
        private AudioManager mAudioManager;
        private float mMaxVolume;
        private float mCurrentVolume;
        private boolean mPlay;

        private int sounds[][];

        public playThread(SoundPool sp, TrackView tv) {
            this.mSp = sp;
            this.mTrackView = tv;
            this.mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            this.mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            sounds = new int[4][4];

            //Load SoundPool files
            AssetFileDescriptor[][] soundsFD = new AssetFileDescriptor[4][4];
            try {
                soundsFD[0][0] = getAssets().openFd("Snare.ogg");
                soundsFD[0][1] = getAssets().openFd("Kick.wav");
                soundsFD[0][2] = getAssets().openFd("Hat.wav");
                soundsFD[0][3] = getAssets().openFd("Clap.wav");

                soundsFD[1][0] = getAssets().openFd("bass1.wav");
                soundsFD[1][1] = getAssets().openFd("bass2.wav");
                soundsFD[1][2] = getAssets().openFd("bass3.wav");
                soundsFD[1][3] = getAssets().openFd("bass4.wav");

                soundsFD[2][0] = getAssets().openFd("guitar1.wav");
                soundsFD[2][1] = getAssets().openFd("guitar2.wav");
                soundsFD[2][2] = getAssets().openFd("guitar3.wav");
                soundsFD[2][3] = getAssets().openFd("guitar4.wav");

                soundsFD[3][0] = getAssets().openFd("vocals_female1.wav");
                soundsFD[3][1] = getAssets().openFd("vocals_female2.wav");
                soundsFD[3][2] = getAssets().openFd("vocals_male1.wav");
                soundsFD[3][3] = getAssets().openFd("vocals_male2.wav");

                for(int i = 0; i < 3; i++) {
                    for(int j = 0; j < 3; j++) {
                        sounds[i][j] = mSndPool.load(soundsFD[i][j], 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setPlay(boolean _play) {
            this.mPlay = _play;
        }

        @Override
        public void run() {
            while(true) {

//                this.sleep();
                mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mCurrentVolume = mCurrentVolume / mMaxVolume;

                if(quit)
                    break;

                if(mPlay) {
                    for(int i =0; i < 8; i++) {

                        for(Track tk: mTrackView.mChannels[0].getTracks()) {
                            for(int beat = 0; beat < 8; beat++) {
                                if(tk.grid[beat][0]) {
                                    this.mSp.play(sounds[0][0], mCurrentVolume, mCurrentVolume, 1,0,1);
                                }
                                if(tk.grid[beat][1]) {
                                    this.mSp.play(sounds[0][1], mCurrentVolume, mCurrentVolume, 1,0,1);
                                }
                                if(tk.grid[beat][2]) {
                                    this.mSp.play(sounds[0][2], mCurrentVolume, mCurrentVolume, 1,0,1);
                                }
                                if(tk.grid[beat][3]) {
                                    this.mSp.play(sounds[0][3], mCurrentVolume, mCurrentVolume, 1,0,1);
                                }
                            }

                        }
                        for(Track tk: mTrackView.mChannels[1].getTracks()) {

                        }
                        for(Track tk: mTrackView.mChannels[2].getTracks()) {

                        }
                        for(Track tk: mTrackView.mChannels[3].getTracks()) {

                        }
                    }

                }
            }
        }
    }

}