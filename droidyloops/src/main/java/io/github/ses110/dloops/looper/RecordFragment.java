package io.github.ses110.dloops.looper;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import io.github.ses110.dloops.R;
import io.github.ses110.dloops.models.Sample;
import io.github.ses110.dloops.utils.FileHandler;

/**
 * Created by sid9102 on 4/29/2014.
 */
public class RecordFragment extends Fragment implements View.OnClickListener
{
    private ImageButton mRecordButton;
    private MediaRecorder mRecorder;

    private Button saveButton;
    private ImageButton mPlayButton;
    private MediaPlayer mPlayer = null;

    private TextView recordingText;

    boolean recording;
    boolean playing;

    private static final String LOG_TAG = "Recorder";

    private File mFileName;
    private FileHandler handler;

    public RecordFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handler = new FileHandler(getActivity());
        mFileName = new File(handler.getDir(FileHandler.FileType.SAMPLES), "temp.aac");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v =  inflater.inflate(R.layout.fragment_record, container, false);
        recordingText = (TextView) v.findViewById(R.id.recordingText);
        mRecordButton = (ImageButton) v.findViewById(R.id.recordButton);
        mRecordButton.setOnClickListener(this);
        mPlayButton = (ImageButton) v.findViewById(R.id.playRecording);
        mPlayButton.setOnClickListener(this);
        saveButton = (Button) v.findViewById(R.id.save_recording);
        saveButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
            recordingText.setText("Recording!");
            recordingText.setTextColor(Color.RED);
            mRecordButton.setBackground(getResources().getDrawable(R.drawable.recordstop));
        } else {
            stopRecording();
            recordingText.setText("Not recording.");
            recordingText.setTextColor(Color.WHITE);
            mRecordButton.setBackground(getResources().getDrawable(R.drawable.record));
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
            mPlayButton.setBackground(getResources().getDrawable(R.drawable.ic_action_stop));
        } else {
            stopPlaying();
            mPlayButton.setBackground(getResources().getDrawable(R.drawable.ic_action_play));
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.recordButton:
                onRecord(!recording);
                recording = !recording;
                break;
            case R.id.playRecording:
                if(!recording)
                    onPlay(!playing);
                playing = !playing;
                break;
            case R.id.save_recording:

                break;
        }

    }

    private void saveSample(String name) throws IOException, ParseException, JSONException {
        mFileName.renameTo(new File(handler.getDir(FileHandler.FileType.SAMPLES), name + ".aac"));
        Sample s = new Sample(name, name + ".aac");
        ArrayList<Sample> list = s.loadList(handler);
        list.add(s);
        handler.writeJSONDir(handler.saveList(list), FileHandler.FileType.SAMPLES);
        Log.v(LOG_TAG, "Added new sample!");

    }
}
