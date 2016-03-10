# Android体系与系统架构

## Dalvik与ART

总体来说,Dalvik的特点是在运行时编译,而ART是在安装时编译。

ART的新特征体现在下面的三点：

* Ahead-of-time (AOT) compilation
* Improved garbage collection (GC)
* Improved debugging support

可参考：

[ART and Dalvik](https://source.android.com/devices/tech/dalvik/index.html)
[Android 5.0 Behavior Changes](http://developer.android.com/about/versions/android-5.0-changes.html)

## 应用运行上下文对象

此处指Context,Android中的Activity,Service,Application均继承自Context.
关于Context,可以参考这里:
[Android Context完全解析,你所不知道的Context的各种细节 ](http://blog.csdn.net/guolin_blog/article/details/47028975)

# Android开发工具新接触

## 导入Android Studio项目

有时候导入项目到Android Studio的时候,由于Gradle版本的不同,会带来一些问题,这样导入可以解决Gradle版本的不同带来的一些问题。

首先在本地用当前版本的Gradle创建一个正常的项目,保证可以正常编译通过即可,然后用本地项目的`gradle文件夹`和`build.gradle`文件去替换要导入项目中的这两个。即可。

2016-03-02更正：
最好别使用这个方法,否则可能会遇到很麻烦的问题。

# Android控件架构与自定义控件详解

## Android控件架构

Android系统中的所有UI类都是建立在View和ViewGroup这两个类的基础上的。所有View的子类成为"Widget",所有ViewGroup的子类成为"Layout"。
View和ViewGroup之间采用了`组合设计模式`,可以使得"部分-整体"同等对待。ViewGroup作为布局容器类的最上层,
布局容器里面又可以有View和ViewGroup。如下图：

![View树结构](http://7xljei.com1.z0.glb.clouddn.com/viewgroup.png)

其中,ViewParent是个接口,ViewGroup实现了该接口。

![UI界面架构图](http://7xljei.com1.z0.glb.clouddn.com/UIFramework_%E5%89%AF%E6%9C%AC.png)

当我们运行程序的时候,有一个setContentView()方法,Activity其实不是显示视图（直观上感觉是它）,实际上Activity调用了
PhoneWindow的setContentView()方法,然后加载视图,将视图放到这个Window上,而Activity其实构造的时候初始化的是Window（PhoneWindow）,
Activity其实是个控制单元，即可视的人机交互界面。打个比喻：

Activity是一个工人，它来控制Window;Window是一面显示屏，用来显示信息;View就是要显示在显示屏上的信息,
这些View都是层层重叠在一起（通过infalte()和addView()）放到Window显示屏上的。
而LayoutInfalter就是用来生成View的一个工具,XML布局文件就是用来生成View的原料。再来说说代码中具体的执行流程

`setContentView(R.layout.main)`其实就是下面内容。（注释掉本行执行下面的代码可以更直观）

```java
getWindow().setContentView(LayoutInflater.from(this).inflate(R.layout.main, null));
```

即运行程序后，Activity会调用PhoneWindow的setContentView()来生成一个Window,
而此时的setContentView就是那个最底层的View。然后通过LayoutInflater.infalte()方法加载布局生成View对象并通过addView()方法
添加到Window上,(一层一层的叠加到Window上)。

所以，Activity其实不是显示视图，View才是真正的显示视图。

注：一个Activity构造的时候只能初始化一个Window(PhoneWindow),
另外这个PhoneWindow有一个"ViewRoot",这个"ViewRoot"是一个View或ViewGroup,是最初始的根视图,然后通过addView方法将View一个个层叠到ViewRoot上,这些层叠的View最终放在Window这个载体上面。

当程序在onCreate方法中调用setContentView方法后,ams会回调onResume方法,此时系统才会把整个DecorView添加到PhoneWindow中,并让其显示,从而最终完成界面的绘制。

## View的测量

下面的代码,可以说是通用的：

```java
package me.jarvischen.viewonmeasure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/2/29.
 */
public class MyView extends View {
    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 200;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 200;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, result);
            }
        }
        return result;
    }
}
```

## 自定义控件

自定义控件的时候遇到一个问题，详见[这里](http://chenfuduo.me/2016/03/01/onMeasure%E6%89%A7%E8%A1%8C%E6%AC%A1%E6%95%B0%E7%9A%84%E9%97%AE%E9%A2%98/)

这个问题所有的讨论在[这里](https://github.com/android-cn/android-discuss/issues/386)可以看到。

这个问题在性能优化上可以用的到。


## 自定义ViewGroup

这里只提Scroller的用法。
```java
package me.jarvischen.scrollerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        final Button by = (Button) findViewById(R.id.by);
        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相对于上一次
                ll.scrollBy(-60, -100);
                int scrollY = ll.getScrollY();
                int scrollX = ll.getScrollX();
                //向右,向下均为负,其他相反
                Log.e(TAG, "scrollY: " + scrollY);
                Log.e(TAG, "scrollX: " + scrollX);
            }
        });
        final Button to = (Button) findViewById(R.id.to);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.scrollTo(-60, -100);
                int scrollY = ll.getScrollY();
                Log.e(TAG, "onClick: " + scrollY);
                int scrollX = ll.getScrollX();
                Log.e(TAG, "scrollY: " + scrollY);
                Log.e(TAG, "scrollX: " + scrollX);
            }
        });
    }

}
```
几个容易混淆的方法,参见代码的注释。参考：
[scrollTo、scrollBy、getScrollX、getScrollY这4个方法的含义](http://blog.csdn.net/xiaoguochang/article/details/8655210)
[关于View的ScrollTo， getScrollX 和 getScrollY](http://www.xuebuyuan.com/2013505.html)
[图解Android View的scrollTo(),scrollBy(),getScrollX(), getScrollY()](http://blog.csdn.net/bigconvience/article/details/26697645)

# Android Scroll分析

## Android坐标系

Android中，将屏幕最左上角的顶点作为Android坐标系的原点,从这个点向右是X轴正方向,从这个点向下是Y轴正方向。

![Android坐标系](http://7xljei.com1.z0.glb.clouddn.com/androidzuobiao.png)


系统提供了`getLocationOnScreen(int location[])`这样的方法来获取Android坐标系中点的位置。在触摸事件中使用`getRawX`和`getRawY`方法
所获取的坐标同样是Android坐标系中的坐标。

## 视图坐标系

视图坐标系描述了子视图在父视图中的位置关系,视图坐标系同样是以原点向右是X轴正方向,以原点向下是Y轴正方向,以父视图左上角为坐标原点。

![视图坐标系](http://7xljei.com1.z0.glb.clouddn.com/layoutzuobiaoxi.png)

在触摸事件中,通过`getX`和`getY`所获得的坐标就是视图坐标系中的坐标。

## 触摸事件

![触摸事件](http://7xljei.com1.z0.glb.clouddn.com/20150115155321445.png)

其中,View提供的获取坐标的方法有：

* getTop():获取到的是View自身的顶边到其父布局顶边的距离
* getBottom():获取到的是View自身的底边到其父布局顶边的距离
* getLeft():获取到的是View自身的左边到其父布局左边的距离
* getRight():获取到的是View自身的右边到其父布局左边的距离

其中MotionEvent提供的方法有:

* getX():获取点击事件距离控件左边的距离,即视图坐标
* getY():获取点击事件距离控件右边的距离,即视图坐标
* getRawX():获取点击事件距离整个屏幕左边的距离,即绝对坐标
* getRawY():获取点击事件距离整个屏幕顶边的距离,即绝对坐标

## 实现滑动的七种方法

### layout方法

layout方法可以通过两种坐标系去完成这个,其一：
```java
package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/6.
 */
public class MyDragView01 extends View {
    private static final String TAG = MyDragView01.class.getSimpleName();
    private int x, y;

    public MyDragView01(Context context) {
        this(context, null);
    }

    public MyDragView01(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView01(Context context, AttributeSet attrs, int defStyleAttr) {
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
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
        }
        return true;
    }
}
```

其二,使用绝对坐标系：
```java
package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView02 extends View {
    int x;
    int y;

    public MyDragView02(Context context) {
        this(context, null);
    }

    public MyDragView02(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView02(Context context, AttributeSet attrs, int defStyleAttr) {
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
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                x = rawX;
                y = rawY;
                break;
        }
        return true;
    }
}
```
使用绝对坐标系需要注意是每次执行完ACTION_MOVE的逻辑后,一定要重新设置初始坐标,这样才能准确获得偏移量。

### offsetLeftAndRight()和offsetTopAndBottom()方法

```java
package me.jarvischen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class MyDragView03 extends View {
    private static final String TAG = MyDragView03.class.getSimpleName();
    private int x, y;

    public MyDragView03(Context context) {
        this(context,null);
    }

    public MyDragView03(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragView03(Context context, AttributeSet attrs, int defStyleAttr) {
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
                //layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                break;
        }
        return true;
    }
}
```

对于使用绝对坐标实现的,和此原理相同。

### LayoutParams

LayoutParams保存了一个布局的位置参数信息。通过`getLayoutParams()`获取LayoutParams时,需要根据View所在父布局的类型来设置不同的类型。
比如的我的布局代码：
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="me.jarvischen.dragview.MainActivity">

    <me.jarvischen.dragview.MyDragView05
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="@color/colorAccent" />
</LinearLayout>
```
它的父布局是LinearLayout,所以我是用`LinearLayout.LayoutParams`,当然,也可以使用`ViewGroup.LayoutParams`。
```java
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
                //layoutParams.rightMargin = getRight() + offsetX;
                //layoutParams.bottomMargin = getBottom() + offsetY;
                setLayoutParams(layoutParams);
                break;
        }
        return true;
    }
}
```
绝对坐标的情况：
```java
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
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin = offsetX + getLeft();
                layoutParams.topMargin = offsetY + getTop();
                setLayoutParams(layoutParams);
                x = rawX;
                y = rawY;
                break;
        }
        return true;
    }
```

### scrollTo与scrollBy

scrollTo()方法是让View相对于初始的位置滚动某段距离,由于View的初始位置是不变的,因此不管我们点击多少次scrollTo按钮滚动到的都将是
同一个位置。而scrollBy()方法则是让View相对于当前的位置滚动某段距离,那每当我们点击一次scrollBy按钮,View的当前位置都进行了变动,
因此不停点击会一直向右下方(传入的两个坐标值均为负数)移动。

[Android Scroller完全解析,关于Scroller你所需知道的一切 ](http://blog.csdn.net/guolin_blog/article/details/48719871)

```java
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
                scrollBy(offsetX, offsetY);
                break;
        }
        return true;
    }
```
代码这样写,发现其并不能移动。原因是scrollTo和scrollBy移动的是View的content。改成这样：
```java
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
                ((View)getParent()).scrollBy(offsetX, offsetY);
                break;
        }
        return true;
    }
```
但是发现它在乱动。
scrollBy中的参数是负数的时候,那么将向坐标轴正方向移动,反之相反。
所以正确的应该是这样的：
```java
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
                ((View)getParent()).scrollBy(-offsetX, -offsetY);
                break;
        }
        return true;
    }
```

### Scroller

通过`Scroller`类可以实现平滑移动的效果,而不是瞬间完成的移动(比如通过scrollTo和scrollBy方法)。

Scroller原理：和scrollTo,scrollBy类似,只是,在ACTION_MOVE中不断获取手指移动的微小偏移量,将一段距离划分为N个非常小的偏移量。
在每个偏移量里面通过scrollBy方法进行瞬间移动,实现平滑移动。

(本demo中,同样让子View跟随手指的滑动而滑动,但是在手指离开屏幕时,让子View平滑的移动到初始位置,即屏幕左上角。)

* 第一步,初始化Scroller

```java
scroller = new Scroller(context);
```

* 第二步,重写computeScroll()方法,实现模拟滚动

computeScroll()是Scroller类的核心,系统在绘制View的时候会在draw()方法中调用该方法。这个方法实际上就是使用的scrollTo方法。再结合Scroller
对象,帮助获取到当前的滚动值。下面的代码是模板代码：

```java
//第二步,重写computeScroll()方法,实现模拟滑动
    @Override
    public void computeScroll() {
        super.computeScroll();
        //判断Scroller是否执行完毕
        if (scroller.computeScrollOffset()){
            ((View)getParent()).scrollTo(scroller.getCurrX(),scroller.getCurrY());
            //通过重绘来不断调用computeScroll()
            invalidate();
        }
    }
```

Scroller类提供了computeScrollOffset()方法来判断是否完成了整个滑动,同时也提供了getCurrX()和getCurrY()
来获取当前的滑动坐标。

唯一需要注意的是invalidate()方法,因为只能在computeScroll()方法中获取模拟过程的scrollX和scrollY坐标,但computeScroll()不会自动调用,
只能通过invalidate()->draw()->computeScroll()来间接调用computeScroll(),所以需要在上述代码中调用invalidate(),
实现循环获取scrollX和scrollY的目的。模拟过程结束后,scroller.computeScrollOfset()方法会返回 false,从而中断循环，完成整个平滑移动的过程。

* 第三步,startScroll开启模拟过程

```java
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
```
### 属性动画

```java
package me.jarvischen.dragview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyDragView10 myDragView = (MyDragView10) findViewById(R.id.myDragView);
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(myDragView, "translationX",0, 500,0,500);
        objectAnimator.setDuration(5000);
        objectAnimator.start();
        objectAnimator.setRepeatCount(200);
    }
}
```
### ViewDragHelper

ViewDragHelper在之前已经写了相关的文章。
[ViewDragHelper的用法](http://chenfuduo.me/2015/07/16/ViewDragHelper%E7%9A%84%E7%94%A8%E6%B3%95/)
[ViewDragHelper的用法2](http://chenfuduo.me/2015/07/16/ViewDragHelper%E7%9A%84%E7%94%A8%E6%B3%952/)

这里也是简单的做的演示下QQ侧滑的效果,代码如下:
```java
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
```
布局：
```xml
<?xml version="1.0" encoding="utf-8"?>
<me.jarvischen.dragview.MyDragView11 xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="me.jarvischen.dragview.ViewDragHelperTestActivity">

    <FrameLayout
        android:id="@+id/menu"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/colorAccent" />

    <FrameLayout
        android:id="@+id/main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/colorPrimaryDark" />

