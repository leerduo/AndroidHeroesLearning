package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by chenfuduo on 2016/3/10.
 */
public class MySurfaceView  extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private SurfaceHolder holder;

    private Canvas canvas;

    private boolean isDrawing;

    private Path path;

    private Paint paint;

    private int x,y;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        path.moveTo(0,400);
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        while (isDrawing){
            draw();
            x+=1;
            y = (int) (100*Math.sin(x*2*Math.PI/180) + 400);
            path.lineTo(x,y);
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(path,paint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null){
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
