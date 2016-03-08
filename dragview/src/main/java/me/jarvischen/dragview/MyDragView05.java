package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView05 extends View {
    private static final String TAG = MyDragView05.class.getSimpleName();
    private int x, y;
    public MyDragView05(Context context) {
        this(context, null);
    }

    public MyDragView05(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView05(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int getX = (int) event.getX();
        int getY = (int) event.getY();
        Log.e(TAG, "onTouchEvent: " + "getLeft()=" + getLeft());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: " + "MotionEvent.ACTION_DOWN   " + "getLeft()=" + getLeft());
                x = (int) event.getX();
                y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: " + "MotionEvent.ACTION_MOVE  " + "getLeft()=" + getLeft());
                int offsetX = getX - x;
                int offsetY = getY - y;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
                layoutParams.rightMargin = getRight() + offsetX;
                layoutParams.bottomMargin = getBottom() + offsetY;
                setLayoutParams(layoutParams);
                break;
        }
        return true;
    }
}
