package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView04 extends View {
    int x;
    int y;
    public MyDragView04(Context context) {
        this(context,null);
    }

    public MyDragView04(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView04(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = rawX;
                y = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = rawX - x;
                int offsetY = rawY - y;
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                x = rawX;
                y = rawY;
                break;
        }
        return true;
    }
}
