package me.jarvischen.animationmechanism;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BasicPropertyAnimationActivity extends AppCompatActivity {

    private static final String TAG = "BasicProperty";
    private ImageView iv;

    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        iv = (ImageView) findViewById(R.id.iv);
        ll = (LinearLayout) findViewById(R.id.ll);
        AlphaAnimation aa = new AlphaAnimation(0f,1f);
        aa.setDuration(3000);
        LayoutAnimationController lac = new LayoutAnimationController(aa,0.5f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        ll.setLayoutAnimation(lac);
        //从xml加载动画
        final Animator animator = AnimatorInflater.loadAnimator(this, R.animator.animatorset_set);
        animator.setTarget(iv);
        animator.setDuration(3000);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator.start();
            }
        });
    }

    public void valueAnimator1(View view) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(55, 233);
        valueAnimator.setDuration(3000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate=" + animatedValue);
            }
        });
    }

    public void objectAnimator1Alpha(View view) {
        //透明度
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv, "alpha", 1f, 0f, 1f);
        objectAnimator.setDuration(1500);
        objectAnimator.setRepeatCount(100);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.start();


        //添加Animator监听器(接口的四个方法全部实现了)
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void objectAnimator1Rotation(View view) {
        //旋转
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv, "rotation", 0f, 360f);
        objectAnimator.setDuration(1500);
        objectAnimator.setRepeatCount(100);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.start();

        //添加Animator监听器(AnimatorListenerAdapter这种方式只需要选择自己需要的方法即可,不需要四个全部实现)
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    public void objectAnimator1translationX(View view) {
        //平移
        float translationX = iv.getTranslationX();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv, "translationX", translationX, -500f, translationX);
        objectAnimator.setDuration(1500);
        objectAnimator.setRepeatCount(100);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.start();
    }

    public void objectAnimator1scaleY(View view) {
        //缩放
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv, "scaleY", 1f, 1 / 2f, 1f);
        objectAnimator.setDuration(1500);
        objectAnimator.setRepeatCount(100);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.start();
    }

    public void objectAnimator1set(View view) {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(iv, "translationX", -500f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(iv, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(iv, "alpha", 1f, 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotate).with(fadeInOut).after(moveIn);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

}
