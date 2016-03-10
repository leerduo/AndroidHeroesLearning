package me.jarvischen.animationmechanism.advancepropertyanimation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.jarvischen.animationmechanism.R;

public class PropertyAnimationAdvanceActivity extends AppCompatActivity {

    private MyAnimView myAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation_advance);
        myAnimView = (MyAnimView) findViewById(R.id.myanimview);
       /* ObjectAnimator anim = ObjectAnimator.ofObject(myAnimView, "color", new ColorEvaluator(),
                "#0000FF", "#FF0000");
        anim.setDuration(2500);
        anim.start();
        anim.setRepeatCount(100);
        anim.setRepeatMode(ObjectAnimator.REVERSE);*/
    }
}