</me.jarvischen.dragview.MyDragView11>
```

# Android绘图机制与处理技巧

## 屏幕的尺寸信息

### 屏幕参数

* 分辨率

分辨率就是手机屏幕的像素点数,一般描述成屏幕的"宽×高",安卓手机屏幕常见的分辨率有480×800、720×1280、1080×1920等。
720×1280表示此屏幕在宽度方向有720个像素，在高度方向有1280个像素。

* 屏幕大小

屏幕大小是手机对角线的物理尺寸,以英寸(inch)为单位。比如某某手机为"5寸大屏手机",就是指对角线的尺寸,5寸×2.54厘米/寸=12.7厘米。

* 密度(dpi,dots per inch;或PPI，pixels per inch)

从英文顾名思义,就是每英寸的像素点数,数值越高当然显示越细腻。假如我们知道一部手机的分辨率是1080×1920,屏幕大小是5英寸,
通过宽1080和高1920,根据勾股定理,我们得出对角线的像素数大约是2203,那么用2203除以5就是此屏幕的密度了,计算结果是440。
440dpi的屏幕已经相当细腻了。

### 系统屏幕密度

[系统屏幕密度](http://7xljei.com1.z0.glb.clouddn.com/027f3555d594bd00000128a1d733f9.jpg)

### 独立像素密度dp

dp也可写为dip,即density-independent pixel。你可以想象dp更类似一个物理尺寸,比如一张宽和高均为100dp的图片在320×480和480×800
的手机上"看起来"一样大。而实际上,它们的像素值并不一样。dp正是这样一个尺寸,不管这个屏幕的密度是多少,屏幕上相同dp大小的元素看起来
始终差不多大。
另外,字尺寸使用sp,即scale-independentpixel的缩写,这样,当你在系统设置里调节字号大小时,应用中的文字也会随之变大变小。
![sp&dp](http://7xljei.com1.z0.glb.clouddn.com/02a266556d6bd20000016b62fbb2ec.jpg)

`ldpi:mdpi:hdpi:xhdpi:xxhdpi=3:4:6:8:12`

![转化关系](http://7xljei.com1.z0.glb.clouddn.com/024dc5556d6bbb0000016b62bbcfca.jpg)

## 单位转换

单位转换是个工具类。

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by chenfuduo on 2016/3/8.
 */
public class DisplayUtils {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dp2px(Context context, float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

}
```
最后两个是通过TypedValue类帮助转换的。

