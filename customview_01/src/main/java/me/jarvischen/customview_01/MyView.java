package me.jarvischen.customview_01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chenfuduo on 2016/3/1.
 */
public class MyView extends TextView {

    private static final String TAG = "MyView";
    private Paint paint1;

    private Paint paint2;

    private TextPaint paint;

    private LinearGradient linearGradient;

    private int viewWidth;

    private Matrix matrix;

    private int translate;

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
        paint1 = new Paint();
        paint1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        paint1.setStyle(Paint.Style.FILL);

        paint2 = new Paint();
        paint2.setColor(Color.YELLOW);
        paint2.setStyle(Paint.Style.FILL);
    }

    /*
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint1);
        //绘制内层矩形
        canvas.drawRect(10,10,getMeasuredWidth()-10,getMeasuredHeight()-10,paint2);
        //save和restore成对出现
        canvas.save();
        canvas.translate(10, 0);
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");
        canvas.restore();
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw: " );
        if (matrix != null){
            translate += viewWidth / 5;
            if (translate > 2 * viewWidth){
                translate -= viewWidth;
            }
            matrix.setTranslate(translate,0);
            linearGradient.setLocalMatrix(matrix);
            postInvalidateDelayed(100);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure:");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged: ");
        if (viewWidth ==  0){
            viewWidth = getMeasuredWidth();
            if (viewWidth>0){
                paint = getPaint();
                linearGradient = new LinearGradient(0,0,viewWidth,0,
                        new int[]{Color.BLUE,0xffffffff,Color.BLUE},null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                matrix = new Matrix();
            }
        }
    }
}
