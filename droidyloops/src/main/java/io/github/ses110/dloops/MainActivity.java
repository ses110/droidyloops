package io.github.ses110.dloops;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.ses110.dloops.models.Loop;
import io.github.ses110.dloops.models.Sample;
import io.github.ses110.dloops.models.Song;
import io.github.ses110.dloops.looper.LoopRowView;
import io.github.ses110.dloops.looper.LooperFragment;
import io.github.ses110.dloops.picker.PickerFragment;
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
    private Loop curLoop;

    /**
     * ARRANGER VARIABLES
     */
    // TODO: add arranger variables

    /**
     * ACTIVITY VARIABLES
     */

    private static MainMenuFragment menuFragment;
    private FragmentManager fragmentManager;

    // Tells the activity what fragment to display
    private enum AppState
    {
        MENU, ARRANGER, LOOPER, PICKER
    }
    private AppState appState;
    File dataDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            menuFragment = new MainMenuFragment();
            fragmentTransaction.add(R.id.mainContainer, menuFragment);
            fragmentTransaction.commit();
        }
        appState = AppState.MENU;

        // Set up backbutton handling of state
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // TODO: handle all cases
                switch(appState)
                {
                    case LOOPER:
                        appState = AppState.MENU;
                        break;
                    case PICKER:
                        appState = AppState.MENU;
                        break;
                }
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void addLoopRow(View view)
    {
        //TODO: make this hook up to curLoop, and initialise with name and sample
        ViewGroup listView = (ViewGroup) looper.getView().findViewById(R.id.loopRowList);
        LoopRowView child = new LoopRowView(view.getContext());
        if(curLoop == null)
        {
            curLoop = new Loop();
        }
        // TODO: fix blank sample
        curLoop.addSample(new Sample("der", "der"));
        child.setDetails(rowCount, curLoop);
        listView.addView(child, 0);
        rowCount++;
    }

    /**
     *  PICKER FUNCTIONS
     */

    @Override
    public void onPickerSelection(Sample sample) {
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
        if (appState != AppState.LOOPER)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // TODO: initialise looper with pre-existing data if any
            picker = PickerFragment.newInstance(FileHandler.FileType.SAMPLES);
            //looper = LooperFragment.newInstance("pl", "pl");
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.mainContainer, picker, "PLACEHOLDER");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            appState = AppState.PICKER;
        }
    }

    public void loadClick(View view)
    {

    }
}
