package io.github.ses110.dloops.models;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sid9102 on 4/17/2014.
 *
 * Enforces common methods that should be implemented by saveable things like loops, samples, and songs
 */
public interface Saveable
{
    public JSONObject toJSON() throws JSONException;
    public String toString();
}
