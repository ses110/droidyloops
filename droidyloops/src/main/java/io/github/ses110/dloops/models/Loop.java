package io.github.ses110.dloops.models;

import android.util.Log;

import org.json.simple.JSONArray;
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

    public Loop() {
        samples = new ArrayList<Sample>();
        cells = new ArrayList<boolean[]>();

        this.addSample(new Sample("bass one", "bass1.wav"));
        this.addSample(new Sample("snares", "drum_Snare.wav"));
        try {
            this.toJSON();
        } catch(JSONException e) {
            Log.e("LoopError", "Loop: JSON exception");
        }

    }

    // TODO: implement loading
    public Loop(JSONObject savedLoop) throws JSONException
    {
        this.name = (String) savedLoop.get("name");
        JSONArray getSamples = (JSONArray) savedLoop.get("samples");

        for (int i = 0; i < getSamples.size(); i++) {
            JSONObject current = (JSONObject) getSamples.get(i);
            samples.add(new Sample(current));
        }

        JSONArray getCells = (JSONArray) savedLoop.get("cells");
        for (int i = 0; i < getCells.size(); i++) {
            JSONArray current = (JSONArray) getCells.get(i);
            boolean[] curRow = new boolean[current.size()];
            for (int j = 0; j < current.size(); j++) {
                curRow[j] = (Boolean) current.get(j);
            }
            this.cells.add(curRow);
        }

        Log.v("Loop loadJSON->name", this.name);
        for(Sample s: this.samples)
            Log.v("Loop loadJSON->samples", s.toString());
        for(boolean[] b: this.cells)
            Log.v("Loop loadJSON->cells", Arrays.toString(b));

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
        JSONObject result = new JSONObject();
//        JSONArray list =
        result.put("name", this.name);
        result.put("samples", FileHandler.saveList(samples));

        JSONArray jsonCellRows = new JSONArray();
        for(boolean[] arr: cells) {
            JSONArray cellRow = new JSONArray();
            for(boolean b: arr) {
                cellRow.add(b);
            }
            jsonCellRows.add(cellRow);
        }
        result.put("cells", jsonCellRows);
        Log.v("Loop", result.toString());

        return result;
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