## 2D绘图基础

系统通过提供的Canvas对象来提供绘图方法。它提供了各种绘制图像的API,如drawPoint(点)、drawLine(线)、drawRect(矩形)、
drawVertices(多边形)、drawArc(弧)、drawCircle(圆)、drawPath(路径)、drawText(文本)、drawOval(椭圆)、
drawBitmap(贴图)等等。

Paint画笔也是绘图中一个非常重要的元素。

* setAntiAlias() 　　　//设置画笔的锯齿效果
* setColor() 　　　　//设置画笔的颜色
* setARGB() 　　　　　//设置画笔的A、R、G、B值
* setAlpha() 　　　　//设置画笔的Alpha值
* setTextSize() 　　　//设置字体的尺寸
* setStyle() 　　　　//设置画笔的风格(空心或者实心)
* setStrokeWidth() 　//设置空心边框的宽度

Canvas家族成员

* DrawPoint，绘制点

```java
canvas.drawPoint(x,y,paint);
//绘制多个点
canvas.drawPoints(pts,paint);
```

* DrawLine，绘制直线
```java
canvas.drawLine(startX,startY,endX,endY,paint);
```
* DrawLines，绘制多条直线
```java
float[] pts = {startX1,startX2,endX1,endX2,...,startXn,startYn,endXn,endYn};
canvas.drawLines(pts,point);
```
* DrawRect,绘制矩形
```java
canvas.drawRect(left,top,right,bottom,paint);
```
* DrawRoundRect,绘制圆角矩形
```java
canvas.drawRoundRect(left,top,right,bottom,radiusX,radiusY,paint);
```
* DrawCircle,绘制圆
```java
canvas.drawCircle(circleX,circleY,radius,paint);
```
* DrawArc,绘制弧形、扇形
```java
paint.setStyle(Paint.STROKE);
canvas.drawArc(left,top,right,bottom,startAngle,sweepAngle,(boolean类型)useCenter,paint);
//Panit.Style和useCenter属性的组合
//1.绘制扇形
Panit.Style.STROKE + useCenter(true);

//2.绘制弧形
Panit.Style.STROKE + useCenter(false);

//3.绘制实心扇形
Panit.Style.FILL + useCenter(true);

//4.绘制实心弧形
Panit.Style.FILL + useCenter(false);
```
* DrawOval，绘制椭圆
```java
//通过椭圆的外接矩形来绘制椭圆
canvas.drawOval(left,top,right,bottom,paint);
```
* DrawText，绘制文本
```java
canvas.drawText(text,startX,startY,paint);
```
* DrawPosText,在指定位置绘制文本
```java
canvas.drawPosText(text,new float[]{x1,x2,x2,y2,...xn,yn},paint);
```
* DrawPath,绘制路径
```java
Path path = new Path();
path.moveTo(50,50);
path.lineTo(100,100);
path.lineTo(100,300);
path.lineTo(300,50);
canvas.drawPath(path,paint);
```
## Android XML绘图

### Bitmap
```xml
<?xml version="1.0" encoding="utf-8"?>
<bitmap xmlns:android="http://schema.android.com/apk/res/android
    android:src="@drawable/ic_lancher" />
```

### Shape

Shape可以说是XML绘图的精华所在。Shape功能十分强大,无论是扁平化、拟物化还是渐变,它都能绘制。
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schema.android.com/apk/res/android
    //默认为rectangle
    android:shape=["rectangle"|"oval"|"line"|"ring"] >
    <corners 　//当shape="rectangle"时使用
        android:radius="1dp"
        android:topLeftRadius="0.5dp"
        android:topRightRadius="0.5dp"
        android:bottomLeftRadius="0.5dp"
        android:bottomRightRadius="0.5dp" />

    <gradient  //渐变
        android:angle="0.5dp"
        android:centerX="0.5dp"
        android:centerY="0.5dp"
        android:centerColor="#ffffff"
        android:endColor="@color/"
        android:gradientRadius="0.5dp"
        android:startColor="@color/"
        android:type=["linear"|"radial"|"sweep"]
        android:useLevel=["true" | "false"] />

    <padding
        android:left="20dp"
        android:top="20dp"
        android:right="20dp"
        android:bottom="20dp" />

    <size  //指定大小，一般用imageview配合scaleType属性使用
        android:width="20dp"
        android:height="20dp" />

    <solid  //填充颜色
        android:color="@color/" />

    <stroke //指定边框
        android:width="20dp"
        android:color="@color/"
        //虚线宽度
        android:dashWidth="5dp"
        //虚线间隔宽度
        android:dashGap="2dp"

