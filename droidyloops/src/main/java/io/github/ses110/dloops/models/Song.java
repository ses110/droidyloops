package io.github.ses110.dloops.models;

import org.json.JSONException;
import org.json.simple.JSONArray;
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

    public Song(JSONObject object) throws JSONException {
        this.name = (String) object.get("name");

        JSONArray getChannels = (JSONArray) object.get("loops");

        for (int i = 0; i < getChannels.size(); i++) {
            JSONObject current = (JSONObject) getChannels.get(i);
            channels.add(new Channel(current));
        }
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
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("channels", FileHandler.saveList(channels));
        JSONArray putVolumes = new JSONArray();
        putVolumes.addAll(this.volumes);
        result.put("volumes", putVolumes);

        return result;
    }

    public String toString()
    {
        return name;
    }

    @Override
    public ArrayList<Song> loadList(FileHandler fileHandler) throws JSONException, FileNotFoundException, ParseException {
        JSONArray array = fileHandler.readJSONDir(FileHandler.FileType.SONGS);
        ArrayList<Song> result = new ArrayList<Song>();
        for (Object anArray : array) {
            result.add(new Song((JSONObject) anArray));
        }
        return result;
    }
}
