package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class BasicView extends View {

    private Paint paint;

    private static final int LAYER_FLAGS =
            Canvas.MATRIX_SAVE_FLAG |
                    Canvas.CLIP_SAVE_FLAG |
                    Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                    Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                    Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    public BasicView(Context context) {
        this(context, null);
    }

    public BasicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawCircle(150, 150, 100, paint);

        //127,255,0
        canvas.saveLayerAlpha(0, 0, 400, 400, 0, LAYER_FLAGS);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawCircle(200, 200, 100, paint);
        canvas.restore();
    }
}
