package com.example.droidyloops.dloops;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class TrackView extends View {
    private Rect mRect, mRectBorder;
    private Paint mPaintFill,
                  mPaintStroke;

    private int mBorder = 2;

    /* Constants */
    private static final String TAG = "TrackView";

    private int width,
                height;

    private int mLength,
                mChannel;

    public TrackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.TrackView, 0, 0);

        try {
            mLength = a.getInteger(R.styleable.TrackView_length, 1);
            mChannel = a.getInteger(R.styleable.TrackView_channel, 1);
            mBorder = a.getInteger(R.styleable.TrackView_border, 3);
        } finally {
            a.recycle();
        }

        init();
    }
    private final void init() {
        mRect = new Rect(10,10,mLength*100,100);

        mRectBorder = new Rect(10+mBorder,10+mBorder, (mLength*100-mBorder),100-mBorder);

        mPaintFill = new Paint();
        mPaintStroke = new Paint();
        mPaintStroke.setAntiAlias(true);
        switch(mChannel) {
            case 1:
                mPaintFill.setColor(getResources().getColor(R.color.track_blue));
                mPaintStroke.setColor(getResources().getColor(R.color.track_blue_border));
                break;
            case 2:
                mPaintFill.setColor(getResources().getColor(R.color.track_green));
                mPaintStroke.setColor(getResources().getColor(R.color.track_green_border));
                break;
            case 3:
                mPaintFill.setColor(getResources().getColor(R.color.track_orange));
                mPaintStroke.setColor(getResources().getColor(R.color.track_orange_border));
                break;
            case 4:
                mPaintFill.setColor(getResources().getColor(R.color.track_red));
                mPaintStroke.setColor(getResources().getColor(R.color.track_red_border));
                break;
        }

    }

    public void setLength(int newLength) {
        mLength = newLength;
        invalidate();
        requestLayout();
    }
    public void setChannel(int newChannel) {
        mChannel = newChannel;
        invalidate();
        requestLayout();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int desiredWidth = 100;
//        int desiredHeight = 100;
//
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        int width;
//        int height;
//
//        //Measure Width
//        if (widthMode == MeasureSpec.EXACTLY) {
//            //Must be this size
//            width = widthSize;
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            //Can't be bigger than...
//            width = Math.min(desiredWidth, widthSize);
//        } else {
//            //Be whatever you want
//            width = desiredWidth;
//        }
//
//        //Measure Height
//        if (heightMode == MeasureSpec.EXACTLY) {
//            //Must be this size
//            height = heightSize;
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            //Can't be bigger than...
//            height = Math.min(desiredHeight, heightSize);
//        } else {
//            //Be whatever you want
//            height = desiredHeight;
//        }
//        setMeasuredDimension(width, height);
//    }


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
        //getDrawingRect(mRectBorder);
        //mRectBorder.set(mRect.left+border,mRect.top+border, mRect.right-border, mRect.bottom-border);

        canvas.drawRect(mRect, mPaintStroke);
        canvas.drawRect(mRectBorder, mPaintFill);

    }

}