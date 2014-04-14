package io.github.ses110.dloops.internals;

import org.json.JSONObject;

/**
 * Created by sid9102 on 4/14/2014.
 */
public class Sample
{
    private String name;
    private String path;
    private int spID;

    public Sample(String name, String path)
    {
        this.name = name;
        this.path = path;
    }

    public int getSpID() {
        return spID;
    }

    public void setSpID(int spID) {
        this.spID = spID;
    }

    public String getName() {
        return name;
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

    // TODO: implement saving and loading
    public JSONObject saveSample()
    {
        return null;
    }
}
