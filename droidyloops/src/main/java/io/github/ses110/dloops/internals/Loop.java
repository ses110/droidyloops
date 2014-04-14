package io.github.ses110.dloops.internals;

import java.util.ArrayList;

/**
 * Created by sid9102 on 4/14/2014.
 */
public class Loop
{
    public ArrayList<Sample> samples;
    public ArrayList<boolean[]> cells;
    private final int maxBeats = 8;
    
    public Loop()
    {
        samples = new ArrayList<Sample>();
        cells = new ArrayList<boolean[]>();
    }

    public void addSample(Sample sample)
    {
        samples.add(sample);
        cells.add(new boolean[maxBeats]);
    }

    public void touch(int x, int y)
    {
        if(y < samples.size())
            cells.get(y)[x] = !cells.get(y)[x];
    }
}
