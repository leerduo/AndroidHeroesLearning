package me.jarvischen.systeminformation;

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

    public void systemInfo(View view) {
        Intent intent = new Intent(MainActivity.this, SystemInfoActivity.class);
        startActivity(intent);
    }

    public void btnPM(View view) {
        Intent intent = new Intent(MainActivity.this, PackageManagerActivity.class);
        startActivity(intent);
    }

    public void btnAM(View view) {
        Intent intent = new Intent(MainActivity.this, ActivityManagerActivity.class);
        startActivity(intent);
    }

}
