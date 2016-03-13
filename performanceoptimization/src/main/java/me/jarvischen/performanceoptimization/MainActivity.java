package me.jarvischen.performanceoptimization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewStub viewStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewStub = (ViewStub) findViewById(R.id.not_ofen_use);
    }
    public void btnVisible(View view){
        viewStub.setVisibility(View.VISIBLE);
    }
    public void btnInflate(View view){
        View view1 = viewStub.inflate();
        TextView tv = (TextView) view1.findViewById(R.id.tv);
        tv.setText("HAHA");
    }
}
