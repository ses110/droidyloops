package io.github.ses110.dloops;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.github.ses110.dloops.internals.Loop;
import io.github.ses110.dloops.looper.LoopRowView;
import io.github.ses110.dloops.looper.LooperFragment;


public class MainActivity extends Activity implements LooperFragment.OnFragmentInteractionListener, MainMenuFragment.OnFragmentInteractionListener{

    private int rowCount;
    private static LooperFragment looper;
    private static MainMenuFragment menuFragment;
    private Loop curLoop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        menuFragment = new MainMenuFragment();
        fragmentTransaction.add(android.R.id.content, menuFragment);
        fragmentTransaction.commit();

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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // TODO: initialise looper with pre-existing data if any
        looper = LooperFragment.newInstance("pl", "pl");
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.remove(menuFragment);
        fragmentTransaction.replace(android.R.id.content, looper, "PLACEHOLDER");
        fragmentTransaction.commit();
    }
}
