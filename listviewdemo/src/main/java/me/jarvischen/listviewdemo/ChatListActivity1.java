package me.jarvischen.listviewdemo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;



public class ChatListActivity1 extends AppCompatActivity {
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list  );

        mListView = (ListView) findViewById(R.id.listView_chat);
        ChatItemListViewBean bean1 = new ChatItemListViewBean();
        bean1.setType(0);
        bean1.setIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.in_icon));
        bean1.setText("Hello how are you?");

        ChatItemListViewBean bean2 = new ChatItemListViewBean();
        bean2.setType(1);
        bean2.setIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.in_icon));
        bean2.setText("Fine thank you, and you?");

        ChatItemListViewBean bean3 = new ChatItemListViewBean();
        bean3.setType(0);
        bean3.setIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.in_icon));
        bean3.setText("I am fine too");

        ChatItemListViewBean bean4 = new ChatItemListViewBean();
        bean4.setType(1);
        bean4.setIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.in_icon));
        bean4.setText("Bye bye");

        ChatItemListViewBean bean5 = new ChatItemListViewBean();
        bean5.setType(0);
        bean5.setIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.in_icon));
        bean5.setText("See you");

        List<ChatItemListViewBean> data = new ArrayList<>();
        data.add(bean1);
        data.add(bean2);
        data.add(bean3);
        data.add(bean4);
        data.add(bean5);
        mListView.setAdapter(new ChatItemListViewAdapter(this, data));
    }


    private void test(){
        //test
    }
}
