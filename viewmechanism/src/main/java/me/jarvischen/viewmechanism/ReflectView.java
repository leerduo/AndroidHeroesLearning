package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class ReflectView extends View {

    private Bitmap srcBitmap;
    private Bitmap refBitmap;

    private Paint paint;

    private PorterDuffXfermode xfermode;

    public ReflectView(Context context) {
        this(context, null);
    }

    public ReflectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReflectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        Matrix matrix = new Matrix();
        matrix.setScale(1F, -1F);
        refBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);

        paint = new Paint();
        paint.setShader(new LinearGradient(0, srcBitmap.getHeight(), 0,
                srcBitmap.getHeight() + srcBitmap.getHeight() / 4,
                0XDD000000, 0X10000000, Shader.TileMode.CLAMP));
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.drawBitmap(refBitmap, 0, srcBitmap.getHeight(), null);
        paint.setXfermode(xfermode);
        canvas.drawRect(0, srcBitmap.getHeight(), refBitmap.getWidth(), srcBitmap.getHeight()*2, paint);
        paint.setXfermode(null);
    }
}
