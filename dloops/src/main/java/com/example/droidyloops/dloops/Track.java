package com.example.droidyloops.dloops;

import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;

/**
 * Created by sergioescoto on 3/22/14.
 *
 * Track
 * An abstraction that represents an instance of a sound clip, whether it'd be a drum pattern
 * or a user recording. The Channel class will contain an arraylist of indefinite amounts of
 * Tracks.
 *
 * A Track needs a start time (in (ms) milliseconds) and its play duration (also in ms).
 * It needs a container that will hold the sound stream
 */
public class Track {
    private int startTime,
                   length;

    private SoundPool mSound;


    public int setSound(AssetFileDescriptor asset) {
        return setSound(asset, 1);
    }
    public int setSound(AssetFileDescriptor asset, int priority) {
        return mSound.load(asset, priority);
    }
}