</shape>
```

### layer

通过layer、layer-list实现图层效果,图片会依次叠加

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schema.android.com/apk/res/android >

<!--图片1-->
<item
    android:drawable="@drawable/ic_launcher" />

<!--图片2-->
<item
    android:drawable="@drawable/ic_launcher"
    android:left="10dp"
    android:top="10dp"
    android:right="10dp"
    android:bottom="10dp" />

</layer-list>
```

### Selector

Selector的作用在于实现静态绘图中的事件反馈,通过给不同的事件设置不同的图像,从而在程序中根据用户输入,返回不同的效果。
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schema.android.com/apk/res/android >

<!--默认时的背景图片-->
<item android:drawable="@drawable/X1" />

<!--没有焦点时的背景图片-->
<item android:state_window_focused="false"
      android:drawable="@drawable/X2 />

<!--非触摸模式下获得焦点并单击时的背景图片-->
<item android:state_focused="true"
      android:state_pressed="true"
      android:drawable="@drawable/X3" />

<!--触摸模式下获得焦点并单击时的背景图片-->
<item android:state_focused="false"
      android:state_pressed="true"
      android:drawable="@drawable/X4" />

<!--选中时的背景图片-->
<item android:state_selected="true"
      android:drawable="@drawable/X5" />

<!--获得焦点时的图片背景-->
<item android:state_focused="true"
      android:drawable="@drawable/X6 />

</selector>
```
Selector也可以使用Shape作为它的Item，实现具有点击反馈效果的Selector
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schema.android.com/apk/res/android >

<item android:state_pressed="true" >
    <shape android:shape="rectangle" >

<!--填充的颜色-->
<solid android:color="#00ff00" />

<!--设置按钮的四个角为弧形-->
<corners android:radius="5dp" />

<padding
    android:bottom="10dp"
    android:left="10dp"
    android:right="10dp"
    android:top="10dp" />

    </shape>
</item>

<item >
    <shape android:shape="rectangle" >

<!--填充的颜色-->
<solid android:color="#00ffff" />

<!--设置按钮的四个角为弧形-->
<corners android:radius="5dp" />

<padding
    android:bottom="10dp"
    android:left="10dp"
    android:right="10dp"
    android:top="10dp" />

    </shape>
</item>

</selector>
```
## Android绘图技巧

### Canvas

* Canvas.save()

Canvas.save()这个方法,从字面上可以理解为保存画布。它的作用就是将之前的所有已绘制图像保存起来,让后续的操作就好像在一个新的图层
上操作一样。

* Canvas.restore()

Canvas.restore()这个方法,可以理解为合并图层操作。它的作用是将我们在sava()之后绘制的所有图像与sava()之前的图像进行合并。

* Canvas.translate()

Canvas.translate()这个方法,可以理解为坐标系的平移。默认绘制坐标零点位于屏幕左上角,那么在调用translate(x,y)方法之后,
则将原点(0,0)移动到了(x,y)。之后的所有绘图操作都将以(x,y)为原点执行。

* Canvas.rotate()

Canvas.rotate()将坐标系旋转了一定的角度。

下面的例子绘制一个仪表盘。

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class MyView extends View {

    //大圆
    private Paint circlePaint;
    //刻度线和刻度值
    private Paint degreePaint;
    //圆心的点
    private Paint pointPaint;
    //指针,小时的指针
    private Paint hourPaint;
    //指针,分钟的指针
    private Paint minutePaint;
    private int width, height;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(15);
        pointPaint.setColor(getResources().getColor(R.color.colorAccent));

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(getResources().getColor(R.color.colorAccent));

        degreePaint = new Paint();
        degreePaint.setColor(getResources().getColor(R.color.colorAccent));
        degreePaint.setStrokeWidth(3);
        degreePaint.setAntiAlias(true);

        hourPaint = new Paint();
        hourPaint.setStrokeWidth(20);
        hourPaint.setAntiAlias(true);
        hourPaint.setColor(getResources().getColor(R.color.colorAccent));

        minutePaint = new Paint();
        minutePaint.setStrokeWidth(10);
        minutePaint.setAntiAlias(true);
        minutePaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
        canvas.drawPoint(width / 2, height / 2, pointPaint);
        for (int i = 0; i < 24; i++) {
            if (i == 0 || i == 6 || i == 12 || i == 18) {
                degreePaint.setStrokeWidth(5);
                degreePaint.setTextSize(30);
                canvas.drawLine(width / 2, height / 2 - width / 2,
                        width / 2, height / 2 - width / 2 + 60, degreePaint);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        width / 2 - degreePaint.measureText(degree) / 2, height / 2 - width / 2 + 90, degreePaint);
            } else {
                degreePaint.setStrokeWidth(3);
                degreePaint.setTextSize(15);
                canvas.drawLine(width / 2, height / 2 - width / 2,
                        width / 2, height / 2 - width / 2 + 30, degreePaint);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        width / 2 - degreePaint.measureText(degree) / 2, height / 2 - width / 2 + 60, degreePaint);
            }
            canvas.rotate(15, width / 2, height / 2);
        }

        canvas.save();
        canvas.translate(width / 2, height / 2);
        canvas.drawLine(0, 0, 100, 100, hourPaint);
        canvas.drawLine(0, 0, 100, 200, minutePaint);
        canvas.restore();
    }
}
```
效果如下图：
![仪表盘](http://7xljei.com1.z0.glb.clouddn.com/4379274283332372.jpg)

[Android Canvas编程：对rotate()和translate()两个方法的研究](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/0304/957.html)

### Layer图层

Android通过调用saveLayer()、saveLayerAlpha()方法将一个图层入栈(图层基于栈管理结构),
使用restore()、restoreToCount()方法将一个图层出栈。入栈的时候,后面所有的操作都发生在这个图层上,
而出栈的时候,则会把图像绘制到上层Canvas上。

如下的代码：

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class BasicView extends View {

    private Paint paint;

    private static final int LAYER_FLAGS =
            Canvas.MATRIX_SAVE_FLAG |
                    Canvas.CLIP_SAVE_FLAG |
                    Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                    Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                    Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    public BasicView(Context context) {
        this(context, null);
    }

    public BasicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawCircle(150, 150, 100, paint);

        //127,255,0
        canvas.saveLayerAlpha(0, 0, 400, 400, 0, LAYER_FLAGS);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawCircle(200, 200, 100, paint);
        canvas.restore();
    }
}
```

透明为127的时候,半透明:

![半透明](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160309152958.png)

透明为255的时候,完全不透明:

![完全不透明](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160309153018.png)

透明为0的时候,完全透明:

![完全透明](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160309153027.png)

