package io.github.ses110.dloops.looper;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import io.github.ses110.dloops.R;
import io.github.ses110.dloops.internals.Loop;

/**
 * Created by sid9102 on 4/14/2014.
 */

public class LoopRowView extends LinearLayout
{
    public TextView sampleName;
    public ToggleButton[] cells;

    private Loop loop;
    int number;

    public LoopRowView(Context context) {
        super(context);
    }

    public LoopRowView(Context context, int number, Loop loop)
    {
        super(context);
        this.number = number;
        this.loop = loop;
    }

    public LoopRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoopRowView(Context context, String sampleName)
    {
        super(context);
        this.sampleName = new TextView(context);
        this.sampleName.setText(sampleName);
        cells = new ToggleButton[Loop.maxBeats];
        for(int i = 0; i < Loop.maxBeats; i++)
        {
            cells[i] = new ToggleButton(context);
            cells[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_style));
            final int j = i;
            cells[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    LoopRowView parent = (LoopRowView) v.getParent();
                    parent.handleTouch(j);
                }
            });
        }
    }

    public void handleTouch(int cell)
    {
        loop.touch(cell, number);
    }
}
