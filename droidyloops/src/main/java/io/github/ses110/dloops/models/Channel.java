package io.github.ses110.dloops.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sid9102 on 4/14/2014.
 */
public class Channel implements Saveable
{
    public ArrayList<Loop> loops;
    public ArrayList<Integer> startPoints;
    public int length;
    public String name;

    public Channel(String name)
    {
        this.name = name;
        loops = new ArrayList<Loop>();
        startPoints = new ArrayList<Integer>();
        length = 64;
    }

    public void addLoop(int x, Loop loop)
    {
        if(x > length - 10)
            length += Loop.maxBeats;
        loops.add(loop);
        startPoints.add(x);
    }

    public int[] curSamples(int x)
    {
        int size = startPoints.size();
        for (int i = 0; i < size; i++) {
            int cur = startPoints.get(i);
            if(cur < x && cur > (x - Loop.maxBeats))
                return loops.get(i).curSamples(x - cur);
            else if(cur > x)
                break;
        }
        return null;
    }

    // TODO: set up Channel.toJSON
    @Override
    public JSONObject toJSON() throws JSONException {
        return null;
    }
}
