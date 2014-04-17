package io.github.ses110.dloops.utils;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by sid9102 on 4/17/2014.
 *
 * Enforces common methods that should be implemented by saveable things like loops, samples, and songs
 */
public interface Saveable
{
    public JSONObject toJSON() throws JSONException;
    public String toString();
    public ArrayList<? extends Saveable> loadList(FileHandler fileHandler) throws JSONException, FileNotFoundException, ParseException;
}
