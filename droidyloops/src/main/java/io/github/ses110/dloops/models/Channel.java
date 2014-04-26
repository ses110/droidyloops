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
public class Channel implements Saveable
{
    public ArrayList<Loop> loops;
    public String name;

    public Channel(String name)
    {
        this.name = name;
        loops = new ArrayList<Loop>();
    }

    public Channel(JSONObject object) throws JSONException {
        this.name = (String) object.get("name");

        JSONArray getLoops = (JSONArray) object.get("loops");

        for (int i = 0; i < getLoops.size(); i++) {
            JSONObject current = (JSONObject) getLoops.get(i);
            loops.add(new Loop(current));
        }
    }

    public void addLoop(int x, Loop loop)
    {
        loops.add(x, loop);
    }
    public void removeLoop(int x) {
        loops.remove(x);
    }

    public int[] curSamples(int channelOffset, int loopOffset)
    {
        Loop curLoop = loops.get(channelOffset);
        int[] result = null;
        if(curLoop != null) {
            result = curLoop.curSamples(loopOffset);
        }
        return result;   
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("loops", FileHandler.saveList(loops));
        
        return result;
    }

    @Override
    public ArrayList<Channel> loadList(FileHandler fileHandler) throws JSONException, FileNotFoundException, ParseException {
        JSONArray array = fileHandler.readJSONDir(FileHandler.FileType.SAMPLES);
        ArrayList<Channel> result = new ArrayList<Channel>();
        for (Object anArray : array) {
            result.add(new Channel((JSONObject) anArray));
        }
        return result;
    }
}
