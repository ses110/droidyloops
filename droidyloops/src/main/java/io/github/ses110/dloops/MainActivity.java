package io.github.ses110.dloops;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.github.ses110.dloops.arranger.ArrangerFragment;
import io.github.ses110.dloops.looper.LoopRowView;
import io.github.ses110.dloops.looper.LooperFragment;
import io.github.ses110.dloops.looper.RecordFragment;
import io.github.ses110.dloops.models.Loop;
import io.github.ses110.dloops.models.Sample;
import io.github.ses110.dloops.models.Song;
import io.github.ses110.dloops.picker.PickerFragment;
import io.github.ses110.dloops.utils.BpmDialog;
import io.github.ses110.dloops.utils.CircularArrayList;
import io.github.ses110.dloops.utils.FileHandler;
import io.github.ses110.dloops.utils.ProgressBarView;


public class MainActivity extends FragmentActivity implements ArrangerFragment.ArrangerFragmentListener, LooperFragment.LooperFragmentListener,
        MainMenuFragment.onMainMenuFragmentListener,
        PickerFragment.PickerFragmentListener
{

    /**
     * LOOPER VARIABLES
     */
    // TODO: move these to LooperFragment
    private int rowCount;
    public static LooperFragment looper;
    public static PickerFragment picker;
    public static ArrangerFragment arranger;

    private int mBeatTime = fromBpmToBeat(120);
    private Loop curLoop;
    public ArrayList<LoopRowView> mLoopRows;
    private Runnable mRunnable;
    private boolean mPlaying = false;


    private CircularArrayList<Sample> mSpBuffer;
    private AudioManager mAudioManager;
    private SoundPool mSndPool;

    public ProgressDialog mProgDialog;

    FileHandler mFH;
    /**
     * ARRANGER VARIABLES
     */
    // TODO: add arranger variables

    /**
     * ACTIVITY VARIABLES
     */

    private FragmentManager mFragMan;
    File mDataDir;

    /*
    *   BPM Listeners for the progress bar
    * */

    private OnBPMListener mOnBPMListener;

    public ProgressBarView mProgressBar;



    public interface OnBPMListener {

        public void onBPM(int beatCount);
    }

    public void setOnBPMListener(OnBPMListener l) {
        this.mOnBPMListener = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        mProgressBar = new ProgressBarView(this, width, height, (width / 9),fromBeatToBPM(mBeatTime));
        this.setOnBPMListener(mProgressBar);

        /***************************
         *      Set up soundPool
         * ************************/
        if(mSndPool == null || mAudioManager == null) {
            mSndPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
            mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        }
        mSpBuffer = new CircularArrayList<Sample>(10);

        /*************************
         * Set up ProgressDialog
         * */
        mProgDialog = new ProgressDialog(this);

        mFH = new FileHandler(this);


        // Set up Loop

        mLoopRows = new ArrayList<LoopRowView>();

        // Fragment setup: start MainMenuFragment
        if(mFragMan == null) {
            mFragMan = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragMan.beginTransaction();
            MainMenuFragment menuFragment = MainMenuFragment.newInstance("", "");
            fragmentTransaction.add(R.id.mainContainer, menuFragment);
            fragmentTransaction.commit();
        }

        mFragMan.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.v("BACKSTACK", "FragmentManager backstack changed.");
            }
        });

        // Do first time setup
        this.doSetupFile();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
    private void doSetupFile() {
        mDataDir = getFilesDir();
        File setupFile = new File(mDataDir, "setup.txt");
        if(!setupFile.exists())
        {
            Log.v("SETUP", "First time run detected, copying files");
            try {
                FileHandler fh = new FileHandler(this);
                fh.firstTimeSetup(getAssets());
                FileOutputStream fos = openFileOutput("setup.txt", Context.MODE_PRIVATE);
                fos.write("setup complete".getBytes());
                fos.close();
            } catch (IOException e) {
                Log.e("SETUP", "FAILED");
                Log.e("SETUP", e.toString());
            } catch (JSONException e) {
                Log.e("SETUP", "JSONException!");
            }
            Log.v("SETUP", "Setup complete.");
        }
        else
        {
            Log.v("SETUP", "Setup file found");
            if(!new File(new File(mDataDir, "samples"), "vocals_male2.wav").exists())
                Log.e("SETUP", "Default sample not found");
        }
    }


    private float getVolume() {
        float maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return streamVolume/maxVolume;
    }

    /**
     *  ACTIVITY FUNCTIONS
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MainActivity", "In onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlaying = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlaying = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    /*
    *   Arranger Functions
    * */


    //Handle transition between arranger to looper. Saves arranger view
    public void newLoopClick(int channel, int cell) {
        FragmentTransaction fragmentTransaction = mFragMan.beginTransaction();
        // TODO: initialise looper with pre-existing data if any
        looper = LooperFragment.newInstance("pl", "pl");
        looper.setChannel(channel, cell);
        curLoop = new Loop();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.hide(arranger);
//        (mFragMan.findFragmentById(R.id.mainContainer));
        fragmentTransaction.add(R.id.mainContainer, looper);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //Handle transition between arranger to looper, but with a preset loop
    public void loadLoopClick(Loop init) {
        FragmentTransaction fragmentTransaction = mFragMan.beginTransaction();
        looper = LooperFragment.newInstance("pl", "pl");
        boolean setup = false;
        if(init == null)
            init = new Loop();
        else
            setup = true;
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.hide(arranger);
//        (mFragMan.findFragmentById(R.id.mainContainer));
        fragmentTransaction.add(R.id.mainContainer, looper);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mFragMan.executePendingTransactions();
        if(setup)
            looper.setup(init);
        curLoop = init;
    }


     /**
     *  LOOPER FUNCTIONS
     */

    @Override
    public void getLoop(Loop loop) {

    }


    public int fromBeatToBPM(int beat) {
        return (int) ((1000.0/beat) * 60.0);
    }
    public int fromBpmToBeat(int bpm) {
        return  (int) (1000.0 / (bpm / 60.0));
    }

    public void setBPM(int newBPM) {
        this.mBeatTime = fromBpmToBeat(newBPM);
    }

    public void changeBpmDialog(View view) {
        FragmentManager fm = getSupportFragmentManager();
        Log.v("BPM Dialog", "this.mBeatTime: " + this.mBeatTime);
        BpmDialog changeBpm = new BpmDialog(this, fromBeatToBPM(this.mBeatTime));
        changeBpm.show(fm, "Dialog");
        Log.v("BPM Dialog", "changeBpm.NewBPM:" + changeBpm.newBPM());
    }

    public void newLoopRow(View view)
    {
        startPlay(null);
        picker = PickerFragment.newInstance(FileHandler.FileType.SAMPLES);
        picker.attachSoundPool(this.mSndPool);
        FragmentTransaction ft = mFragMan.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.hide(mFragMan.findFragmentById(R.id.mainContainer));
        ft.add(R.id.mainContainer, picker);
        ft.addToBackStack("loop");
        ft.commit();
    }
    public void addLoopRow(Sample s) {
        mFragMan.popBackStack("loop", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mFragMan.executePendingTransactions();
        looper = (LooperFragment) mFragMan.findFragmentById(R.id.mainContainer);
        ViewGroup listView = (ViewGroup) looper.getView().findViewById(R.id.loopRowList);
        LoopRowView child = new LoopRowView(this);
        if(curLoop == null)
        {
            curLoop = new Loop();
        }
        curLoop.addSample(s);
        child.setDetails(rowCount, curLoop, s);
        this.mLoopRows.add(child);
        listView.addView(child, 0);
        rowCount++;
    }
    /*
    *       TODO: Looper: press save, save the current loop and send it back.
    * */
    public void saveLoop(int channel, int cell) throws JSONException {
        Log.v("LOOPER", "Save loop");
        startPlay(null);
        if(curLoop != null) {
            Log.v("LOOPER", curLoop.toJSON().toString());
            arranger.addLoop(channel, cell, curLoop);
            mFragMan.popBackStack();
        }
        curLoop = null;
        rowCount = 0;
    }

    /*
    *  Start playing sounds
    * */
    public void playLoop() {
        mRunnable = new Runnable()
        {
            int mIndex = 0;

            int tempId = -1;
            public void run() {
                while(mPlaying) {
//                    if(tempId != -1)
//                        playMute(tempId);

                    if(mOnBPMListener != null) {
                        mOnBPMListener.onBPM(mIndex);
                    }
                    long millis = System.currentTimeMillis();

                    for (int id : curLoop.curSamples(mIndex)) {
                        if(tempId == -1)
                            tempId = id;
                        if(id != - 1)
                            playSound(id);
                    }

                    mIndex = (mIndex + 1) % Loop.maxBeats;
                    try {
                        Thread.sleep(mBeatTime - (System.currentTimeMillis()-millis));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        for(LoopRowView row : mLoopRows)
                        {
                            row.reset();
                        }
                    }
                });
            }

        };
        mPlaying = true;
        Thread thandler = new Thread(mRunnable);
        thandler.start();
    }



    public void startPlay(View view) {
        Log.v("Looper", "startPlay called");
        if(view == null) {
            if(mPlaying) {
                try {
                    ((ViewGroup) mProgressBar.getParent()).removeView(mProgressBar);
                }
                catch (NullPointerException e)
                {
                    mPlaying = false;
                    Log.e("startPlay", e.toString());
                }
            }
            findViewById(R.id.play_button).setBackground(getResources().getDrawable(R.drawable.ic_action_play));
            mPlaying = false;
        }
        else if(curLoop.curSamples(0) != null){
            if (mPlaying) {
                mPlaying = false;
                view.setBackground(getResources().getDrawable(R.drawable.ic_action_play));
                ((ViewGroup) view.getParent()).removeView(mProgressBar);
            } else {
                this.playLoop();
                ((ViewGroup) view.getParent()).addView(mProgressBar);
                view.setBackground(getResources().getDrawable(R.drawable.ic_action_stop));
            }
        }
    }

    /**
    *       ARRANGER FUNCTIONS
     */

    private boolean arrangerPlay = false;
    public void onPlayArranger()
    {
        if(arrangerPlay)
        {
            arrangerPlay = false;
            arranger.mMenu.findItem(R.id.playSong).setIcon(R.drawable.ic_action_play);
        }
        else
        {
            arrangerPlay = true;
            arranger.mMenu.findItem(R.id.playSong).setIcon(R.drawable.ic_action_stop);
            playArranger();
        }
    }
    public void playArranger() {
        final int length = arranger.length();
        Log.v("playArranger got length", Integer.toString(length));

        mRunnable = new Runnable()
        {
            int loopIndex = 0;
            int colIndex = 0;
            int tempId = -1;
            public void run() {
                for(colIndex = 0; colIndex < length && arrangerPlay; colIndex++){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arranger.mTrackGridView.setHighlightColumn(colIndex);
                        }
                    });


                    for(loopIndex = 0; loopIndex < 8; loopIndex++) {
//                        if (tempId != -1)
//                            playMute(tempId);

                        if (mOnBPMListener != null) {
                            mOnBPMListener.onBPM(loopIndex);
                        }
                        long millis = System.currentTimeMillis();

                        for (int id : arranger.curSamples(colIndex, loopIndex)) {
                            if (tempId == -1)
                                tempId = id;
                            if (id != -1)
                                playSound(id);
                        }
                        try {
                            Thread.sleep(mBeatTime - (System.currentTimeMillis() - millis));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arranger.mTrackGridView.removeHighlightColumn(colIndex);
                            }
                        });
                        
                    }
                }
            }

        };
        mPlaying = true;
        Thread thandler = new Thread(mRunnable);
        thandler.start();
    }

    /**
     *  PICKER FUNCTIONS
     */

    public void startRecorder()
    {
        RecordFragment record = new RecordFragment();
        FragmentTransaction ft = mFragMan.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.remove(mFragMan.findFragmentById(R.id.mainContainer));
        ft.add(R.id.mainContainer, record);
        ft.commit();
    }

    public void finishRecorder()
    {
        picker = PickerFragment.newInstance(FileHandler.FileType.SAMPLES);
        picker.attachSoundPool(this.mSndPool);
        FragmentTransaction ft = mFragMan.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        ft.hide(mFragMan.findFragmentById(R.id.mainContainer));
        ft.add(R.id.mainContainer, picker);
        ft.commit();
    }

    public void closeDialog(int spID) {
        mProgDialog.dismiss();
        playSound(spID);
    }

    public void previewSound(int spID) {
        if(!mPlaying) {
            playSound(spID);
        }
    }

    private void playSound(int spID) {
        mSndPool.play(spID, getVolume(), getVolume(), 1,0,1);
        Log.v("Playsound", "Playing SOUND id: " + Integer.toString(spID));
    }
