package me.jarvischen.listviewdemo;

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

    public void baseListView(View view){
        Intent intent = new Intent(MainActivity.this,BaseListViewActivity.class);
        startActivity(intent);
    }

    public void scrollToHide(View view){
        Intent intent = new Intent(MainActivity.this,ScrollToHideActivity.class);
        startActivity(intent);
    }
    public void scrollToChat(View view){
        Intent intent = new Intent(MainActivity.this,ChatListActivity.class);
        startActivity(intent);
    }

    public void focusListView(View view){
        Intent intent = new Intent(MainActivity.this,FocusListViewActivity.class);
        startActivity(intent);
    }



}
