package com.example.droidyloops.dloops;

import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;

/**
 * Created by sergioescoto on 3/22/14.
 *
 * Track
 * An abstraction that represents an instance of a sound clip, whether it'd be a drum pattern
 * or a user recording.
 *
 * Note that the Channel class will contain an arraylist of indefinite amounts of Tracks.
 *
 * A Track needs a start time (in (ms) milliseconds) and its play duration (also in ms).
 * It needs a container that will hold the sound stream or the array of sound streams if its a drum loop.
 */
public class Track {
    private int length;

    private SoundPool mSound;

    private double x_start, x_end;

    public Track(int duration) {
        this.length = duration;

    }
    public void setPos(double x1, double x2) {
        this.x_start = x1;
        this.x_end = x2;
    }
    public int getDuration() { return length; }

    public int setSound(AssetFileDescriptor asset) {
        return setSound(asset, 1);
    }
    public int setSound(AssetFileDescriptor asset, int priority) {
        return mSound.load(asset, priority);
    }
}
