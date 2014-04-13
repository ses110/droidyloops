package com.example.droidyloops.dloops;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class TrackView extends View {

    /* Constants */
    private static final String TAG = "TrackView";

    private int width,
                height;

    private Rect drawingRect_;

    private Paint mLabelPaint;
    private Paint mTracksPaint;
    private Typeface mTypeFace;



    public TrackView(Context context) {
        super(context);

        this.instantiate();
    }

    public TrackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.instantiate();
    }

    public TrackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.instantiate();
    }
    private final void instantiate() {

        //A track for each channel. Track's constructor argument is duration in milliseconds.
//        Track track_1 = new Track(1000);
//        Track track_2 = new Track(1500);
//        Track track_3 = new Track(600);
//        Track track_4 = new Track(1400);

        //TrackView will display track rectangles and scale according to the ratio between canvas width to the longest
        // track found in any channel.
        //So each track will be a width of : (a_track_duration / longest_track_duration) * canvas_width
//        mLongestTrack_Width = 1500 * 1.5;


        setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 400;
        int desiredHeight = 325;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float labelHeight = (float) height;
                float labelWidth = (float) width / 9;

                Log.d(TAG, "Detected screen press at x: " + Float.toString(x) + " y: " + Float.toString(y));


                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(width == 0 || height == 0) {
            width = canvas.getWidth();
            height = canvas.getHeight();
            Log.d(TAG, "canvas.getWidth:" + Integer.toString(width));
            Log.d(TAG, "canvas.getHeight:" + Integer.toString(height));
        }

        float labelHeight = (float) height;
        float labelWidth = (float) width / 9;

        //Set background color of background where track rectangles will be placed.
        canvas.drawColor(getResources().getColor(R.color.track_bg));

        //Set background color of channel labels
        mLabelPaint.setColor(getResources().getColor(R.color.label_bg));
        mLabelPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, labelWidth, height, mLabelPaint);

        //Draw dividing lines between channels
        mLabelPaint.setStyle(Paint.Style.STROKE);
        mLabelPaint.setColor(Color.BLACK);

        // X, Y, X2, Y2
        canvas.drawLine(0,labelHeight, width, labelHeight, mLabelPaint);
        canvas.drawLine(0,labelHeight*2, width, labelHeight*2, mLabelPaint);
        canvas.drawLine(0,labelHeight*3, width, labelHeight*3, mLabelPaint);

        mTypeFace = Typeface.create("Helvetica",Typeface.BOLD);
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setStyle(Paint.Style.FILL);
        mLabelPaint.setTypeface(mTypeFace);

        //Update Track rectangles
        mLabelPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.channelLabel));

    }

}