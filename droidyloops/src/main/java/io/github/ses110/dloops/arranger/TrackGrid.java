package io.github.ses110.dloops.arranger;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import io.github.ses110.dloops.MainActivity;
import io.github.ses110.dloops.R;

/**
 * Created by sergioescoto on 4/26/14.
 */
public class TrackGrid extends RelativeLayout {
    private static final String TAG = "TrackGrid";

    private int mColSpan = 0;
    private int mRowSpan = 0;

    private static final int mColLimit = 100;
    TableLayout mTableTracks;

    TwoDScrollView m2DScrollView;

//    ScrollView mScrollTracks;
//    HorizontalScrollView mHorizontalScrollTracks;

    Context mContext;


    private final int mColsOnScreen = 8;

    private TableRow.LayoutParams mCellParams;

    private TableRow.LayoutParams mLabelParams;

    private TableRow.LayoutParams mRowParams;

    public TrackGrid(Context context) {
        super(context);
        if(!this.isInEditMode())
            this.init(context);
    }

    public TrackGrid(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.setFromXML(context, attrs);
        if(!this.isInEditMode())
            this.init(context);
    }
    public TrackGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setFromXML(context, attrs);
        if(!this.isInEditMode())
            this.init(context);
    }

    // Obtain row/col information from XML attributes
    private final void setFromXML(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TrackGrid, 0, 0);
        try {
            mColSpan = a.getInteger(R.styleable.TrackGrid_columns, 10);
            mRowSpan = a.getInteger(R.styleable.TrackGrid_rows, 1);
        } finally {
            a.recycle();
        }
    }
    public int getColSpan() {
        return mColSpan;
    }


    private final void init(Context context) {
        if(mColSpan == 0) mColSpan = 10;
        if(mRowSpan == 0) mRowSpan = 1;

        this.mContext = context;

        //Change this to views, emptyCells
//        this.setOnDragListener(this);
        List<String> rowItems = this.createCellList(mContext, mRowSpan);

        // Get the screen's width. We want to make sure at least 8 columns show up on screen. Add +1 to compensate for track labels
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int cellSize = dm.widthPixels;
        cellSize = cellSize / (mColsOnScreen+1);


        mCellParams = new TableRow.LayoutParams(cellSize, cellSize);
        mLabelParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRowParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);

        this.setupTables();

        this.generateRows(rowItems);

    }
    private List<String> createCellList(Context context, int rows) {
        //There are only 4 color styles set up for channels 1 to 4
        int maxRowColors = 4;

        List<String> cellList = new ArrayList<String>();
        for (int i = 1; i <= rows; i++) {
            cellList.add(new String("Track " + i));
        }
        return cellList;
    }


    private void setupTables() {
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTableTracks = new TableLayout(this.mContext);
        mTableTracks.setLayoutParams(tableParams);

        m2DScrollView = new TwoDScrollView(mContext);
        m2DScrollView.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_LOW);
//        mScrollTracks = new ScrollView(this.mContext);
//        mHorizontalScrollTracks = new HorizontalScrollView(this.mContext);

        mTableTracks.setId(1);
        m2DScrollView.setId(2);
//        mScrollTracks.setId(2);
//        mHorizontalScrollTracks.setId(3);

        m2DScrollView.addView(mTableTracks);
//        mScrollTracks.addView(mHorizontalScrollTracks);
//        mHorizontalScrollTracks.addView(mTableTracks);

