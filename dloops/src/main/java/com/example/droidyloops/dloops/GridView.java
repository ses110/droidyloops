package com.example.droidyloops.dloops;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by sid9102 on 3/16/14.
 */


public class GridView extends SurfaceView implements SurfaceHolder.Callback
{

    public GridView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onDraw(Canvas canvas) {
        //do drawing stuff here.
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
