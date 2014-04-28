package io.github.ses110.dloops.arranger;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.View.OnDragListener;
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
public class TrackGrid extends RelativeLayout implements OnTouchListener, OnLongClickListener, OnDragListener {
    private static final String TAG = "TrackGrid";

    private int mMaxColumns = 26;
    private int mMaxRows = 6;

    private static final int mColLimit = 100;
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

        this.setOnDragListener(this);

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
        //There are only 4 color styles set up for channels 1 to 4
        int maxRowColors = 4;

        List<String> cellList = new ArrayList<String>();
        for (int i = 1; i <= rows; i++) {

            String emptyCell;

            int channel = (((i-1) % maxRowColors) +1);

            emptyCell = "Track " + channel;

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
            int rowId = (mColLimit - mMaxColumns % mColLimit) + mMaxColumns;
            Log.v("GenerateID", "From mMaxColumns: " + mMaxColumns + " to rowId: " + rowId);
            rowId = rowId * (i+1);

            TableRow row_track = this.addNewRow(rowItems.get(i), rowId);
            this.mTableTracks.addView(row_track);
        }
    }

    private TableRow addNewRow(String channelName, int id) {
        TableRow rowForTracks = new TableRow(this.mContext);
        rowForTracks.setId(id);
        rowForTracks.setLayoutParams(mRowParams);

        rowForTracks.addView(addLabel(channelName, id + 1));

        for (int i = 0; i < mMaxColumns-1; i++) {
            rowForTracks.addView(addEmptyCell(id+i+2));
        }
        return rowForTracks;
    }

    private EditText addLabel(String label, int id) {
        EditText trackLabel = new EditText(mContext);
        trackLabel.setId(id);
        trackLabel.setLayoutParams(mLabelParams);
        trackLabel.setPadding(10,10,10,10);
        trackLabel.setImeOptions(EditorInfo.IME_ACTION_DONE);
        trackLabel.setCursorVisible(false);
        trackLabel.setBackground(getResources().getDrawable(R.drawable.track_label));
        trackLabel.setGravity(Gravity.CENTER);
        trackLabel.setText(label);
        return trackLabel;
    }

    private ImageView addEmptyCell(final int id) {
        ImageView emptyCell = new ImageView(mContext);
        emptyCell.setId(id);
        emptyCell.setLayoutParams(mCellParams);
        emptyCell.setBackground(getResources().getDrawable(R.drawable.empty_cell));

        emptyCell.setOnTouchListener(this);
        emptyCell.setOnLongClickListener(this);

        return emptyCell;
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d("CELL","Pressed on cell " + view.getId());
        ViewGroup parent = (ViewGroup) view.getParent();
        Log.d("CELL parent", "Cell parent id: " + parent.getId());
        int index = parent.indexOfChild(view);
        ViewGroup.LayoutParams saveParams = view.getLayoutParams();
        parent.removeView(view);

        TrackView c = new TrackView(getContext());
        c.setId(view.getId());
        c.setChannel((1+(((parent.getId() / 100)-1) % 4)));
        Log.d("SetChannel", " to " + (1+(((parent.getId() / 100)-1) % 4)));
        c.invalidate();
        c.setLayoutParams(saveParams);

        parent.addView(c, index);
        return true;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                Log.v("View Press", "MOVED");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.v("View Press", "DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.v("View Press", "UP!");
                break;
        }
        return false;
    }

    @Override
    public boolean onDrag(View view, DragEvent Event) {
        switch (Event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.v("DragDrop","Drag Started");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.v("DragDrop","Drag ENTERED");
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.v("DragDrop","Drag EXITED");
                break;
            case DragEvent.ACTION_DROP:
                Log.v("DragDrop","Drag ACTION DROP");
                break;
        }
        return true;
    }
}
