package me.jarvischen.animationmechanism.advancepropertyanimation;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by chenfuduo on 2016/3/10.
 * 电视关闭的效果
 */
public class CustomAnimation extends Animation {


    private float centerHeight;
    private float centerWidth;

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(500);
        setFillAfter(true);
        centerWidth = width / 2;
        centerHeight = height / 2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        matrix.preScale(1, 1 - interpolatedTime, centerWidth, centerHeight);
    }
}
