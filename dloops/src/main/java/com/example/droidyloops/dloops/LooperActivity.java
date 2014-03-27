package com.example.droidyloops.dloops;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import java.io.IOException;

public class LooperActivity extends Activity {

    private GridView mGridView;
    private SoundPool mPool;
    private SoundPoolThread spThread;
    private boolean play;
    private boolean quit;

    private int beatTime;
    private boolean save = false;
    private int instrument;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        instrument = intent.getIntExtra("instrument", 1);

        setContentView(R.layout.activity_looper);
        mGridView = (GridView) findViewById(R.id.loop_view);
        mPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        //TODO: Make this customizable
        AssetFileDescriptor one;
        AssetFileDescriptor two;
        AssetFileDescriptor three;
        AssetFileDescriptor four;
        int sounds[] = new int[4];
        switch (instrument) {
            case 1:
                try {
                    one = getAssets().openFd("Snare.ogg");
                    two = getAssets().openFd("Kick.wav");
                    three = getAssets().openFd("Hat.wav");
                    four = getAssets().openFd("Clap.wav");

                    sounds[0] = mPool.load(one, 1);
                    sounds[1] = mPool.load(two, 1);
                    sounds[2] = mPool.load(three, 1);
                    sounds[3] = mPool.load(four, 1);
                    mGridView.sampleIDs = sounds;
                    for (int i : sounds) {
                        Log.v("sound", Integer.toString(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    one = getAssets().openFd("bass1.wav");
                    two = getAssets().openFd("bass2.wav");
                    three = getAssets().openFd("bass3.wav");
                    four = getAssets().openFd("bass4.wav");

                    sounds[0] = mPool.load(one, 1);
                    sounds[1] = mPool.load(two, 1);
                    sounds[2] = mPool.load(three, 1);
                    sounds[3] = mPool.load(four, 1);
                    mGridView.sampleIDs = sounds;
                    for (int i : sounds) {
                        Log.v("sound", Integer.toString(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    one = getAssets().openFd("guitar1.wav");
                    two = getAssets().openFd("guitar2.wav");
                    three = getAssets().openFd("guitar3.wav");
                    four = getAssets().openFd("guitar4.wav");

                    sounds[0] = mPool.load(one, 1);
                    sounds[1] = mPool.load(two, 1);
                    sounds[2] = mPool.load(three, 1);
                    sounds[3] = mPool.load(four, 1);
                    mGridView.sampleIDs = sounds;
                    for (int i : sounds) {
                        Log.v("sound", Integer.toString(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    one = getAssets().openFd("vocals_female1.wav");
                    two = getAssets().openFd("vocals_female2.wav");
                    three = getAssets().openFd("vocals_male1.wav");
                    four = getAssets().openFd("vocals_male1.wav");

                    sounds[0] = mPool.load(one, 1);
                    sounds[1] = mPool.load(two, 1);
                    sounds[2] = mPool.load(three, 1);
                    sounds[3] = mPool.load(four, 1);
                    mGridView.sampleIDs = sounds;
                    for (int i : sounds) {
                        Log.v("sound", Integer.toString(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        beatTime = 500;
        spThread=new SoundPoolThread(mPool, mGridView, sounds);

        spThread.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(play)
        {
            spThread.toggleRunning();
            mGridView.playStop();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        spThread.interrupt();
        mPool.release();
        quit = true;
        if(!save)
        {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }
    }

    public void saveAndReturn(View view)
    {
        Intent returnIntent = new Intent();
        String result = "";
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if(mGridView.grid[i][j])
                    result += "1";
                else
                    result += "0";
            }
        }
        returnIntent.putExtra("grid", result);
        returnIntent.putExtra("instrument", instrument);
        save = true;
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void changeBpmDialog(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        BpmDialogFragment changeDialog = new BpmDialogFragment();
        changeDialog.setLooperActivity(this, beatTime);
        changeDialog.show(ft, "Dialog");


    }
    public void changeBpm(float bpm) {
        beatTime = (int)(1000 / (bpm / 60));
    }

    public void playStop(View view)
    {
        play = !play;
        mGridView.playStop();
        spThread.toggleRunning();
    }

    public void playSound(int row)
    {
        if(!play)
        {
            spThread.previews[row] = true;
        }
    }

    class SoundPoolThread extends Thread {

        private GridView mGridView;
        private SoundPool mSoundPool;
        private int[] sounds;
        private boolean play = false;
        private AudioManager mAudioManager;
        private float maxVolume;
        public boolean[] previews = new boolean[4];

        private long lastBeat;
        private int curCol = 0;

        public SoundPoolThread(SoundPool mSoundPool, GridView gridView, int[] sounds)
        {
            this.mSoundPool = mSoundPool;
            this.mGridView = gridView;
            this.sounds = sounds;
            lastBeat = System.currentTimeMillis() - beatTime;
            mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }

        public void toggleRunning() {
            play = !play;
            curCol = -1;
            lastBeat = System.currentTimeMillis() - beatTime;
        }

        @Override
        public void run() {
            while (true) {
                if(quit)
                    break;
                // Get the volume
                float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                streamVolume = streamVolume / maxVolume;
                if (play) {
                    synchronized (mGridView.grid) {

                        long curTime = System.currentTimeMillis();
                        if (curTime - lastBeat >= beatTime) {

                            curCol++;
                            if (curCol == 8 || curCol == -1)
                                curCol = 0;
                            for (int i = 0; i < 4; i++) {
                                if (mGridView.grid[curCol][i]) {
                                    mSoundPool.play(mGridView.sampleIDs[i], streamVolume, streamVolume, 1, 0, 1);
                                }
                            }
                            lastBeat = curTime;
                            mGridView.incrementHL();
                        }

                    }
                }
                else // preview sounds as you tap
                {
                    for(int i = 0; i < 4; i++)
                    {
                        if(previews[i]) {
                            mSoundPool.play(i + 1, streamVolume, streamVolume, 1, 0, 1);
                            previews[i] = false;
                        }
                    }
                }
            }
        }
    }

    public class BpmDialogFragment extends DialogFragment {
        LooperActivity mLooper;
        int mBeatTime;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Set BPM");
            builder.setMessage("Input your BPM:");


            final EditText input = new EditText(LooperActivity.this);
            input.setText(Float.toString((1000/mBeatTime) * 60));
            input.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    float bpm = Float.parseFloat((input.getText().toString()));
                    if(bpm < 30)
                        bpm = 30;
                    if(bpm > 300)
                        bpm = 300;
                    mLooper.changeBpm(bpm);
                }
            })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getDialog().dismiss();
                        }
                    });
            return builder.create();
        }

        public void setLooperActivity(LooperActivity looper, int beatTime)
        {
            mLooper = looper;
            mBeatTime = beatTime;
        }


    }
}
