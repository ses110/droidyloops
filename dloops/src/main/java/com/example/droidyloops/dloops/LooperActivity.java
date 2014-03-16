package com.example.droidyloops.dloops;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class LooperActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_looper);
        final View contentView = findViewById(R.id.loop_view);
    }

    public void playStop(View view)
    {
        //Start/stop playback here
    }
}