## Android图像处理之色彩特效处理

### 色彩矩阵

图像的色调、饱和度、亮度这三个属性在图像处理中使用非常之多,Android封装了一个ColorMatrix类（颜色矩阵）,
通过改变矩阵值来处理这些属性。


* 色调：//第一个参数分别为red、green、blue 第二个参数为要处理的值。

```java
ColorMatrix hueMatrix=new ColorMatrix();
      hueMatrix.setRotate(0, hue);
      hueMatrix.setRotate(1, hue);
      hueMatrix.setRotate(2, hue);
```

* 饱和度：//参数即为饱和度，当饱和度为0时图像就变成灰图像了。

```java
ColorMatrix saturationMatrix=new ColorMatrix();
      saturationMatrix.setSaturation(saturation);
```

* 亮度：//当三原色以相同比例进行混合的时候，就会显示出白色，从而调节亮度。

```java
ColorMatrix lumMatrix=new ColorMatrix();
      lumMatrix.setScale(lum, lum, lum, 1);
```

* 让三种效果叠加：ColorMatrix imageMatrix=new ColorMatrix();

```java
imageMatrix.postConcat(hueMatrix);
imageMatrix.postConcat(saturationMatrix);
imageMatrix.postConcat(lumMatrix);
```

下面是个具体的案例。

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/imageview"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="24dp"
        android:layout_marginTop="24dp" />

    <SeekBar
        android:id="@+id/seekbarHue"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/imageview" />

    <SeekBar
        android:id="@+id/seekbarSaturation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/seekbarHue" />

    <SeekBar
        android:id="@+id/seekbatLum"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/seekbarSaturation" />

</RelativeLayout>
```

Bitmap处理类：

```java
package me.jarvischen.viewmechanism;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class ImageHelper {
    public static Bitmap handleImageEffect(Bitmap bm, float hue, float saturation, float lum) {
        Bitmap bmp = Bitmap.createBitmap(
                bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, hue);
        hueMatrix.setRotate(1, hue);
        hueMatrix.setRotate(2, hue);

        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }
}
```

逻辑：

```java
package me.jarvischen.viewmechanism;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

