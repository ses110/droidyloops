package io.github.ses110.dloops;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
    }
}
