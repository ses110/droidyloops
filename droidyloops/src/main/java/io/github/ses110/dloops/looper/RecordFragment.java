package io.github.ses110.dloops.looper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.ses110.dloops.R;

/**
 * Created by sid9102 on 4/29/2014.
 */
public class RecordFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
