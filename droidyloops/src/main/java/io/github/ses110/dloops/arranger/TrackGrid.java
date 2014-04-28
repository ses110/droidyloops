package io.github.ses110.dloops.arranger;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import io.github.ses110.dloops.R;

/**
 * Created by sergioescoto on 4/26/14.
 */
public class TrackGrid extends RelativeLayout {
    private static final String TAG = "TrackGrid";

    private int mMaxColumns = 26;
    private int mMaxRows = 6;
    TableLayout mTableTracks;

    ScrollView mScrollTracks;

    HorizontalScrollView mHorizontalScrollTracks;

    Context mContext;

    private final int mColsOnScreen = 8;

    private TableRow.LayoutParams mCellParams;

    private TableRow.LayoutParams mLabelParams;

    private TableRow.LayoutParams mRowParams;


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

        List<String> mRowItems = this.createCellList(mContext, mMaxRows);


        // Get the screen's width. We want to make sure at least 8 columns show up on screen. Add +1 to compensate for labels
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int cellSize = dm.widthPixels;
        cellSize = cellSize / (mColsOnScreen+1);


        mCellParams = new TableRow.LayoutParams(cellSize, cellSize);
        mLabelParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRowParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);

        this.initializeTables();

        this.generateRows(mRowItems);

    }
    private List<String> createCellList(Context context, int rows) {
        List<String> cellList = new ArrayList<String>();
        for (int i = 1; i <= rows; i++) {

            String emptyCell;

            //Save this. If adding more than 4 rows, use this:
            int channel = (((i-1) % 4) +1);
            emptyCell = "Channel " + channel;

            cellList.add(emptyCell);
        }
        return cellList;
    }


    private void initializeTables() {
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTableTracks = new TableLayout(this.mContext);
        mTableTracks.setLayoutParams(tableParams);

        mScrollTracks = new ScrollView(this.mContext);
        mHorizontalScrollTracks = new HorizontalScrollView(this.mContext);

        mTableTracks.setId(1);
        mScrollTracks.setId(2);
        mHorizontalScrollTracks.setId(3);

        mScrollTracks.addView(mHorizontalScrollTracks);
        mHorizontalScrollTracks.addView(mTableTracks);

        RelativeLayout.LayoutParams TrackParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.addView(this.mScrollTracks, TrackParams);
    }

    private void generateRows(List<String> rowItems) {
        for (int i = 0; i < rowItems.size(); i++) {
            TableRow row_track = this.addNewRow(rowItems.get(i), i+1);
            this.mTableTracks.addView(row_track);
        }
    }

    private TableRow addNewRow(String channelName, int id) {
        TableRow rowForTracks = new TableRow(this.mContext);
        rowForTracks.setId(id);
        rowForTracks.setLayoutParams(mRowParams);

        rowForTracks.addView(addLabel(channelName, id));

        for (int i = 0; i < mMaxColumns-1; i++) {
            rowForTracks.addView(addEmptyCell(i + 1));
        }
        return rowForTracks;
    }

    private EditText addLabel(String label, int id) {
        EditText trackLabel = new EditText(mContext);
        trackLabel.setId(id);
        trackLabel.setLayoutParams(mLabelParams);
        trackLabel.setImeOptions(EditorInfo.IME_ACTION_DONE);
        trackLabel.setCursorVisible(false);
        trackLabel.setBackground(getResources().getDrawable(R.drawable.track_label));
        trackLabel.setGravity(Gravity.CENTER);
        trackLabel.setText(label);
        return trackLabel;
    }

    private ImageView addEmptyCell(int id) {
        ImageView emptyCell = new ImageView(mContext);
        emptyCell.setId(id);
        emptyCell.setLayoutParams(mCellParams);
        emptyCell.setBackground(getResources().getDrawable(R.drawable.empty_cell));
        return emptyCell;
    }

    @Override
    protected void onAttachedToWindow() {

    }
}
