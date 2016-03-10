package me.jarvischen.animationmechanism.advancepropertyanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

import me.jarvischen.animationmechanism.R;

/**
 * Created by chenfuduo on 2016/3/10.
 */
public class MyAnimView extends View {

    private static final float RADIUS = 50.0f;

    private Point currentPoint;

    private Paint paint;

    private String color;

    public MyAnimView(Context context) {
        this(context, null);
    }

    public MyAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setAntiAlias(true);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        paint.setColor(Color.parseColor(color));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentPoint == null) {
            currentPoint = new Point(RADIUS, RADIUS);
            drawCircle(canvas);
            startAnimation();
        } else {
            drawCircle(canvas);
        }
    }

    private void startAnimation() {
        //从左上角到右下角
       /* Point startPoint = new Point(RADIUS, RADIUS);
        Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);*/
        //从屏幕正中央的顶部掉落到底部
        Point startPoint = new Point(getWidth()/2, RADIUS);
        Point endPoint = new Point(getWidth()/2, getHeight() - RADIUS);
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(3000);
        //valueAnimator.setInterpolator(new AccelerateInterpolator(2f));
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.start();
        valueAnimator.setRepeatCount(100);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);

        valueAnimator.start();
        //暂时注释掉
        /*ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(),
                "#0000FF", "#FF0000");
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(valueAnimator).with(anim2);
        animSet.setDuration(2500);
        animSet.start();*/
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, paint);
    }
}
