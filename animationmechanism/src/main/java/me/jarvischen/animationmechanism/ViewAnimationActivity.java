package me.jarvischen.animationmechanism;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class ViewAnimationActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void btnAlpha(View view) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(3000);
        iv.startAnimation(aa);
    }

    public void btnRotate(View view) {
        //方案1
       /* RotateAnimation ra = new RotateAnimation(0,360,100,100);
        ra.setDuration(3000);
        iv.startAnimation(ra);*/

        //方案2
        RotateAnimation ra = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(3000);
        iv.startAnimation(ra);
    }

    public void btnTranslate(View view) {
        TranslateAnimation ta = new TranslateAnimation(0, 200, 0, 300);
        ta.setDuration(3000);
        iv.startAnimation(ta);
    }

    public void btnScale(View view) {
        //方案1
        /*ScaleAnimation sa = new ScaleAnimation(0,2,0,2);
        sa.setDuration(3000);
        iv.startAnimation(sa);*/
        //方案2
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(3000);
        iv.startAnimation(sa);
    }

    public void btnSet(View view){
        AnimationSet as = new AnimationSet(true);
        as.setDuration(1500);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1500);
        as.addAnimation(aa);

        TranslateAnimation ta = new TranslateAnimation(0, 100, 0, 200);
        ta.setDuration(1500);
        as.addAnimation(ta);

        iv.startAnimation(as);
    }
}