//    private void playMute(int spID) {
//        mSndPool.play(spID, 0, 0, 1, -1, 1f);
//        Log.v("Playsound", "Playing SOUND id: " + Integer.toString(spID));
//    }

    @Override
    public void onPickerSelection(Sample sample) {
        if(sample.getSpID() == -1) {
            mProgDialog.show();
            mProgDialog.setContentView(R.layout.dialog_load);
            int id = mFH.loadSample(sample, mSndPool);
            if(mSpBuffer.isFull()) {
                Sample temp = mSpBuffer.remove(0);
                mSndPool.unload(temp.getSpID());
                temp.setSpID(-1);
            }
            mSpBuffer.add(sample);
        } else {
            playSound(sample.getSpID());
        }
        Log.v("PICKER", "Selected Sample: " + sample.toString());
    }

    @Override
    public void onPickerSelection(Loop loop) {

    }

    @Override
    public void onPickerSelection(Song song) {

    }

    @Override
    public void createNewLoop(Uri uri) {

    }
    @Override
    public void onMainMenuInteraction(Uri uri) {

    }

    /**
     *  MAIN MENU FUNCTIONS
     */

    public void startClick(View view) {
        FragmentTransaction fragmentTransaction = mFragMan.beginTransaction();
        // TODO: initialise looper with pre-existing data if any
        looper = LooperFragment.newInstance("pl", "pl");
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.remove(mFragMan.findFragmentById(R.id.mainContainer));
        fragmentTransaction.add(R.id.mainContainer, looper);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void loadClick(View view) {

    }
    public void arrangeClick(View view) {
        FragmentTransaction fragmentTransaction = mFragMan.beginTransaction();
        // TODO: initialise arranger with pre-existing data if any
        arranger = ArrangerFragment.newInstance("ar", "ar");
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.remove(mFragMan.findFragmentById(R.id.mainContainer));
        fragmentTransaction.add(R.id.mainContainer, arranger);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
