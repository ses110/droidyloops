package io.github.ses110.dloops.looper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import io.github.ses110.dloops.R;
import io.github.ses110.dloops.models.Loop;
import io.github.ses110.dloops.models.Sample;

/**
 * Created by sid9102 on 4/14/2014.
 */

public class LoopRowView extends LinearLayout
{
    public TextView sampleName;
    public ToggleButton[] cells;

    private Loop loop;
    int number;

    public LoopRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoopRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public LoopRowView(Context context)
    {
        super(context);
        initView();
    }

    public void initView()
    {
        inflate(getContext(), R.layout.loop_row_view, this);
        sampleName = (TextView) findViewById(R.id.sampleName);
        cells = new ToggleButton[Loop.maxBeats];
        for(int i = 0; i < Loop.maxBeats; i++) {
            switch (i) {
                case 0:
                    cells[i] = (ToggleButton) findViewById(R.id.cell1);
                    break;
                case 1:
                    cells[i] = (ToggleButton) findViewById(R.id.cell2);
                    break;
                case 2:
                    cells[i] = (ToggleButton) findViewById(R.id.cell3);
                    break;
                case 3:
                    cells[i] = (ToggleButton) findViewById(R.id.cell4);
                    break;
                case 4:
                    cells[i] = (ToggleButton) findViewById(R.id.cell5);
                    break;
                case 5:
                    cells[i] = (ToggleButton) findViewById(R.id.cell6);
                    break;
                case 6:
                    cells[i] = (ToggleButton) findViewById(R.id.cell7);
                    break;
                case 7:
                    cells[i] = (ToggleButton) findViewById(R.id.cell8);
                    break;
                default:
                    break;
            }
            final int j = i;
            cells[i].setText("");
            cells[i].setTextOff("");
            cells[i].setTextOn("");
            cells[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoopRowView parent = (LoopRowView) v.getParent().getParent();
                    parent.handleTouch(j);
                }
            });
        }
    }

    public void setDetails(int number, Loop loop, Sample sample)
    {
        this.number = number;
        this.loop = loop;
        String name = sample.toString();
        if(name.length() > 7)
        {
            name = name.substring(0, 5) + "...";
        }
        sampleName.setText(name);
    }

    public void handleTouch(int cell)
    {
        loop.touch(cell, number);
        Log.v("found touch", Integer.toString(number) + ", " + Integer.toString(cell));
    }
}
