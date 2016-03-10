package me.jarvischen.animationmechanism.advancepropertyanimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import me.jarvischen.animationmechanism.R;

public class PropertyValuesHolderActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_values_holder);
        iv = (ImageView) findViewById(R.id.iv);
        PropertyValuesHolder pvh1 =  PropertyValuesHolder.ofFloat("translationX",300f);
        PropertyValuesHolder pvh2 =  PropertyValuesHolder.ofFloat("scaleX",1f,0f,1f);
        PropertyValuesHolder pvh3 =  PropertyValuesHolder.ofFloat("scaleY",1f,0f,1f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(iv, pvh1, pvh2, pvh3);
        objectAnimator.setDuration(3000);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setRepeatCount(100);
        objectAnimator.start();
    }
}
