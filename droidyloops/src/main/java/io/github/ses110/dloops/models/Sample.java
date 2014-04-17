package io.github.ses110.dloops.models;

import android.media.SoundPool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

/**
 * Created by sid9102 on 4/14/2014.
 */
public class Sample implements Saveable
{
    private String name;
    private String path;
    private int spID;

    public Sample(String name, String path)
    {
        this.name = name;
        this.path = path;
        spID = -1; // Indicates that the sample has not been loaded yet.
    }

    public Sample(JSONObject object) throws JSONException
    {
        this.name = object.getString("name");
        this.path = object.getString("path");
        spID = -1;
    }

    public int getSpID() {
        return spID;
    }

    public void setSpID(int spID) {
        this.spID = spID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject toJSON() throws JSONException
    {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("path", path);
        return object;
    }

    public String toString()
    {
        return name;
    }

    public static ArrayList<Sample> loadSamples(SoundPool soundPool)
    {
        ArrayList<Sample> samples = new ArrayList<Sample>();
        JSONParser parser = new JSONParser();
        return samples;
    }
}
