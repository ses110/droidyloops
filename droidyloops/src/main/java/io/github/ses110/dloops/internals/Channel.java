package io.github.ses110.dloops.internals;

import java.util.ArrayList;

/**
 * Created by sid9102 on 4/14/2014.
 */
public class Channel
{
    public ArrayList<Loop> loops;
    public ArrayList<Integer> startPoints;
    public int length;
    public String name;
    private final int maxBeats = 8;

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
            length += maxBeats;
        loops.add(loop);
        startPoints.add(x);
    }

    public int[] curSamples(int x)
    {
        int size = startPoints.size();
        for (int i = 0; i < size; i++) {
            int cur = startPoints.get(i);
            if(cur < x && cur > (x - maxBeats))
                return loops.get(i).curSamples(x - cur);
            else if(cur > x)
                break;
        }
        return null;
    }
}
