package me.jarvischen.animationmechanism.advancepropertyanimation;

import android.animation.TimeInterpolator;

/**
 * Created by chenfuduo on 2016/3/10.
 */
public class DecelerateAccelerateInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        float result;
        if (input <= 0.5) {
            result = (float) (Math.sin(Math.PI * input)) / 2;
        } else {
            result = (float) (2 - Math.sin(Math.PI * input)) / 2;
        }
        return result;
    }
}