//        RelativeLayout.LayoutParams TrackParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams TrackParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.addView(this.m2DScrollView, TrackParams);
    }

    private void generateRows(List<String> rowItems) {
        for (int i = 0; i < rowItems.size(); i++) {

            // Make sure row IDs are multiples of 100. So 1st row will be 100, row 2 will be 200, and so on.
            // mColLimit default is set to 100, an upper bound on the number of columns.
            // The issue is when setting IDs of the elements inside a row, if we go over mColLimit we lose those clean ids for each row
            int rowId = (mColLimit - mColSpan % mColLimit) + mColSpan;

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

        for (int i = 0; i < mColSpan -1; i++) {
            rowForTracks.addView(addEmptyCell(id+i+2));
        }
        this.mRowSpan++;
        return rowForTracks;
    }

    private EditText addLabel(String label, int id) {
        EditText trackLabel = new EditText(mContext);
        trackLabel.setId(id);
        trackLabel.setLayoutParams(mLabelParams);
        trackLabel.setPadding(10, 10, 10, 10);
        trackLabel.setImeOptions(EditorInfo.IME_ACTION_DONE);
        trackLabel.setCursorVisible(false);
        trackLabel.setBackground(getResources().getDrawable(R.drawable.track_label));
        trackLabel.setGravity(Gravity.CENTER);
        trackLabel.setText(label);
        trackLabel.setOnEditorActionListener(((MainActivity) this.mContext).arranger);

        return trackLabel;
    }


    private ImageView addEmptyCell(final int id) {
        ImageView emptyCell = new ImageView(mContext);
        emptyCell.setId(id);
        emptyCell.setLayoutParams(mCellParams);
        emptyCell.setBackground(getResources().getDrawable(R.drawable.empty_cell));

        /*
        *   Setup cell event listeners
        *
        * */

//        emptyCell.setOnTouchListener((OnTouchListener) mContext);
        emptyCell.setOnLongClickListener(((MainActivity) this.mContext).arranger);
        emptyCell.setOnDragListener(((MainActivity) this.mContext).arranger);

        return emptyCell;
    }

    public TableRow getParentRow(View view) {
        TableRow parentRow = (TableRow)view.getParent();
        return parentRow;
    }

    // Table's cells are numbered differently, normalize them to use with a Channel
    public int getCellIdForChannel(View view) {
        int id = ((view.getId()-2) % mColSpan);
        return id;
    }

    public TableRow createNewRow() {
        // Get the last row's id
        TableRow lastRow = (TableRow) mTableTracks.getChildAt(mTableTracks.getChildCount()-1);
        Log.v(TAG, "Last row ID is: " + lastRow.getId());

        int rowId = (mColLimit - mColSpan % mColLimit) + mColSpan;

        rowId = lastRow.getId() + rowId;
        TableRow newRow = this.addNewRow("New Track", rowId);
        this.mTableTracks.addView(newRow, mRowParams);
        return newRow;
    }

    public void setHighlightColumn(int offset) {
        if(offset >= 0 && offset < mColSpan) {
            Log.v("TrackGrid", "In setHighlight given index " + offset);
            int fixed_offset = offset + 2;
            for (int i = 0; i < mTableTracks.getChildCount(); i++) {
                TableRow iRow = (TableRow) mTableTracks.getChildAt(i);
                fixed_offset = iRow.getId() + offset + 2;
                View iCol = findViewById(fixed_offset);
                if(iCol instanceof TrackView)
                    ((TrackView)iCol).setHighlight(true);

            }
        }
    }

    public void removeHighlightColumn(int offset) {
        if(offset >= 0 && offset < mColSpan) {
            Log.v("TrackGrid", "In removeHighlight given index " + offset);
            int fixed_offset = offset + 2;
            for (int i = 0; i < mTableTracks.getChildCount(); i++) {
                TableRow iRow = (TableRow) mTableTracks.getChildAt(i);
                fixed_offset = iRow.getId() + offset + 2;
                View iCol = findViewById(fixed_offset);
                if(iCol instanceof TrackView)
                    ((TrackView)iCol).setHighlight(false);
            }
        }
    }

    public void createLoopCell(View view) {
        if(view != null) {
            Log.d("CELL", "Pressed on cell " + view.getId());
            ViewGroup parent = (ViewGroup) view.getParent();
            int index = parent.indexOfChild(view);

            ViewGroup.LayoutParams saveParams = view.getLayoutParams();
            parent.removeView(view);

            TrackView c = new TrackView(view.getContext());
            c.setId(view.getId());
            c.setChannel((1 + (((parent.getId() / 100) - 1) % 4)));
            Log.d("SetChannel", " to " + (1 + (((parent.getId() / 100) - 1) % 4)));
            c.invalidate();
            c.setOnClickListener(((MainActivity) this.mContext).arranger);
            c.setOnDragListener(((MainActivity) this.mContext).arranger);
            c.setOnLongClickListener(((MainActivity) this.mContext).arranger);
            c.setLayoutParams(saveParams);


            parent.addView(c, index);
        }
    }

    /*
    *  Return a list of all the TableRow
    * */
    public ArrayList<TableRow> returnRows() {
        ArrayList<TableRow> listRows = new ArrayList<TableRow>();

        int numRows = mTableTracks.getChildCount();

        for (int i = 0; i < numRows; i++) {
            listRows.add((TableRow) mTableTracks.getChildAt(i));
        }
        return listRows;
    }

    public void swapCells(View originalCell, View dropOnCell) {
        if(originalCell instanceof TrackView && dropOnCell instanceof ImageView) {
            Log.v("TrackGrid", "Swapping cell id: " + originalCell.getId() + " with id: " + dropOnCell.getId());

            ViewGroup.LayoutParams originalParams   = originalCell.getLayoutParams();
            ViewGroup.LayoutParams dropOnParams       = dropOnCell.getLayoutParams();

            TableRow originalRow    = (TableRow) originalCell.getParent();
            TableRow dropRow        = (TableRow) dropOnCell.getParent();

            int originalViewIndex = originalRow.indexOfChild(originalCell);
            int dropViewIndex = dropRow.indexOfChild(dropOnCell);

            Log.v("TrackGrid", "OriginalIndex: " + Integer.toString(originalViewIndex));
            Log.v("TrackGrid", "dropViewIndex: " + Integer.toString(dropViewIndex));

            int originalId = originalCell.getId();
            int dropId = dropOnCell.getId();

            originalCell.setLayoutParams(dropOnParams);
            dropOnCell.setLayoutParams(originalParams);

            originalCell.setId(dropId);
            dropOnCell.setId(originalId);

            originalRow.removeView(originalCell);
            dropRow.removeView(dropOnCell);

            originalRow.addView(dropOnCell, originalViewIndex);
            dropRow.addView(originalCell, dropViewIndex);
        }
    }

    public void copyCells(View originalCell, View dropOnCell) {
        if(originalCell instanceof TrackView && dropOnCell instanceof ImageView) {
            ViewGroup.LayoutParams originalParams   = originalCell.getLayoutParams();
            ViewGroup.LayoutParams dropOnParams       = dropOnCell.getLayoutParams();

            TableRow originalRow    = (TableRow) originalCell.getParent();
            TableRow dropRow        = (TableRow) dropOnCell.getParent();

            int originalViewIndex = originalRow.indexOfChild(originalCell);
            int dropViewIndex = dropRow.indexOfChild(dropOnCell);

            int originalId = originalCell.getId();
            int dropId = dropOnCell.getId();

//            originalCell.setLayoutParams(dropOnParams);
            dropOnCell.setLayoutParams(originalParams);

            originalCell.setId(dropId);
            dropOnCell.setId(originalId);

            originalRow.removeView(originalCell);
            dropRow.removeView(dropOnCell);

            originalRow.addView(dropOnCell, originalViewIndex);
            dropRow.addView(originalCell, dropViewIndex);

        }
    }

//
//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//        int action = event.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_MOVE:
//                Log.v("View Press", "MOVED");
//                break;
//            case MotionEvent.ACTION_DOWN:
//                Log.v("View Press", "DOWN");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.v("View Press", "UP!");
//                break;
//        }
//        return false;
//    }

}
