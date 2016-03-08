package me.jarvischen.customview_04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/3.
 */
public class MyView extends View {

    private Paint paint;

    private int rectCount = 12;

    private int width;

    private int rectHeight;

    private int randomRectHeight;

    private int rectWidth;

    private int offset = 10;

    private LinearGradient linearGradient;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        rectHeight = getHeight();
        rectWidth = (int) (width * 0.6 / rectCount);
        linearGradient = new LinearGradient(
                0,
                0,
                rectWidth,
                rectHeight,
                Color.YELLOW,
                Color.BLUE,
                Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < rectCount; i++) {
            double random = Math.random();
            randomRectHeight = (int) (random * rectHeight);
            canvas.drawRect(width / 8 + rectWidth * i + offset,
                    randomRectHeight,width / 8 + rectWidth * i  + rectWidth,
                    rectHeight,paint);
        }
        postInvalidateDelayed(300);
    }
}
