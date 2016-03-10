package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by chenfuduo on 2016/3/10.
 */
public class MyMathCurveWithSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = MyMathCurveWithSurfaceView.class.getSimpleName();
    private SurfaceHolder holder;
    private Thread myThread;
    private Canvas mCanvas;
    private boolean isRunning;
    private float ax, ay, bx, by, angleA, angleB, speedA, speedB, axR, ayR, bxR, byR;
    private Paint mPaint;


    public MyMathCurveWithSurfaceView(Context context) {
        this(context, null);
    }

    public MyMathCurveWithSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyMathCurveWithSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint();

        mPaint.setStrokeWidth((float) 2.0);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        speedA = (float) 0.012;
        speedB = (float) 0.078;
        axR = 120;
        ayR = 200;
        bxR = 200;
        byR = 320;


        holder = this.getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        myThread = new Thread(this);
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            //录视频需要,睡3秒吧
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        try {
            mCanvas = holder.lockCanvas();
            if (mCanvas != null) {
                angleA += speedA;
                angleB += speedB;
                ax = (float) (Math.cos(angleA) * axR);
                ay = (float) (Math.sin(angleA) * ayR);
                bx = (float) (Math.cos(angleB) * bxR);
                by = (float) (Math.sin(angleB) * byR);


                Log.e(TAG,"ax:" + ax);
                Log.e(TAG,"ay:" + ay);
                Log.e(TAG,"bx:" + bx);
                Log.e(TAG, "by:" + by);
                //mPaint.setColor(Color.WHITE);
                // mCanvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);


               /* int[] colorsArr = new int[]{R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark};

                Random random = new Random();
                int colorIndex = random.nextInt(colorsArr.length-1);*/

                mPaint.setColor(getResources().getColor(R.color.colorAccent));
                mCanvas.drawLine(ax+350, ay+350, bx+350, by+350, mPaint);

                invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                holder.unlockCanvasAndPost(mCanvas);
            }
        }
    }


}