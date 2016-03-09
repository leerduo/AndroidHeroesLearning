package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class BasicView extends View {

    private Paint pointPaint;
    private Paint linePaint;
    private Paint multiLinesPaint;
    private Paint rectPaint;
    private Paint roundRectPaint;
    private Paint circlePaint;
    private Paint arcPaint;
    private Paint ovalPaint;
    private Paint textPaint;
    private Paint posTextPaint;
    private Paint pathPaint;

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
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(getResources().getColor(R.color.colorAccent));
        pointPaint.setStrokeWidth(15);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.colorAccent));
        linePaint.setStrokeWidth(3);

        multiLinesPaint = new Paint();
        multiLinesPaint.setAntiAlias(true);
        multiLinesPaint.setColor(getResources().getColor(R.color.colorAccent));
        multiLinesPaint.setStrokeWidth(3);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(getResources().getColor(R.color.colorAccent));
        rectPaint.setStrokeWidth(2);

        roundRectPaint = new Paint();
        roundRectPaint.setAntiAlias(true);
        roundRectPaint.setColor(getResources().getColor(R.color.colorAccent));
        roundRectPaint.setStrokeWidth(3);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getResources().getColor(R.color.colorAccent));
        circlePaint.setStrokeWidth(15);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(getResources().getColor(R.color.colorAccent));
        arcPaint.setStrokeWidth(15);

        ovalPaint = new Paint();
        ovalPaint.setAntiAlias(true);
        ovalPaint.setColor(getResources().getColor(R.color.colorAccent));
        ovalPaint.setStrokeWidth(15);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(R.color.colorAccent));
        textPaint.setStrokeWidth(15);

        posTextPaint = new Paint();
        posTextPaint.setAntiAlias(true);
        posTextPaint.setColor(getResources().getColor(R.color.colorAccent));
        posTextPaint.setStrokeWidth(15);

        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(getResources().getColor(R.color.colorAccent));
        pathPaint.setStrokeWidth(15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint(50, 50, pointPaint);
        canvas.drawLine(133, 133, 250, 250, linePaint);
        float[] lines = {55, 55, 170, 170,
                170, 170, 222, 222,
                222, 222, 444, 444
        };
        canvas.drawLines(lines, multiLinesPaint);
    }
}
