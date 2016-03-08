package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView07 extends View {
    private int x, y;

    public MyDragView07(Context context) {
        this(context, null);
    }

    public MyDragView07(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView07(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int getX = (int) event.getX();
        int getY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = getX - x;
                int offsetY = getY - y;
                ((View)getParent()).scrollBy(-offsetX,-offsetY);
                break;
        }
        return true;
    }
}
