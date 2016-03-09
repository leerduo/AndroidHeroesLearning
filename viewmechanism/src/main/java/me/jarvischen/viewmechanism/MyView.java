package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class MyView extends View {

    //大圆
    private Paint circlePaint;
    //刻度线和刻度值
    private Paint degreePaint;
    //圆心的点
    private Paint pointPaint;
    //指针,小时的指针
    private Paint hourPaint;
    //指针,分钟的指针
    private Paint minutePaint;
    private int width, height;

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

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(15);
        pointPaint.setColor(getResources().getColor(R.color.colorAccent));

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(getResources().getColor(R.color.colorAccent));

        degreePaint = new Paint();
        degreePaint.setColor(getResources().getColor(R.color.colorAccent));
        degreePaint.setStrokeWidth(3);
        degreePaint.setAntiAlias(true);

        hourPaint = new Paint();
        hourPaint.setStrokeWidth(20);
        hourPaint.setAntiAlias(true);
        hourPaint.setColor(getResources().getColor(R.color.colorAccent));

        minutePaint = new Paint();
        minutePaint.setStrokeWidth(10);
        minutePaint.setAntiAlias(true);
        minutePaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
        canvas.drawPoint(width / 2, height / 2, pointPaint);
        for (int i = 0; i < 24; i++) {
            if (i == 0 || i == 6 || i == 12 || i == 18) {
                degreePaint.setStrokeWidth(5);
                degreePaint.setTextSize(30);
                canvas.drawLine(width / 2, height / 2 - width / 2,
                        width / 2, height / 2 - width / 2 + 60, degreePaint);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        width / 2 - degreePaint.measureText(degree) / 2, height / 2 - width / 2 + 90, degreePaint);
            } else {
                degreePaint.setStrokeWidth(3);
                degreePaint.setTextSize(15);
                canvas.drawLine(width / 2, height / 2 - width / 2,
                        width / 2, height / 2 - width / 2 + 30, degreePaint);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        width / 2 - degreePaint.measureText(degree) / 2, height / 2 - width / 2 + 60, degreePaint);
            }
            canvas.rotate(15, width / 2, height / 2);
        }

        canvas.save();
        canvas.translate(width / 2, height / 2);
        canvas.drawLine(0, 0, 100, 100, hourPaint);
        canvas.drawLine(0, 0, 100, 200, minutePaint);
        canvas.restore();
    }
}
