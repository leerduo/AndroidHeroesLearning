package me.jarvischen.animationmechanism.advancepropertyanimation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;

/**
 * Created by chenfuduo on 2016/3/10.
 * 电视关闭的效果
 */
public class CustomAnimation2 extends Animation {


    private float centerHeight;
    private float centerWidth;

    private Camera camera = new Camera();

    private float mRotateY = 0.0f;

    public void setmRotateY(float mRotateY) {
        this.mRotateY = mRotateY;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(3000);
        setFillAfter(true);
        setInterpolator(new BounceInterpolator());
        centerWidth = width / 2;
        centerHeight = height / 2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(mRotateY * interpolatedTime);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preScale(centerWidth, centerHeight);
        matrix.preScale(-centerWidth, -centerHeight);
    }
}
