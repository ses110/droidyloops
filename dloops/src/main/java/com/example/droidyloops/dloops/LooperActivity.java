package com.example.droidyloops.dloops;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class LooperActivity extends Activity {

    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_looper);
        mGridView = (GridView) findViewById(R.id.loop_view);
    }

    public void playStop(View view)
    {
        mGridView.playStop();
    }
}
