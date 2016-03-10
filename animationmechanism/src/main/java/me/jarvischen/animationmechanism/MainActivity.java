package me.jarvischen.animationmechanism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.jarvischen.animationmechanism.advancepropertyanimation.CustomAnimation;
import me.jarvischen.animationmechanism.advancepropertyanimation.CustomAnimation2;
import me.jarvischen.animationmechanism.advancepropertyanimation.PropertyAnimationAdvanceActivity;
import me.jarvischen.animationmechanism.advancepropertyanimation.PropertyValuesHolderActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewAnimate(View view) {
        Intent intent = new Intent(MainActivity.this, ViewAnimationActivity.class);
        startActivity(intent);
    }

    public void propertyAnimate(View view) {
        Intent intent = new Intent(MainActivity.this, BasicPropertyAnimationActivity.class);
        startActivity(intent);
    }

    public void propertyAnimateAdvance(View view) {
        Intent intent = new Intent(MainActivity.this, PropertyAnimationAdvanceActivity.class);
        startActivity(intent);
    }


    public void PropertyValuesHolder(View view) {
        Intent intent = new Intent(MainActivity.this, PropertyValuesHolderActivity.class);
        startActivity(intent);
    }

    public void imgClose(View view){
        CustomAnimation ca = new CustomAnimation();
        view.startAnimation(ca);
    }

    public void btnAnim(View view) {
        CustomAnimation2 customAnim = new CustomAnimation2();
        customAnim.setmRotateY(30);
        view.startAnimation(customAnim);
    }

}
