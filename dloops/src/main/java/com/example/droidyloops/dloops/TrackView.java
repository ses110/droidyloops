package com.example.droidyloops.dloops;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * TODO: document your custom view class.
 */
public class TrackView extends SurfaceView implements SurfaceHolder.Callback {

    /* Constants */
    private final int maxChannels = 4;
    private static final String TAG = "TrackView";

    private float mStartDragX;
    private float mStartDragY;

    private int width,
                height;

    private double mLongestTrack_Width;

    private TrackThread mTrackThread;

    private Paint mLabelPaint;
    private Paint mTracksPaint;


    private Channel[] mChannels;

    private ScaleGestureDetector mScaleDetector;


    public TrackView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public TrackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public TrackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
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
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);

        //          This is dummy data for testing. [TODO: ERASE DUMMY DATA AFTER TESTING]
        mChannels = new Channel[maxChannels];

        mChannels[0] = new Channel("Drums");
        mChannels[1] = new Channel("Bass");
        mChannels[2] = new Channel("Guitar");
        mChannels[3] = new Channel("Vocals");

        //A track for each channel. Track's constructor argument is duration in milliseconds.
        Track track_1 = new Track(1000);
        Track track_2 = new Track(1500);
        Track track_3 = new Track(600);
        Track track_4 = new Track(1400);

        //TrackView will display track rectangles and scale according to the ratio between canvas width to the longest
        // track found in any channel.
        //So each track will be a width of : (a_track_duration / longest_track_duration) * canvas_width
        mLongestTrack_Width = 1500 * 1.5;

        mChannels[0].addTrack(track_1);
        mChannels[1].addTrack(track_2);
        mChannels[2].addTrack(track_3);
        mChannels[3].addTrack(track_4);






        mTrackThread = new TrackThread(getHolder(), this);
        mTrackThread.setRunning(true);
        mTrackThread.start();


        Log.d(TAG, "TrackView: surfaceCreated: getWidth = " + Integer.toString(getWidth()));
        mLabelPaint = new Paint();
        mLabelPaint.setStyle(Paint.Style.STROKE);
        mLabelPaint.setStrokeWidth(2.0f);
        mLabelPaint.setColor(getResources().getColor(R.color.label_bg));

        mTracksPaint = new Paint();
        mTracksPaint.setStyle(Paint.Style.FILL);
        mTracksPaint.setColor(getResources().getColor(R.color.track_bars));


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.d(TAG, "on surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while(retry) {
            try {
                mTrackThread.setRunning(false);
                mTrackThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {

            float labelHeight = (float) height / (maxChannels);
            float labelWidth = (float) width / 9;

            float x = event.getX();
            float y = event.getY();
            mStartDragX = x;
            mStartDragY = y;

            Log.d(TAG, "Detected screen press at x: " + Float.toString(x) + " y: " + Float.toString(y));

            if(x > labelWidth) {
                for(Channel c: mChannels) {
                    for(Track tk : c.getTracks()) {
                        if(tk.getRect().contains((int)x,(int)y)) {
                            Log.d(TAG, "Pressed a track on channel " + c.toString());
                        }
                    }
                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE)
        {

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

        float labelHeight = (float) height / (maxChannels);
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

        Typeface tf = Typeface.create("Helvetica",Typeface.BOLD);
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setStyle(Paint.Style.FILL);
        mLabelPaint.setTypeface(tf);

        //Update Track rectangles
        mLabelPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.channelLabel));
        for(int i = 0; i < maxChannels; i++) {
            Channel ch = mChannels[i];

            //  draw Channel's label text
            canvas.drawText(ch.toString(), labelWidth/12, (i+1)*(labelHeight)-(labelHeight/3), mLabelPaint);

            int leftMost = (int)labelWidth;

            //Redraw each track rectangle
            for(Track tk : ch.getTracks()) {
                double scaledWidth = ((double) tk.getDuration() / mLongestTrack_Width) * (width);

                mTracksPaint.setStyle(Paint.Style.FILL);
                mTracksPaint.setColor(getResources().getColor(R.color.track_bars));

                tk.setRect(leftMost, (int)(i*labelHeight), (int)scaledWidth, (int) ((i+1)*labelHeight));
                canvas.drawRect(tk.getRect(), mTracksPaint);
                
                mTracksPaint.setStyle(Paint.Style.STROKE);
                mTracksPaint.setColor(getResources().getColor(R.color.track_bars_edges));

                canvas.drawRect(tk.getRect(), mTracksPaint);
                
                leftMost += scaledWidth;
            }
        }

    }

    class TrackThread extends Thread {
        private static final String TAG = "TrackThread";


        private SurfaceHolder mSurfaceHolder;
        private TrackView mTrackView;
        private boolean mRun = false;

        public TrackThread(SurfaceHolder sH, TrackView view) {
            super();
            this.mSurfaceHolder = sH;
            this.mTrackView = view;
        }

        public void setRunning(boolean set) {
            this.mRun = set;
        }

        @Override
        public void run() {
            Canvas mCanvas;
            Log.d(TAG, "Starting Track Thread...");
            while(mRun) {
                mCanvas = null;
                try {
                    mCanvas = mSurfaceHolder.lockCanvas(null);
                    synchronized(mSurfaceHolder) {
                        //Insert methods to modify positions of items in onDraw()
                        postInvalidate();
                    }

                } finally {
                    if(mCanvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    }
                }
            }
        }

    }


}