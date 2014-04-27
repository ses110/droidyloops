package io.github.ses110.dloops.arranger;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergioescoto on 4/26/14.
 */
public class TrackGrid extends RelativeLayout {
    private static final String TAG = "TrackGrid";

    private int mMaxColumns = 32;
    private int mMaxRows = 12;
    TableLayout mTracks;
    TableLayout mTrackNames;

    ScrollView mScrollTracks;
    ScrollView mScrollTrackNames;

    HorizontalScrollView mHorizontalScrollTracks;

    Context mContext;

    List<TrackView> sampleItems;

    List<TrackView> SampleItems(Context context) {
        List<TrackView> trackList = new ArrayList<TrackView>();
        for (int i = 1; i <= mMaxRows; i++) {

            TrackView dummy_tk = new TrackView(context);

            //For now, if adding more than 4 rows, simply repeat adding them to channels 1-4, since only those channels have predetermined colors
            int channel = (((i-1) % 4) +1);
            dummy_tk.setChannel(channel);
            dummy_tk.setLength(1);
            dummy_tk.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            trackList.add(dummy_tk);
        }
        return trackList;
    }


    public TrackGrid(Context context) {
        super(context);

        this.mContext = context;
        this.init(this.mContext);
    }
    public TrackGrid(Context context, AttributeSet attrs) {
        super(context,attrs);

        this.mContext = context;
        this.init(this.mContext);
    }
    public TrackGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);

        this.mContext = context;
        this.init(this.mContext);
    }
    private final void init(Context context) {
        this.sampleItems = this.SampleItems(mContext);
        this.initializeTables();
        this.addToMainLayout();
        this.generateRows();
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
    }

    private void addToMainLayout() {
        RelativeLayout.LayoutParams TrackParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.addView(this.mScrollTracks, TrackParams);
    }

    private void generateRows() {
        for(TrackView b: this.sampleItems) {
            TableRow row_track = this.addRowToTracks(b);
            this.mTracks.addView(row_track);
        }
    }

    private TableRow addRowToTracks(TrackView b) {
        TableRow rowForTracks = new TableRow(this.mContext);
        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        TextView label_track = new TextView(mContext);
        label_track.setText("Channel " + b.getChannel());
        rowForTracks.addView(label_track, params);

        for (int i = 0; i < mMaxColumns-1; i++) {
            params.setMargins(2,2,0,0);
            Button btn = new Button(this.mContext);
            btn.setBackgroundColor(Color.WHITE);
            btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            TrackView newTk = new TrackView(this.mContext, b);
            rowForTracks.addView(btn, params);
        }
        return rowForTracks;
    }

}
