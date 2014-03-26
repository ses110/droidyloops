package com.example.droidyloops.dloops;

import android.content.res.AssetFileDescriptor;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Parcel;
import android.os.Parcelable;

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
public class Track implements Parcelable {
    private int length,
                start;

    private SoundPool mSound;

    private Rect mRect;

    private Track(Parcel in) {

    }
    public Track(int duration) {
        this.length = duration;
        this.mRect = new Rect();

    }
    public void setRect(int left, int top, int right, int bottom) {
        this.mRect.set(left, top, right, bottom);
    }
    public Rect getRect() {
        return this.mRect;
    }

    public int getDuration() { return length; }

    public int setSound(AssetFileDescriptor asset) {
        return setSound(asset, 1);
    }
    public int setSound(AssetFileDescriptor asset, int priority) {
        return mSound.load(asset, priority);
    }

    @Override
    public int describeContents() {
        //Safe to ignore this method for now
        return 0;
    }

    @Override
    // All this object's data to write into the parsel
    public void writeToParcel(Parcel out, int flags) {
        ;
    }

    // Inner class needed to regenerate a track object.
    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }
        @Override
        public Track[] newArray(int i) {
            return new Track[0];
        }
    };
}
