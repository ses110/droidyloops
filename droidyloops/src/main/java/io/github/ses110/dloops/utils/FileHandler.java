package io.github.ses110.dloops.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.ses110.dloops.models.Sample;

/**
 * Handles all the file bullshit
 * Created by sid9102 on 4/16/2014.
 */
public class FileHandler
{

    private Context context;
    // This tells the picker whether to look at samples, loops, or songs.
    public enum FileType
    {
        SAMPLES, LOOPS, SONGS
    }

    public FileHandler(Context context)
    {
        this.context = context;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void firstTimeSetup(AssetManager am) throws IOException, JSONException {
        // Setup the samples directory
        File sampleDir = new File(context.getFilesDir(), "samples");
        sampleDir.mkdirs();
        String[] files = am.list("samples");
        InputStream in;
        OutputStream out;

        JSONArray sampleList = new JSONArray();

        // Copy the samples
        for (String fileName : files)
        {
            in = am.open("samples/" + fileName);
            File curSample = new File(sampleDir, fileName);
            out = new FileOutputStream(curSample);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            Sample sample = new Sample(fileName.split("\\.")[0], fileName);
            JSONObject object = sample.toJSON();
            sampleList.put(object);
        }

        out = new FileOutputStream(new File(sampleDir, "dir.json"));
        out.write(sampleList.toString().getBytes());
        Log.v("SETUP", "Got the JSON" + sampleList.toString());
        out.close();

        // Setup the other folders
        new File(context.getFilesDir(), "loops").mkdirs();
        new File(context.getFilesDir(), "songs").mkdirs();
    }

    public void writeJSON(JSONObject object, FileType fileType)
    {
        File dir;
        switch (fileType)
        {
            case SAMPLES:
                dir = context.getDir("samples", Context.MODE_PRIVATE);
                break;
            case LOOPS:
                dir = context.getDir("loops", Context.MODE_PRIVATE);
                break;
            case SONGS:
                dir = context.getDir("songs", Context.MODE_PRIVATE);
                break;
        }
    }
}
