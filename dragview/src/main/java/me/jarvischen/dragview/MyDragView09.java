package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView09 extends View {
    private int x, y;
    private Scroller scroller;

    public MyDragView09(Context context) {
        this(context, null);
    }

    public MyDragView09(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView09(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //第一步,初始化Scroller
        scroller = new Scroller(context);
    }

    //第二步,重写computeScroll()方法,实现模拟滑动
    @Override
    public void computeScroll() {
        super.computeScroll();
        //判断Scroller是否执行完毕
        if (scroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(scroller.getCurrX(), scroller.getCurrY());
            //通过重绘来不断调用computeScroll()
            invalidate();
        }
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
                ((View) getParent()).scrollBy(-offsetX, -offsetY);
                break;
            case MotionEvent.ACTION_UP:
                View viewGroup = (View) getParent();
                /**
                 * Start scrolling by providing a starting point and the distance to travel.
                 * The scroll will use the default value of 250 milliseconds for the
                 * duration.
                 *
                 * @param startX Starting horizontal scroll offset in pixels. Positive
                 *        numbers will scroll the content to the left.
                 * @param startY Starting vertical scroll offset in pixels. Positive numbers
                 *        will scroll the content up.
                 * @param dx Horizontal distance to travel. Positive numbers will scroll the
                 *        content to the left.
                 * @param dy Vertical distance to travel. Positive numbers will scroll the
                 *        content up.
                 */
                scroller.startScroll(viewGroup.getScrollX(), viewGroup.getScrollY(), -viewGroup.getScrollX(),
                        -viewGroup.getScrollY());
                invalidate();
                break;
        }
        return true;
    }
}
