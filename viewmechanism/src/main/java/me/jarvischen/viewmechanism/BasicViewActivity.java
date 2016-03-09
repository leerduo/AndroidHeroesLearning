package me.jarvischen.viewmechanism;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BasicViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_view);
    }
}
