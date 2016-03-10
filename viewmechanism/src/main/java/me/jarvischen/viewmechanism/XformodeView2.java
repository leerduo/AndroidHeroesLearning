package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 * 刮刮乐
 */
public class XformodeView2 extends View {

    private Paint paint;

    private Path path;

    private Bitmap bgBitmap;

    private Bitmap fgBitmap;

    private Canvas canvas;


    public XformodeView2(Context context) {
        this(context, null);
    }

    public XformodeView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XformodeView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        path = new Path();

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        fgBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);


        canvas = new Canvas(fgBitmap);
        canvas.drawColor(Color.GRAY);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(fgBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
        }
        canvas.drawPath(path, paint);
        invalidate();
        return true;
    }
}
