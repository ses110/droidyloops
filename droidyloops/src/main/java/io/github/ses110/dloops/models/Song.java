package io.github.ses110.dloops.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sid9102 on 4/16/2014.
 *
 * For holding song information
 */
public class Song implements Saveable
{
    private ArrayList<Channel> channels;
    // TODO: For individual volumes on channels
    private ArrayList<Integer> volumes;
    private String name;

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

    @Override
    public JSONObject toJSON() throws JSONException {
        return null;
    }

    public String toString()
    {
        return name;
    }

    public static ArrayList<Song> loadSongs()
    {
        ArrayList<Song> songs = new ArrayList<Song>();
        // TODO: Load songs
        return songs;
    }
}
