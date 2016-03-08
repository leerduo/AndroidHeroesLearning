package me.jarvischen.scrollerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
