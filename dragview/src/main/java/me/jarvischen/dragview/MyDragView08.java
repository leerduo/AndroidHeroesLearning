package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView08 extends View {
    int x;
    int y;
    public MyDragView08(Context context) {
        this(context,null);
    }

    public MyDragView08(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView08(Context context, AttributeSet attrs, int defStyleAttr) {
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
                ((View)getParent()).scrollBy(-offsetX,-offsetY);
                x = rawX;
                y = rawY;
                break;
        }
        return true;
    }
}
