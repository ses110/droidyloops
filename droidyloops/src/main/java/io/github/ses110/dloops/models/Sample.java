package io.github.ses110.dloops.models;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import io.github.ses110.dloops.utils.FileHandler;
import io.github.ses110.dloops.utils.Saveable;

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
        this.name = (String) object.get("name");
        this.path = (String) object.get("path");
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

    @Override
    public ArrayList<Sample> loadList(FileHandler fileHandler) throws JSONException, FileNotFoundException, ParseException
    {
        JSONArray array = fileHandler.readJSON(FileHandler.FileType.SAMPLES);
        ArrayList<Sample> result = new ArrayList<Sample>();
        for (Object anArray : array) {
            result.add(new Sample((JSONObject) anArray));
        }
        return result;
    }
}
