package me.jarvischen.dragview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView11 extends FrameLayout {

    private ViewDragHelper viewDragHelper;

    private View mainView;
    private View menuView;

    private int width;

    public MyDragView11(Context context) {
        this(context, null);
    }

    public MyDragView11(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView11(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //第一步,通过静态工厂初始化ViewDragHelper
        viewDragHelper = ViewDragHelper.create(this, 1.0f, callback);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mainView == child;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }
        //拖动结束后调用
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //手指抬起后缓慢移动到指定为作弊
            if (mainView.getLeft() < 500){
                viewDragHelper.smoothSlideViewTo(mainView,0,0);
                ViewCompat.postInvalidateOnAnimation(MyDragView11.this);
            }else{
                //打开菜单
                viewDragHelper.smoothSlideViewTo(mainView,300,0);
                ViewCompat.postInvalidateOnAnimation(MyDragView11.this);
            }
        }
    };


    //第二步,拦截事件---重写onInterceptTouchEvent(MotionEvent)&onTouchEvent(MotionEvent)
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
    //第三步,处理computeScroll()
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = menuView.getMeasuredWidth();
    }
}
