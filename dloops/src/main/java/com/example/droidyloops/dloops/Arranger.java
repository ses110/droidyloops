package com.example.droidyloops.dloops;

import android.app.Activity;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/*
* Arranger will simply serve as a container for the TrackView class, which is a class that extends
* SurfaceView; it handles the graphical side of drawing of track rectangles.
* Arranger also creates a Thread subclass to handle playing the sounds and updating
* */

public class Arranger extends Activity {

    private TrackView mTrackView;
    private SoundPool mSndPool;
    private playThread mSpThread;
    private boolean quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove the status bar, title bar and make the application fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //setContentView(R.layout.arrange);
        setContentView(new TableMainLayout(this));
//        GridView mTrackGrid = (GridView) findViewById(R.id.TrackGrid);
//        mTrackGrid.getNumColumns();


//        mTrackView = new TrackView(this);

//        mSndPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

//        mSpThread = new playThread(mSndPool, mTrackView);
//        mSpThread.start();
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

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        mSpThread.interrupt();
//        mSndPool.release();
        quit = true;
    }

    // Create a new sound
    public void newSound(View view) {
//        Intent newTrack = new Intent(this, LooperActivity.class);
//        //newTrack.putExtra();
//        startActivityForResult(newTrack,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    class playThread extends Thread {
        private TrackView mTrackView;
        private SoundPool mSp;
        private boolean mRun;

        public playThread(SoundPool sp, TrackView tv) {
            this.mSp = sp;
            this.mTrackView = tv;
        }

        public void setRunning(boolean _run) {
            this.mRun = _run;
        }

        @Override
        public void run() {
            while(true) {
                if(quit)
                    break;

            }
        }
    }

}