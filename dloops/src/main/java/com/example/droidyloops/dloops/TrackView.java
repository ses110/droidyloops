package com.example.droidyloops.dloops;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * TODO: document your custom view class.
 */
public class TrackView extends SurfaceView implements SurfaceHolder.Callback {

    /* Constants */
    private final int maxChannels = 4;
    private static final String TAG = "TrackView";


    private int width,
                height;

    private TrackThread mTrackThread;

    private Paint channelPaint;

    private Channel[] mChannels;


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

        mTrackThread = new TrackThread(getHolder(), this);
        mTrackThread.setRunning(true);
        mTrackThread.start();


        Log.d(TAG, "TrackView: surfaceCreated: getWidth = " + Integer.toString(getWidth()));
        channelPaint = new Paint();
        channelPaint.setStyle(Paint.Style.STROKE);
        channelPaint.setStrokeWidth(2.0f);
        channelPaint.setColor(Color.WHITE);



        //This is dummy data for testing
        for(Channel ch: mChannels) {
            ch = new Channel();
        }
        //A track for each channel. Track's constructor argument is duration in milliseconds.
        Track track_1 = new Track(1000);
        Track track_2 = new Track(1500);
        Track track_3 = new Track(300);
        Track track_4 = new Track(1400);

        mChannels[0].addTrack(track_1);
        mChannels[1].addTrack(track_2);
        mChannels[2].addTrack(track_3);
        mChannels[3].addTrack(track_4);


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
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
            Log.d(TAG, "Detected screen press.");
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(width == 0 || height == 0) {
            width = canvas.getWidth();
            height = canvas.getHeight();
        }

        float trackNamesHeight = (float) height / (maxChannels + 2);
        float trackNamesWidth = (float) width / 9;

        canvas.drawColor(R.color.orangedark);

        channelPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, trackNamesWidth, height - trackNamesHeight, channelPaint);

        channelPaint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(2, trackNamesHeight, width - 1, height - trackNamesHeight, channelPaint);

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