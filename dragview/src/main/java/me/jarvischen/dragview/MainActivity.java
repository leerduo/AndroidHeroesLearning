package me.jarvischen.dragview;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    public void dragTest(View view){
        Intent intent = new Intent(MainActivity.this,ViewDragHelperTestActivity.class);
        startActivity(intent);
    }
}
