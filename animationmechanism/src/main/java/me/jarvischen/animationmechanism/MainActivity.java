package me.jarvischen.animationmechanism;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import me.jarvischen.animationmechanism.advancepropertyanimation.AdvancePropertyAnimActivity_demo;
import me.jarvischen.animationmechanism.advancepropertyanimation.CustomAnimation;
import me.jarvischen.animationmechanism.advancepropertyanimation.CustomAnimation2;
import me.jarvischen.animationmechanism.advancepropertyanimation.PropertyAnimationAdvanceActivity;
import me.jarvischen.animationmechanism.advancepropertyanimation.PropertyValuesHolderActivity;
import me.jarvischen.animationmechanism.advancepropertyanimation.SVGActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate();
            }
        });
    }

    private void animate() {
        Drawable drawable = imageView1.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
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

    public void btnSVG(View view) {
        Intent intent = new Intent(MainActivity.this, SVGActivity.class);
        startActivity(intent);
    }

    public void btnMenu(View view) {
        Intent intent = new Intent(MainActivity.this, AdvancePropertyAnimActivity_demo.class);
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
