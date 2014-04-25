package io.github.ses110.dloops.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

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
            sampleList.add(object);
        }

        writeJSONDir(sampleList, FileType.SAMPLES);

        // Setup the other folders
        new File(context.getFilesDir(), "loops").mkdirs();
        new File(context.getFilesDir(), "songs").mkdirs();
    }

    public void writeJSONDir(JSONArray array, FileType fileType) throws IOException {
        File dir = getDir(fileType);
        File file;
        FileOutputStream out;

        file = new File(dir, "dir.json");
        out = new FileOutputStream(file);
        out.write(array.toString().getBytes());
        Log.v("Writing " + fileType.toString(), "Got the JSON" + array.toString());
        out.close();
    }

    public JSONObject readJSON(FileType fileType) throws FileNotFoundException, ParseException {
        File dir = getDir(fileType);
        File file;
        FileInputStream in;

        file = new File(dir, "dir.json");
        in = new FileInputStream(file);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new Scanner(in).next());
    }

    public JSONArray readJSONDir(FileType fileType) throws FileNotFoundException, ParseException {
        File dir = getDir(fileType);
        File file;
        FileInputStream in;

        file = new File(dir, "dir.json");
        in = new FileInputStream(file);
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(new Scanner(in).next());
    }

    public static JSONArray saveList(ArrayList<? extends Saveable> list) throws JSONException
    {
        JSONArray result = new JSONArray();

        for (Saveable s : list)
        {
            result.add(s.toJSON());
        }
        return result;
    }

    public void writeJSON(JSONObject object, FileType fileType) throws IOException
    {
        File dir = getDir(fileType);
        File file;
        FileOutputStream out;
        file = new File(dir, (object.get("name")) + ".json");
        out = new FileOutputStream(file);
        out.write(object.toString().getBytes());
        out.close();
    }

    public File getDir(FileType fileType)
    {
        File dir = null;
        switch (fileType)
        {
            case SAMPLES:
                dir = new File(context.getFilesDir(), "samples");
                break;
            case LOOPS:
                dir = new File(context.getFilesDir(), "loops");
                break;
            case SONGS:
                dir = new File(context.getFilesDir(), "songs");
                break;
        }

        dir.mkdirs();
        return dir;
    }
}
