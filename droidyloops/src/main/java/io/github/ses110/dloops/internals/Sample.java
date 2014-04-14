package io.github.ses110.dloops.internals;

import android.content.res.AssetFileDescriptor;

import java.io.File;
import java.io.Serializable;

/**
 * Created by sid9102 on 4/14/2014.
 */
public class Sample
{
    private String name;
    private AssetFileDescriptor mAFD;
    private String fileName;
    public int spID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetFileDescriptor getmAFD() {
        return mAFD;
    }

    public void setmAFD(AssetFileDescriptor mAFD) {
        this.mAFD = mAFD;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