public class PrimaryColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private ImageView mImageView;
    //分别为色调,饱和度,亮度
    private SeekBar mSeekbarhue, mSeekbarSaturation, mSeekbarLum;
    //分别为色调,饱和度,亮度
    private float mHue, mStauration, mLum;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_color);
        bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon);
        mImageView = (ImageView) findViewById(R.id.imageview);
        mSeekbarhue = (SeekBar) findViewById(R.id.seekbarHue);
        mSeekbarSaturation = (SeekBar) findViewById(R.id.seekbarSaturation);
        mSeekbarLum = (SeekBar) findViewById(R.id.seekbatLum);
        mSeekbarhue.setOnSeekBarChangeListener(this);
        mSeekbarSaturation.setOnSeekBarChangeListener(this);
        mSeekbarLum.setOnSeekBarChangeListener(this);
        mSeekbarhue.setMax(MAX_VALUE);
        mSeekbarSaturation.setMax(MAX_VALUE);
        mSeekbarLum.setMax(MAX_VALUE);
        mSeekbarhue.setProgress(MID_VALUE);
        mSeekbarSaturation.setProgress(MID_VALUE);
        mSeekbarLum.setProgress(MID_VALUE);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbarHue:
                mHue = (progress - MID_VALUE) * 1.0F / MID_VALUE * 180;
                break;
            case R.id.seekbarSaturation:
                mStauration = progress * 1.0F / MID_VALUE;
                break;
            case R.id.seekbatLum:
                mLum = progress * 1.0F / MID_VALUE;
                break;
        }
        mImageView.setImageBitmap(ImageHelper.handleImageEffect(
                bitmap, mHue, mStauration, mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
```

![效果图](http://7xljei.com1.z0.glb.clouddn.com/119382253878632655.jpg)

### Android颜色矩阵-ColorMatrix

代码如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/imageview"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:src="@drawable/icon"
        android:layout_weight="2" />

    <GridLayout
        android:id="@+id/group"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="3"
        android:columnCount="5"
        android:rowCount="4">

    </GridLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:onClick="btnChange"
            android:text="Change" />

        <Button
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:onClick="btnReset"
            android:text="Reset" />
    </LinearLayout>

</LinearLayout>
```

逻辑：

```java
package me.jarvischen.viewmechanism;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

public class ColorMatrixActivity extends AppCompatActivity {

    private ImageView mImageView;
    private GridLayout mGroup;
    private Bitmap bitmap;
    private int mEtWidth, mEtHeight;
    //4*5的矩阵
    private EditText[] mEts = new EditText[20];
    private float[] mColorMatrix = new float[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_matrix);
        bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon);
        mImageView = (ImageView) findViewById(R.id.imageview);
        mGroup = (GridLayout) findViewById(R.id.group);
        mImageView.setImageBitmap(bitmap);

        mGroup.post(new Runnable() {
            @Override
            public void run() {
                // 获取宽高信息
                mEtWidth = mGroup.getWidth() / 5;
                mEtHeight = mGroup.getHeight() / 4;
                addEts();
                initMatrix();
            }
        });
    }

    // 获取矩阵值
    private void getMatrix() {
        for (int i = 0; i < 20; i++) {
            mColorMatrix[i] = Float.valueOf(
                    mEts[i].getText().toString());
        }
    }

    // 将矩阵值设置到图像
    private void setImageMatrix() {
        Bitmap bmp = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        android.graphics.ColorMatrix colorMatrix =
                new android.graphics.ColorMatrix();
        colorMatrix.set(mColorMatrix);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        mImageView.setImageBitmap(bmp);
    }

    // 作用矩阵效果
    public void btnChange(View view) {
        getMatrix();
        setImageMatrix();
    }

    // 重置矩阵效果
    public void btnReset(View view) {
        initMatrix();
        getMatrix();
        setImageMatrix();
    }

    // 添加EditText
    private void addEts() {
        for (int i = 0; i < 20; i++) {
            EditText editText = new EditText(ColorMatrixActivity.this);
            mEts[i] = editText;
            mGroup.addView(editText, mEtWidth, mEtHeight);
        }
    }

    // 初始化颜色矩阵为初始状态
    private void initMatrix() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                mEts[i].setText(String.valueOf(1));
            } else {
                mEts[i].setText(String.valueOf(0));
            }
        }
    }
}
```
这里需要注意的是在Activity的onCreate方法中,无法获取到GridLayout的宽高,所以通过View的post方法,在视图创建完毕后获得其宽高值。

![高饱和度](http://7xljei.com1.z0.glb.clouddn.com/868361883326946341.jpg)

![去色效果](http://7xljei.com1.z0.glb.clouddn.com/82765487251827052.jpg)

![怀旧效果](http://7xljei.com1.z0.glb.clouddn.com/385029250006128362.jpg)

![图像反转](http://7xljei.com1.z0.glb.clouddn.com/651940980052885974.jpg)

![灰度效果](http://7xljei.com1.z0.glb.clouddn.com/901854896512497535.jpg)

### 像素点分析

布局：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <ImageView
            android:id="@+id/imageview1"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1" />

        <ImageView
            android:id="@+id/imageview2"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1" />
    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <ImageView
            android:id="@+id/imageview3"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1" />

        <ImageView
            android:id="@+id/imageview4"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1" />
    </LinearLayout>

</LinearLayout>
```

逻辑：

```java
package me.jarvischen.viewmechanism;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class PixelsEffectActivity extends AppCompatActivity {

    private ImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixels_effect);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        imageView1 = (ImageView) findViewById(R.id.imageview1);
        imageView2 = (ImageView) findViewById(R.id.imageview2);
        imageView3 = (ImageView) findViewById(R.id.imageview3);
        imageView4 = (ImageView) findViewById(R.id.imageview4);

        imageView1.setImageBitmap(bitmap);
        imageView2.setImageBitmap(ImageHelper.handleImageNegative(bitmap));
        imageView3.setImageBitmap(ImageHelper.handleImagePixelsOldPhoto(bitmap));
        imageView4.setImageBitmap(ImageHelper.handleImagePixelsRelief(bitmap));
    }
}
```

核心算法：

```java
//底片效果
    public static Bitmap handleImageNegative(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color;
        int r, g, b, a;

        Bitmap bmp = Bitmap.createBitmap(width, height
                , Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }
            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    //老照片效果
    public static Bitmap handleImagePixelsOldPhoto(Bitmap bm) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color = 0;
        int r, g, b, a, r1, g1, b1;

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

        bm.getPixels(oldPx, 0, bm.getWidth(), 0, 0, width, height);
        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            a = Color.alpha(color);
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            if (r1 > 255) {
                r1 = 255;
            }
            if (g1 > 255) {
                g1 = 255;
            }
            if (b1 > 255) {
                b1 = 255;
            }

            newPx[i] = Color.argb(a, r1, g1, b1);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    //浮雕效果
    public static Bitmap handleImagePixelsRelief(Bitmap bm) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color = 0, colorBefore = 0;
        int a, r, g, b;
        int r1, g1, b1;

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

        bm.getPixels(oldPx, 0, bm.getWidth(), 0, 0, width, height);
        for (int i = 1; i < width * height; i++) {
            colorBefore = oldPx[i - 1];
            a = Color.alpha(colorBefore);
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);

            color = oldPx[i];
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            r = (r - r1 + 127);
            g = (g - g1 + 127);
            b = (b - b1 + 127);
            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }
```

![效果图](http://7xljei.com1.z0.glb.clouddn.com/716746336819656539.jpg)

## Android图像处理之图形特效处理

### Android变形矩阵-Matrix

通过Matrix类来操作图形矩阵的变换
```java
private float[] mImageMatrix=new float[9];
Matrix matrix=new Matrix();
matrix.setValues(mImageMatrix);
//当获得一个变换矩阵以后通过以下代码将一个图像以这个变换矩阵的形式绘制出来
canvas.drawBitmap(mBitmap,matrix,null);
```
以下是一些变换的方法：

* setScale(); 缩放变换

* setSkew(); 错切变换

* setRotate(); 旋转变换

* setTranslate(); 平移变换

* pre()和post() 提供矩阵的前乘和后乘运算

具体案例：
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <me.jarvischen.viewmechanism.ImageMatrixView
        android:id="@+id/view"
        android:layout_width="fill_parent"
        android:layout_height="0dp"
        android:layout_weight="2" />

    <GridLayout
        android:id="@+id/grid_group"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_gravity="center_horizontal"
        android:layout_weight="3"
        android:columnCount="3"
        android:rowCount="3">

    </GridLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <Button
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:onClick="change"
            android:text="Change" />

        <Button
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:onClick="reset"
            android:text="Reset" />
    </LinearLayout>

</LinearLayout>
```

ImageMatrixView.java
```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class ImageMatrixView extends View {

    private Matrix mMatrix;
    private Bitmap mBitmap;

    public ImageMatrixView(Context context) {
        super(context);
        initMyView();
    }

    public ImageMatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView();
    }

    public ImageMatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyView();
    }

    public void setImageAndMatrix(Bitmap bm, Matrix matrix) {
        mMatrix = matrix;
        mBitmap = bm;
    }

    public void initMyView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        setImageAndMatrix(bitmap, new Matrix());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
}
```
ImageMatrixActivity.java

```java
package me.jarvischen.viewmechanism;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

public class ImageMatrixActivity extends AppCompatActivity {
    private GridLayout mGridGroup;
    private ImageMatrixView mMyView;
    private Bitmap mBitmap;
    private int mEtWidth = 0;
    private int mEtHeight = 0;
    private float[] mImageMatrix = new float[9];
    private EditText[] mETs = new EditText[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_matrix);
        mGridGroup = (GridLayout) findViewById(R.id.grid_group);
        mMyView = (ImageMatrixView) findViewById(R.id.view);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);

        mGridGroup.post(new Runnable() {
            @Override
            public void run() {
                mEtWidth = mGridGroup.getWidth() / 3;
                mEtHeight = mGridGroup.getHeight() / 3;
                addEts();
                initImageMatrix();
            }
        });
    }

    private void addEts() {
        for (int i = 0; i < 9; i++) {
            EditText et = new EditText(ImageMatrixActivity.this);
            et.setGravity(Gravity.CENTER);
            mETs[i] = et;
            mGridGroup.addView(et, mEtWidth, mEtHeight);
        }
    }

    private void getImageMatrix() {
        for (int i = 0; i < 9; i++) {
            EditText et = mETs[i];
            mImageMatrix[i] = Float.valueOf(et.getText().toString());
        }
    }

    private void initImageMatrix() {
        for (int i = 0; i < 9; i++) {
            if (i % 4 == 0) {
                mETs[i].setText(String.valueOf(1));
            } else {
                mETs[i].setText(String.valueOf(0));
            }
        }
    }

    public void change(View view) {
        getImageMatrix();
        Matrix matrix = new Matrix();
        matrix.setValues(mImageMatrix);

//        matrix.setRotate(45);
//        matrix.postTranslate(200, 200);

//        matrix.setTranslate(200, 200);
//        matrix.preRotate(45);

//        matrix.setScale(1, -1);
//        matrix.postRotate(45);
//        matrix.postTranslate(0, 200);


        mMyView.setImageAndMatrix(mBitmap, matrix);
        mMyView.invalidate();
    }

    public void reset(View view) {
        initImageMatrix();
        getImageMatrix();
        Matrix matrix = new Matrix();
        matrix.setValues(mImageMatrix);
        mMyView.setImageAndMatrix(mBitmap, matrix);
        mMyView.invalidate();
    }
}
```

![效果图](http://7xljei.com1.z0.glb.clouddn.com/603693274204448598.jpg)


### 像素块分析
```java
drawBitmapMesh(Bitmap bitmap,int meshWidth,int meshHeight,float[] verts,
float vertOffset,int[] colors,int colorOffset,Paint paint);
```
方法可以把图像分成一个一个的小块进行处理，其中各个参数的含义如下：

* bitmap——将要操作的图像
* meshWidth——需要的横向网格数
* meshHeight——需要的纵向网格数
* verts——网格交叉点坐标数组
* verftOffset——数组中开始跳过的（x，y）坐标数目
* 其中meshWidth和meshHeight以及verts的对应关系为：

```java
float[] verts = new float[(meshWidth + 1) * (meshHeight + 1)]
```

下面的代码实现了飘扬的旗帜的效果：

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class FlagBitmapMeshView extends View {

    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] orig = new float[COUNT * 2];
    private Bitmap bitmap;
    private float A;
    private float k = 1;

    public FlagBitmapMeshView(Context context) {
        super(context);
        initView(context);
    }

    public FlagBitmapMeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FlagBitmapMeshView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setFocusable(true);
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.icon);
        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = bitmapHeight * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = bitmapWidth * x / WIDTH;
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy + 100;
                index += 1;
            }
        }
        A = 50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        flagWave();
        k += 0.1F;
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT,
                verts, 0, null, 0, null);
        invalidate();
    }

    private void flagWave() {
        for (int j = 0; j <= HEIGHT; j++) {
            for (int i = 0; i <= WIDTH; i++) {
                verts[(j * (WIDTH + 1) + i) * 2 + 0] += 0;
                float offsetY =
                        (float) Math.sin((float) i / WIDTH * 2 * Math.PI +
                                Math.PI * k);
                verts[(j * (WIDTH + 1) + i) * 2 + 1] =
                        orig[(j * WIDTH + i) * 2 + 1] + offsetY * A;
            }
        }
    }
}
```
在布局中使用该View即可。

![效果图](http://7xljei.com1.z0.glb.clouddn.com/494707027301025081.jpg)


## Android图像处理之画笔特效处理

### PorterDuffXfermode

![PorterDuffXfermode](http://7xljei.com1.z0.glb.clouddn.com/20160120092932372.png)

ProterDuffXfermode设置的是两个图层交集区域的显示方式,dst是先画的图形,而src是后画的图形。
经常用到DST_IN、SRC_IN模式来实现将一个矩形图片变成圆角或者圆形图片的效果。
如下：先用一个普通画笔画画一个遮罩层,再用带ProterDuffXfermode的画笔将图形画在罩层上,这样就可以通过上面所说的效果来混合两个图像了。

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class XformodeView extends View {

    private Bitmap bitmap;

    private Bitmap out;

    private Paint paint;

    public XformodeView(Context context) {
        this(context, null);
    }

    public XformodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XformodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        out = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), 160, 160, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(out, 0, 0, null);
    }
}
```
在相应的布局中:
```xml
 <me.jarvischen.viewmechanism.XformodeView
        android:id="@+id/test"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_centerHorizontal="true"
        android:layout_margin="20dp" />
```


