package io.github.ses110.dloops.internals;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sid9102 on 4/16/2014.
 *
 * For holding song information
 */
public class Song
{
    private ArrayList<Channel> channels;
    // TODO: For individual volumes on channels
    private ArrayList<Integer> volumes;

    public Song()
    {
        channels = new ArrayList<Channel>();
    }

    public void setChannels(Channel[] channels)
    {
        Collections.addAll(this.channels, channels);
    }

    public Channel[] getChannels()
    {
        Channel[] result = new Channel[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            result[i] = channels.get(i);
        }
        return result;
    }
}
