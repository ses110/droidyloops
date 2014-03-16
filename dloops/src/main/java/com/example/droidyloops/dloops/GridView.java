package com.example.droidyloops.dloops;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by sid9102 on 3/16/14.
 */


public class GridView extends SurfaceView implements SurfaceHolder.Callback
{
    PanelThread panelThread;
    int height;
    int width;

    public GridView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()

        panelThread = new PanelThread(getHolder(), this); //Start the thread that
        panelThread.setRunning(true);                     //will make calls to
        panelThread.start();                              //onDraw()
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            panelThread.setRunning(false);                //Tells thread to stop
            panelThread.join();                           //Removes thread from mem.
        } catch (InterruptedException e) {}
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(width == 0 || height == 0)
        {
            width = canvas.getWidth();
            height = canvas.getHeight();
            Log.v("width", Integer.toString(width));
            Log.v("height", Integer.toString(height));
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        // make the entire canvas white
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        // another way to do this is to use: // canvas.drawColor(Color.WHITE);
        // draw a solid blue circle
        paint.setColor(Color.BLUE);
        canvas.drawCircle(20, 20, 15, paint);
        // draw blue circle with antialiasing turned on
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(60, 20, 15, paint);
    }

    class PanelThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private GridView mGridView;
        private boolean _run = false;


        public PanelThread(SurfaceHolder surfaceHolder, GridView view) {
            _surfaceHolder = surfaceHolder;
            mGridView = view;
        }


        public void setRunning(boolean run) { //Allow us to stop the thread
            _run = run;
        }


        @Override
        public void run() {
            Canvas c;
            while (_run) {     //When setRunning(false) occurs, _run is
                c = null;      //set to false and loop ends, stopping thread


                try {


                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {


                        //Insert methods to modify positions of items in onDraw()
                        postInvalidate();


                    }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
