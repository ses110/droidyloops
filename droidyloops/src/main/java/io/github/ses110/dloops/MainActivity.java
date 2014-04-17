package io.github.ses110.dloops;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import io.github.ses110.dloops.internals.Loop;
import io.github.ses110.dloops.internals.Sample;
import io.github.ses110.dloops.looper.LoopRowView;
import io.github.ses110.dloops.looper.LooperFragment;


public class MainActivity extends Activity implements LooperFragment.OnFragmentInteractionListener, MainMenuFragment.OnFragmentInteractionListener{

    private int rowCount;
    private static LooperFragment looper;
    private static MainMenuFragment menuFragment;
    private FragmentManager fragmentManager;
    private Loop curLoop;

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
            fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            menuFragment = new MainMenuFragment();
            fragmentTransaction.add(R.id.mainContainer, menuFragment);
            fragmentTransaction.commit();
        }
        appState = AppState.MENU;

        // Do first time setup
        dataDir = getFilesDir();
        File setupFile = new File(dataDir, "setup.txt");
        if(!setupFile.exists())
        {
            Log.v("SETUP", "First time run detected, copying files");
            try {
                firstTimeSetup();
                FileOutputStream fos = openFileOutput("setup.txt", Context.MODE_PRIVATE);
                fos.write("setup complete".getBytes());
                fos.close();
            } catch (IOException e) {
                Log.e("SETUP", "FAILED");
                Log.e("SETUP", e.toString());
            }
            Log.v("SETUP", "Setup complete.");
        }
        else
        {
            Log.v("SETUP", "Setup file found");
            if(!new File(new File(dataDir, "samples"), "vocals_male2.wav").exists())
                Log.e("SETUP", "sample not found");
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    // First time setup
    private void firstTimeSetup() throws IOException
    {
        AssetManager am = getAssets();
        String[] files = am.list("samples");
        InputStream in;
        OutputStream out;

        File sampleDir = new File(getFilesDir(), "samples");
        sampleDir.mkdirs();

        for (String fileName : files)
        {
            in = am.open("samples/" + fileName);
            out = new FileOutputStream(new File(sampleDir, fileName));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MainActivity", "In onResume");
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

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void startClick(View view)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // TODO: initialise looper with pre-existing data if any
        looper = LooperFragment.newInstance("pl", "pl");
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.mainContainer, looper, "PLACEHOLDER");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        appState = AppState.LOOPER;
    }
}
