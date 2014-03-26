package com.example.droidyloops.dloops;

import java.util.ArrayList;

/**
 * Created by sergioescoto on 3/23/14.
 *
 * Channel
 *
 */
public class Channel {
    private String mName;
    ArrayList<Track> mTracks;
    int mChannelLength;

    public Channel() {
        this("New Track");
    }
    public Channel(String name) {
        this.mName = name;
        this.mChannelLength = 0;
        this.mTracks = new ArrayList<Track>();
    }
    public ArrayList<Track> getTracks() {
        return mTracks;
    }

    public void addTrack(Track track) {
        this.mTracks.add(track);
        this.mChannelLength += track.getDuration();
    }
    public String toString() {
        return mName;
    }
}
