package io.github.ses110.dloops.models;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import io.github.ses110.dloops.utils.FileHandler;
import io.github.ses110.dloops.utils.Saveable;

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

    // TODO: do this
    @Override
    public ArrayList<? extends Saveable> loadList(FileHandler fileHandler) throws JSONException, FileNotFoundException, ParseException {
        return null;
    }
}
