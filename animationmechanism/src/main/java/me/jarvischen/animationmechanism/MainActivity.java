package me.jarvischen.animationmechanism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.jarvischen.animationmechanism.advancepropertyanimation.PropertyAnimationAdvanceActivity;

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


}
