package com.example.droidyloops.dloops;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * TODO: document your custom view class.
 */
public class TrackView extends SurfaceView implements SurfaceHolder.Callback {

    /* Constants */
    private final int maxChannels = 4;


    private int width,
                height;

    private TrackThread mTrackThread;

    private Paint channelPaint;

    Channel[] mChannels;

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
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(true);

        mTrackThread = new TrackThread(getHolder(), this);
        mTrackThread.setRunning(true);
        mTrackThread.start();

        channelPaint = new Paint();
        channelPaint.setStyle(Paint.Style.STROKE);
        channelPaint.setStrokeWidth(2.0f);
        channelPaint.setColor(Color.WHITE);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            mTrackThread.setRunning(false);
            mTrackThread.join();
        } catch (InterruptedException e) {}
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(width == 0 || height == 0) {
            width = canvas.getWidth();
            height = canvas.getHeight();
        }

        float trackNamesHeight = (float) height / (maxChannels + 2);
        float trackNamesWidth = (float) width / 9;

        canvas.drawColor(0xFC0099CC);
//        canvas.drawColor(R.color.track_bg);

        channelPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, trackNamesHeight, trackNamesWidth, height - trackNamesHeight, channelPaint);

    }

    class TrackThread extends Thread {
        private SurfaceHolder mSurfaceHolder;
        private TrackView mTrackView;
        private boolean mRun = false;

        public TrackThread(SurfaceHolder sH, TrackView view) {
            mSurfaceHolder = sH;
            mTrackView = view;
        }

        public void setRunning(boolean run) {
            mRun = run;
        }

        @Override
        public void run() {
            Canvas mCanvas;
            while(mRun) {
                mCanvas = null;
                try {
                    mCanvas = mSurfaceHolder.lockCanvas(null);
                    synchronized(mSurfaceHolder) {
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