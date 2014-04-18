package io.github.ses110.dloops.models;

import android.util.Log;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.ses110.dloops.utils.FileHandler;
import io.github.ses110.dloops.utils.Saveable;

/**
 * Created by sid9102 on 4/14/2014.
 */
public class Loop implements Saveable
{
    private ArrayList<Sample> samples;
    private String name;
    public ArrayList<boolean[]> cells;
    public final static int maxBeats = 8;
    // TODO: Add variable length

    public Loop()
    {
        samples = new ArrayList<Sample>();
        cells = new ArrayList<boolean[]>();
    }

    // TODO: implement loading
    public Loop(JSONObject savedLoop)
    {

    }

    // Returns the number of tracks in this loop
    public int length()
    {
        return samples.size();
    }

    // Add a sample to this loop
    public void addSample(Sample sample)
    {
        samples.add(sample);
        cells.add(new boolean[maxBeats]);
    }

    // Update the internal boolean structure when the looper has been touched
    public void touch(int x, int y) {
        if (y < samples.size())
        {
            cells.get(y)[x] = !cells.get(y)[x];
            Log.v("Loop", "got press");
            Log.v("Bool is now", Arrays.toString(cells.get(y)));
        }
    }

    // Returns the IDs of all samples to be played
    public int[] curSamples(int x)
    {
        int size = cells.size();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            boolean[] curRow = cells.get(i);
            if(curRow[x])
                result[i] = samples.get(i).getSpID();
            else
                result[i] = -1;
        }
        return result;
    }

    // TODO: Loop.toJSON
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
    public ArrayList<? extends Saveable> loadList(FileHandler fileHandler) throws JSONException, FileNotFoundException, ParseException
    {
        return null;
    }

}