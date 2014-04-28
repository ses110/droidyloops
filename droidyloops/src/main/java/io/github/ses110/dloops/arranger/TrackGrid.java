package io.github.ses110.dloops.arranger;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import io.github.ses110.dloops.R;

/**
 * Created by sergioescoto on 4/26/14.
 */
public class TrackGrid extends RelativeLayout {
    private static final String TAG = "TrackGrid";

    private int mMaxColumns = 12;
    private int mMaxRows = 6;
    TableLayout mTracks;

    ScrollView mScrollTracks;

    HorizontalScrollView mHorizontalScrollTracks;

    Context mContext;

    List<String> rowItems;

    public TrackGrid(Context context) {
        super(context);

        this.init(context);
    }

    public TrackGrid(Context context, AttributeSet attrs) {
        super(context,attrs);

        this.init(context);
    }
    public TrackGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);

        this.init(context);
    }

    private final void init(Context context) {
        this.mContext = context;
        this.rowItems = this.createEmptyCells(mContext, this.mMaxRows);
        this.initializeTables();
        this.generateRows();
    }
    private List<String> createEmptyCells(Context context, int rows) {
        List<String> cellList = new ArrayList<String>();
        for (int i = 1; i <= rows; i++) {

            String emptyCell = new String();

            //Save this. If adding more than 4 rows, use this:
            int channel = (((i-1) % 4) +1);
            emptyCell = "Channel " + channel;

            cellList.add(emptyCell);
        }
        return cellList;
    }


    private void initializeTables() {
        mTracks     = new TableLayout(this.mContext);
        mScrollTracks = new ScrollView(this.mContext);
        mHorizontalScrollTracks = new HorizontalScrollView(this.mContext);

        mTracks.setId(1);
        mScrollTracks.setId(2);
        mHorizontalScrollTracks.setId(3);

        mScrollTracks.addView(mHorizontalScrollTracks);
        mHorizontalScrollTracks.addView(mTracks);

        RelativeLayout.LayoutParams TrackParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.addView(this.mScrollTracks, TrackParams);
    }

    private void generateRows() {
        for (int i = 0; i < rowItems.size(); i++) {
            TableRow row_track = this.addNewRow(rowItems.get(i));
            row_track.setId(i+1);
            this.mTracks.addView(row_track);
        }
    }

    private TableRow addNewRow(String b) {
        TableRow rowForTracks = new TableRow(this.mContext);

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(0,0,0,0);
        params.weight = 1f;

        TextView label_track = new TextView(mContext);
        label_track.setBackgroundResource(R.drawable.track_label);
        label_track.setGravity(Gravity.CENTER);
        label_track.setText(b);

        rowForTracks.addView(label_track, params);

        for (int i = 0; i < mMaxColumns-1; i++) {
            ToggleButton duplicateButton = new ToggleButton(mContext);
            duplicateButton.setText("");
            duplicateButton.setBackgroundResource(R.drawable.empty_cell);

            rowForTracks.addView(duplicateButton, params);
        }
        return rowForTracks;
    }

}