![效果图](http://7xljei.com1.z0.glb.clouddn.com/297288634849395096.jpg)

下面实现刮刮乐效果。

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 * 刮刮乐
 */
public class XformodeView2 extends View {

    private Paint paint;

    private Path path;

    private Bitmap bgBitmap;

    private Bitmap fgBitmap;

    private Canvas canvas;


    public XformodeView2(Context context) {
        this(context, null);
    }

    public XformodeView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XformodeView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        path = new Path();

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        fgBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);


        canvas = new Canvas(fgBitmap);
        canvas.drawColor(Color.GRAY);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(fgBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
        }
        canvas.drawPath(path, paint);
        invalidate();
        return true;
    }
}
```

需要注意的是：

第一,初始化一些东西：
```java
private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        path = new Path();

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        fgBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);


        canvas = new Canvas(fgBitmap);
        canvas.drawColor(Color.GRAY);
    }
```

其中,
```java
paint.setStrokeJoin(Paint.Join.ROUND);
paint.setStrokeCap(Paint.Cap.ROUND);
```
是让笔触和连接处更加圆滑一些。

第二,获取用户手指滑动所产生的路径,使用Path保存用户手指划过的路径。
```java
@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
        }
        canvas.drawPath(path, paint);
        invalidate();
        return true;
    }
```

第三,需要将画笔的透明度设置为0,这样才能显示出擦除的效果。


![刮刮乐](http://7xljei.com1.z0.glb.clouddn.com/353223673934444001.jpg)

在使用PorterDuffXfermode时,最好在绘图时关闭硬件加速,因为有些模式不支持硬件加速。

### Shader

Shader称为着色器、渲染器,它用来实现一系列的渐变、渲染效果,Android中的Shader包括以下几种

* BitmapShader——位图Shader
* LinearGradient——线性Shader
* RadialGradient——光束Shader
* SweepGradient——梯度Shader
* ComposeShader——混合Shader

与其他几个不同,BitmapShader产生的是一个图像,它的作用是通过Paint对画布进行指定Bitmap填充,填充时有以下几种模式选择：

* CLAMP拉伸——拉伸的是图片的最后那一个像素
* REPEAT重复——横向、纵向不断重复
* MIRROR镜像——横向、纵向不断反转重复

关于这个Shader,下面这个[博客](http://blog.csdn.net/aigestudio/article/details/41799811)阐述的很清楚。

下面是BitmapShader的使用示例(讲一个矩形的图片变成一张圆形的图片)

```java
 private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2 ,bitmap.getHeight()/2 + 100,200,paint);
    }
```
![效果图](http://7xljei.com1.z0.glb.clouddn.com/153428206998797874.jpg)

再看看改变更改模式的效果如何：

```java
private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint = new Paint();
        paint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2 ,bitmap.getHeight()/2 + 100,200,paint);
    }
