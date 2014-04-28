package io.github.ses110.dloops.arranger;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import io.github.ses110.dloops.R;

/**
 * Created by sergioescoto on 4/27/14.
 */
public class TrackView extends View {
    private static final String TAG = "TrackView";

    private Context mContext;

    // Length of a track, in cells
    private int mLength;

    // Channel it belongs to
    private int mChannel;

    //The length of the border radius in pixels
    private int mBorder = 1;

    private int width = 0 ,
                height = 0;

    private Rect mRect;
    private Rect mRectBorder;
    private Paint mPaintFill;
    private Paint mPaintStroke;
    private int length;


    public TrackView(Context context) {
        super(context);
        init(context);
    }

    public TrackView(Context context, TrackView tk) {
        super(context);
        this.setChannel(tk.getChannel());
        this.setLength(tk.getLength());
    }

    public TrackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Get the attributes from XML
        setFromXML(context, attrs);
        init(context);
    }

    public TrackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFromXML(context, attrs);
        init(context);
    }
    /**
     *  If this view is placed in an XML layout, you can its retrieve attributes from XML. (See res/values/attrs_trackview.xml)
     * @param context
     * @param attrs
     */
    private final void setFromXML(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TrackView, 0, 0);
        try {
            mLength = a.getInteger(R.styleable.TrackView_length, 1);
            mChannel = a.getInteger(R.styleable.TrackView_channel, 1);
            mBorder = a.getInteger(R.styleable.TrackView_border, 2);
        } finally {
            a.recycle();
        }
    }
    private final void init(Context context) {
        this.mContext = context;
        mRect = new Rect();
        mRectBorder = new Rect();
//        mRect = new Rect(10,10,mLength*100,100);
//        mRectBorder = new Rect(10+mBorder,10+mBorder, (mLength*100-mBorder), 100-mBorder);
//        mRect = new Rect(10,10,width,height);
//        mRectBorder = new Rect(10+mBorder,10+mBorder, (width-mBorder), height-mBorder);

        mPaintFill = new Paint();
        mPaintStroke = new Paint();
        mPaintStroke.setAntiAlias(true);

        //TODO: Refactor this. Decouple code dependency (the switch statements)
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

    public void setLength(int _length) {
        this.mLength = _length;
        invalidate();
        requestLayout();
    }
    public int getLength() {
        return this.mLength;
    }

    public void setChannel(int _channel) {
        this.mChannel = _channel;
        invalidate();
        requestLayout();
    }
    public int getChannel() {
        return this.mChannel;
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        width = xNew;
        height = yNew;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(width != 0 && height != 0) {
            mRect.set(0,0,width,height);
            mRectBorder.set(mBorder,mBorder, (width-mBorder), height-mBorder);
            Log.d(TAG, "canvas.getWidth:" + Integer.toString(width));
            Log.d(TAG, "canvas.getHeight:" + Integer.toString(height));
            canvas.drawRect(mRect, mPaintStroke);
            canvas.drawRect(mRectBorder, mPaintFill);
        }
//        getDrawingRect(mRectBorder);
//        mRectBorder.set(mRect.left+mBorder,mRect.top+mBorder, mRect.right-mBorder, mRect.bottom-mBorder);


    }
}
