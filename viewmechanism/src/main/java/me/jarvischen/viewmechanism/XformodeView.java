package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class XformodeView extends View {

    private Bitmap bitmap;

    private Bitmap out;

    private Paint paint;

    public XformodeView(Context context) {
        this(context, null);
    }

    public XformodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XformodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        out = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), 160, 160, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(out, 0, 0, null);
    }
}
