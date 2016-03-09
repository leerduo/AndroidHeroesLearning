package me.jarvischen.viewmechanism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void basicView(View view) {
        Intent intent = new Intent(MainActivity.this, BasicViewActivity.class);
        startActivity(intent);
    }

    public void primaryColor(View view) {
        Intent intent = new Intent(MainActivity.this, PrimaryColorActivity.class);
        startActivity(intent);
    }

    public void colorMatrix(View view) {
        Intent intent = new Intent(MainActivity.this, ColorMatrixActivity.class);
        startActivity(intent);
    }

    public void pixelsEffect(View view) {
        Intent intent = new Intent(MainActivity.this, PixelsEffectActivity.class);
        startActivity(intent);
    }

    public void imageMatrix(View view) {
        Intent intent = new Intent(MainActivity.this, ImageMatrixActivity.class);
        startActivity(intent);
    }
}