```

![效果图](http://7xljei.com1.z0.glb.clouddn.com/602647945883516863.jpg)

再看看最常用的Shader---LinearGradient的用法：

```java
 private void init() {
        paint = new Paint();
        paint.setShader(new LinearGradient(0,0,400,400, Color.BLUE,Color.YELLOW,
                Shader.TileMode.REPEAT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,400,400,paint);
    }
```

![效果图](http://7xljei.com1.z0.glb.clouddn.com/368792901625564032.jpg)

通常这些渐变效果不会单独直接的使用,而是会与前面的PorterDuffXformode结合一起使用,
下面的实例是两个东西一起使用实现倒影效果的案例。

* 先把原图复制一份并进行翻转

```java
 srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        Matrix matrix = new Matrix();
        matrix.setScale(1F, -1F);
        refBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
```

`matrix.setScale(1F, -1F);`这个方法用来实现图片的垂直翻转。

* 在onDraw方法中,首先绘制两张图：原图和倒影图,接下来,在倒影图上绘制一个图样大小的渐变矩形,
并通过Mode.DST_IN模式绘制到倒影图中,从而形成一个具有过渡效果的渐变层。

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class ReflectView extends View {

    private Bitmap srcBitmap;
    private Bitmap refBitmap;

    private Paint paint;

    private PorterDuffXfermode xfermode;

    public ReflectView(Context context) {
        this(context, null);
    }

    public ReflectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReflectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        Matrix matrix = new Matrix();
        matrix.setScale(1F, -1F);
        refBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);

        paint = new Paint();
        paint.setShader(new LinearGradient(0, srcBitmap.getHeight(), 0,
                srcBitmap.getHeight() + srcBitmap.getHeight() / 4,
                0XDD000000, 0X10000000, Shader.TileMode.CLAMP));
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.drawBitmap(refBitmap, 0, srcBitmap.getHeight(), null);
        paint.setXfermode(xfermode);
        canvas.drawRect(0, srcBitmap.getHeight(), refBitmap.getWidth(), srcBitmap.getHeight()*2, paint);
        paint.setXfermode(null);
    }
}
```

![图片倒影](http://7xljei.com1.z0.glb.clouddn.com/130363963826656789.jpg)

### PathEffect

这个类本身并没有做什么特殊的处理,只是继承了Object,通常我们使用的是它的几个子类。在使用的时候,通常是
PathEffect pe = new 一个具体的子类;
然后使用Paint的setPathEffect(PathEffect pe)方法即可。

具体的如图所示：

![效果图](http://7xljei.com1.z0.glb.clouddn.com/422970989822569566.jpg)

* 没加处理的效果

* CornerPathEffect：

这个类的作用就是将Path的各个连接线段之间的夹角用一种更平滑的方式连接,类似于圆弧与切线的效果。
一般的,通过CornerPathEffect(float radius)指定一个具体的圆弧半径来实例化一个CornerPathEffect。

* DiscretePathEffect：

这个类的作用是打散Path的线段,使得在原来路径的基础上发生打散效果。
一般的,通过构造DiscretePathEffect(float segmentLength,float deviation)来构造一个实例,其中,segmentLength指定最大的段长,
deviation指定偏离量。

* DashPathEffect：

这个类的作用就是将Path的线段虚线化。
构造函数为DashPathEffect(float[] intervals, float offset),其中intervals为虚线的ON和OFF数组,该数组的length必须大于等于2,
phase为绘制时的偏移量。


* PathDashPathEffect：

这个类的作用是使用Path图形来填充当前的路径,其构造函数为PathDashPathEffect (Path shape, float advance, float phase,
PathDashPathEffect.Stylestyle)。
shape则是指填充图,advance指每个图形间的间距,phase为绘制时的偏移量,style为该类自由的枚举值,
有三种情况：Style.ROTATE、Style.MORPH和
Style.TRANSLATE。其中ROTATE的情况下,线段连接处的图形转换以旋转到与下一段移动方向相一致的角度进行转转,
MORPH时图形会以发生拉伸或压缩等变形的情况与下一段相连接,TRANSLATE时，图形会以位置平移的方式与下一段相连接。

* ComposePathEffect：

组合效果，这个类需要两个PathEffect参数来构造一个实例,ComposePathEffect (PathEffect outerpe,PathEffect innerpe),表现时,
会首先将innerpe表现出来,然后再在innerpe的基础上去增加outerpe的效果。

## SurfaceView

### SurfaceView和View的区别

一般的View通过刷新来重绘视图,Android系统通过发出VSYNC信号来进行屏幕的重绘,刷新的时间间隔是16ms。
如果在16ms内View完成了所需要执行的操作,那么用户在视觉上就不会产生卡顿的感觉;
而如果执行的逻辑太多,特别是需要频繁刷新的界面,如游戏界面,那么就会不断的阻塞主线程,从而导致界面卡顿。
为了避免这种问题,Android提供了SurfaceView来解决这个问题。
SurfaceView和View的区别主要是下面几点：

* View主要适用于主动更新的情况,而SurfaceView适用于被动更新,例如频繁的刷新。
* View在主线程中对画面进行刷新,而SurfaceView通常会在一个子线程中进行页面的刷新。
* View在绘图时没有使用双缓冲机制,而SurfaceView在底层实现了双缓冲机制。

### SurfaceView的使用

* 创建SurfaceView,一般继承自SurfaceView,并实现接口SurfaceHolderCallback。

SurfaceHolderCallback接口的三个回调方法
```java
@Override
public void surfaceCreated(SurfaceHolder holder) {
  //做一些初始化操作，例如开启子线程通过循环来实现不停地绘制
}

@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
}
```

* 初始化SurfaceView

初始化SurfaceHolder对象，并设置Callback
```java
private void initView() {
    mHolder = getHolder();
    mHolder.addCallback(this);
    ......
}
```

* 使用SurfaceView

通过lockCanvas方法获取Canvas对象进行绘制,并通过unlockCanvasAndPost方法对画布内容进行提交
需要注意的是每次调用lockCanvas拿到的Canvas都是同一个Canvas对象,所以之前的操作都会保留,如果需要擦出,
可以在绘制之前调用`drawColor`方法来进行清屏。
```java
private void draw() {
    try {
        mCanvas = mHolder.lockCanvas();
        //mCanvas draw something
    } catch (Exception e) {
    } finally {
        if (mCanvas != null)
            mHolder.unlockCanvasAndPost(mCanvas);
    }
}
```
下面是模板代码：

```java
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;
    private Path mPath;
    private Paint mPaint;

    public MySurfaceView(Context context) {
        super(context);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs,
                      int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(40);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (mIsDrawing) {
            draw();
        }
        long end = System.currentTimeMillis();
        // 50 - 100ms，经验值
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
```

### 案例：正弦曲线

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by chenfuduo on 2016/3/10.
 */
public class MySurfaceView  extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private SurfaceHolder holder;

    private Canvas canvas;

    private boolean isDrawing;

    private Path path;

    private Paint paint;

    private int x,y;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        path.moveTo(0,400);
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        while (isDrawing){
            draw();
            x+=1;
            y = (int) (100*Math.sin(x*2*Math.PI/180) + 400);
            path.lineTo(x,y);
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(path,paint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null){
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
```

效果如下：

![效果图](http://7xljei.com1.z0.glb.clouddn.com/530942805244665603.jpg)

### 案例：复杂数学曲线的绘制

来自[我的博客](http://chenfuduo.me/2016/01/29/fun-math-curves/)

![效果图](http://7xljei.com1.z0.glb.clouddn.com/S60129-161610.jpg)

### 绘图板

```java
package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by chenfuduo on 2016/3/10.
 */
public class SimpleDrawWithSurfaceView  extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;
    private Path mPath;
    private Paint mPaint;

    public SimpleDrawWithSurfaceView(Context context) {
        super(context);
        initView();
    }

    public SimpleDrawWithSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SimpleDrawWithSurfaceView(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(40);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (mIsDrawing) {
            draw();
        }
        long end = System.currentTimeMillis();
        // 50 - 100ms，经验值
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
```

这里时间的判断是因为我们不需要他那么频繁的刷新。

![效果图](http://7xljei.com1.z0.glb.clouddn.com/223678467572715827.jpg)

