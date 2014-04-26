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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.ses110.dloops.arranger.ArrangerFragment;
import io.github.ses110.dloops.looper.LooperFragment;
import io.github.ses110.dloops.models.Loop;
import io.github.ses110.dloops.models.Sample;
import io.github.ses110.dloops.models.Song;
import io.github.ses110.dloops.picker.PickerFragment;
import io.github.ses110.dloops.utils.CircularArrayList;
import io.github.ses110.dloops.utils.FileHandler;


public class MainActivity extends FragmentActivity implements LooperFragment.LooperFragmentListener,
        MainMenuFragment.OnFragmentInteractionListener,
        PickerFragment.PickerFragmentListener
{
    /**
     * LOOPER VARIABLES
     */
    // TODO: move these to LooperFragment
    private int rowCount;
    private static LooperFragment looper;
    private static PickerFragment picker;
    private static ArrangerFragment arranger;
    private Loop curLoop;

    private CircularArrayList<Sample> mSpBuffer;
    private AudioManager mAudioManager;
    private SoundPool mSndPool;

    private ProgressDialog mProgDialog;
    FileHandler mFH;
    /**
     * ARRANGER VARIABLES
     */
    // TODO: add arranger variables

    /**
     * ACTIVITY VARIABLES
     */

    private FragmentManager fm;
    File dataDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /************
         *
         *  Set up soundPool stuff
         * *************/
        if(mSndPool == null || mAudioManager == null) {
            mSndPool = new SoundPool(32, AudioManager.STREAM_MUSIC, 0);
            mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        }

        /**
         * Set up ProgressDialog
         * */
        mProgDialog = new ProgressDialog(this);
        mProgDialog.setTitle("Loading");

        mFH = new FileHandler(this);

        mSpBuffer = new CircularArrayList<Sample>(10);

        if(fm == null) {
            fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            MainMenuFragment menuFragment = MainMenuFragment.newInstance("", "");
            fragmentTransaction.add(R.id.mainContainer, menuFragment);
            fragmentTransaction.commit();
        }

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.v("BACKSTACK", "Changed");

            }
        });

        // Do first time setup
        dataDir = getFilesDir();
        File setupFile = new File(dataDir, "setup.txt");
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
            if(!new File(new File(dataDir, "samples"), "vocals_male2.wav").exists())
                Log.e("SETUP", "Default sample not found");
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MainActivity", "In onResume");
    }


    /**
     *  ACTIVITY FUNCTIONS
     */

    private float getVolume() {
        float maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return streamVolume/maxVolume;
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


    /**
     *  LOOPER FUNCTIONS
     */


    @Override
    public void getLoop(Loop loop)
    {

    }

    public void addLoopRow(View view)
    {
        picker = PickerFragment.newInstance(FileHandler.FileType.SAMPLES);
        picker.attachSoundPool(this.mSndPool);
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.remove(fm.findFragmentById(R.id.mainContainer));
        ft.add(R.id.mainContainer, picker);
        ft.addToBackStack(null);
        ft.commit();
        // TODO: make this eventually go back to looper
//        ViewGroup listView = (ViewGroup) looper.getView().findViewById(R.id.loopRowList);
//        LoopRowView child = new LoopRowView(view.getContext());
//        if(curLoop == null)
//        {
//            curLoop = new Loop();
//        }
//        // TODO: fix blank sample
//        curLoop.addSample(new Sample("der", "der"));
//        child.setDetails(rowCount, curLoop);
//        listView.addView(child, 0);
//        rowCount++;
    }

    /**
     *  PICKER FUNCTIONS
     */
    public void closeDialog(int spID) {
        mProgDialog.dismiss();
        playSound(spID);
    }

    private void playSound(int spID) {
        mSndPool.play(spID, getVolume(), getVolume(), 1,0,1);
        Log.v("Playsound", "Playing SOUND id: " + Integer.toString(spID));
    }

    @Override
    public void onPickerSelection(Sample sample) {
        if(sample.getSpID() == -1) {
            mProgDialog.setMessage("Loading sample...");
            mProgDialog.show();

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
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     *  MAIN MENU FUNCTIONS
     */


    public void startClick(View view) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // TODO: initialise looper with pre-existing data if any
        looper = LooperFragment.newInstance("pl", "pl");
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.remove(fm.findFragmentById(R.id.mainContainer));
        fragmentTransaction.add(R.id.mainContainer, looper);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void loadClick(View view)
    {

    }
    public void arrangeClick(View view) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // TODO: initialise arranger with pre-existing data if any
        arranger = ArrangerFragment.newInstance("", "");
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.remove(fm.findFragmentById(R.id.mainContainer));
        fragmentTransaction.add(R.id.mainContainer, arranger);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
