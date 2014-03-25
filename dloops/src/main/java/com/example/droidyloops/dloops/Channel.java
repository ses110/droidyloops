package com.example.droidyloops.dloops;

import java.util.ArrayList;

/**
 * Created by sergioescoto on 3/23/14.
 *
 * Channel
 *
 */
public class Channel {
    ArrayList<Track> mTracks;
    int mChannelLength;
    public Channel() {
        this.mChannelLength = 0;
        mTracks = new ArrayList<Track>();
    }
    public void addTrack(Track track) {
        mTracks.add(track);
        this.mChannelLength += track.duration();
    }
}
